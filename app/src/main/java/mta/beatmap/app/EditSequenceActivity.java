package mta.beatmap.app;

import android.content.Intent;
import android.net.ParseException;
import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import mta.beatmap.app.metro.config.Beat;
import mta.beatmap.app.metro.config.Meter;
import mta.beatmap.app.metro.Metronome;
import mta.beatmap.app.metro.SimpleMetronome;
import mta.beatmap.app.track.Sequence;

public class EditSequenceActivity extends AppCompatActivity {

    private static final int RESULT_SAVE = 100;
    private Metronome metronome;

    public final static String ACTION_EDIT = "edit";
    public final static String ACTION_CREATE = "create";
    public final static int DELETE = -2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sequence);

        metronome = new SimpleMetronome(this);

        Intent intent = getIntent();
        String action = intent.getAction();

        if (action == ACTION_EDIT) {
            Sequence seq = new Sequence(intent.getIntArrayExtra("sequence"));
            System.out.println("Edit action chosen for sequence " + seq.toString());
            metronome.setBars(seq.getBars());
            metronome.setBeat(seq.getBeat());
        } else if (action == ACTION_CREATE) {
            System.out.println("Create action chosen");
        } else {
            throw new NoSuchMethodError("Calling Intent must be ACTION_EDIT or ACTION_CREATE");
        }

        loadSpinners();
        initEditBars();

        // TODO Show track title
    }

    private void initEditBars() {
        EditText editBar = (EditText) findViewById(R.id.editBars);
        editBar.setText(Integer.toString(metronome.getBars()));
    }


    private void loadSpinners() {
        Spinner spinMeterUp = (Spinner) findViewById(R.id.meterUp);
        Spinner spinMeterDown = (Spinner) findViewById(R.id.meterDown);

        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> upperAdapter = ArrayAdapter.createFromResource(this,
            R.array.upperMeterNums, android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> lowerAdapter = ArrayAdapter.createFromResource(this,
                R.array.lowerMeterNums, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        upperAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lowerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinMeterUp.setAdapter(upperAdapter);
        spinMeterDown.setAdapter(lowerAdapter);

        Meter meter = metronome.getMeter();

        final int numerator = upperAdapter.getPosition(String.valueOf(meter.getUpper()));
        final int denominator = lowerAdapter.getPosition(String.valueOf(meter.getLower()));
        spinMeterUp.setSelection(numerator);
        spinMeterDown.setSelection(denominator);

        // set up numberpicker -> bpm
        Beat beat = metronome.getBeat();
        NumberPicker editBpm = (NumberPicker) findViewById(R.id.editBpm);
        editBpm.setMinValue(0);
        editBpm.setMaxValue(300);
        editBpm.setValue(beat.getBPM());

        final Metronome metro = metronome;
        editBpm.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                metro.setBPM(newVal);
            }
        });

        // set up spinners -> meter
        spinMeterUp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int val = Integer.parseInt(upperAdapter.getItem(position).toString());
                metro.setMeter(metro.getMeter().setUpper(val));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        spinMeterDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int val = Integer.parseInt(lowerAdapter.getItem(position).toString());
                metro.setMeter(metro.getMeter().setLower(val));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        EditText editBars = (EditText) findViewById(R.id.editBars);
        editBars.setText("4");

    }

    public void toggleMetronome(View view) {
        boolean isp = metronome.isPlaying();
        metronome.toggle();
        String btnText = (String) getText(isp ? R.string.start_beat_btn : R.string.stop_beat_btn);
        Button toggleBtn = (Button) findViewById(R.id.toggleBtn);
        toggleBtn.setText(btnText);
    }

    // public void handleSaveClick(View view) {
       // finishWithResult();
    // }

    int getBars() {
        try {
            return Integer.parseInt(String.valueOf(((EditText) findViewById(R.id.editBars)).getText()));
        } catch (NumberFormatException ex){
            return 4;
        }

    }

    // from Adam's branch
    private void finishWithResult() {
        Intent intent = new Intent();
        setResult(RESULT_SAVE, intent);
        intent.putExtra("bpm", metronome.getBPM());
        intent.putExtra("meter.upper", metronome.getMeter().getUpper());
        intent.putExtra("meter.lower", metronome.getMeter().getLower());
        intent.putExtra("bars", getBars());
        if (getParent() != null) {
            getParent().setResult(RESULT_SAVE, intent);
        }
        finish();
    }
    public int getBPM() {
        NumberPicker editBPM = (NumberPicker) findViewById(R.id.editBpm);
        return editBPM.getValue();
    }

    public int getMeterNumerator() {
        // TODO detach from metronome object, no?
        return metronome.getMeter().getNumerator();
    }

    public int getMeterDenominator() {
        // TODO detach from metronome object
        return metronome.getMeter().getDenominator();
    }

    public void onDoneEditing(View view) {
        System.out.println("clicked done editing");
        Intent intent = new Intent();
        int[] payload = new int[] {getBPM(),
                                   getMeterNumerator(),
                                   getMeterDenominator(),
                                   getBars()};
        intent.putExtra("sequence", payload);
        Intent input = getIntent();
        if (input.hasExtra("sequenceIndex")) {
            intent.putExtra("sequenceIndex", input.getIntExtra("sequenceIndex", -1));
        }
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void onDeleteBeat(View view) {
        System.out.println("clicked delete beat");

//        Intent data = new Intent();
//        data.putExtra("sequenceIndex", getIntent().getIntExtra("sequenceIndex", -1));
        setResult(DELETE, getIntent());
        finish();
        // TODO consider asking user to verify
    }

    @Override
    public void finishActivity(int requestCode) {
        super.finishActivity(requestCode);
    }

}
