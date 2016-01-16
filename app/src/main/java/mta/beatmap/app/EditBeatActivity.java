package mta.beatmap.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import dk.aschoen.beatplanner.core.Beat;
import dk.aschoen.beatplanner.core.Meter;
import dk.aschoen.beatplanner.core.Metronome;
import mta.beatmap.app.metro.SimpleMetronome;

public class EditBeatActivity extends AppCompatActivity {

    private Metronome metronome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_beat);

        metronome = new SimpleMetronome(this);
        loadSpinners();
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

    }

    public void toggleMetronome(View view) {
        boolean isp = metronome.isPlaying();
        metronome.toggle();
        String btnText = isp ? "Start" : "Stop";
        Button toggleBtn = (Button) findViewById(R.id.toggleBtn);
        toggleBtn.setText(btnText);
    }
}
