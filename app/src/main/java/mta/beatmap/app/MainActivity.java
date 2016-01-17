package mta.beatmap.app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import mta.beatmap.app.persistence.db.DBHandler;
import mta.beatmap.app.persistence.db.models.TrackModel;
import mta.beatmap.app.ui.views.util.SequenceRVAdapter;
import mta.beatmap.app.track.SequenceVM;
import mta.beatmap.app.track.Sequence;
import mta.beatmap.app.track.Track;

public class MainActivity extends AppCompatActivity {

    private List<SequenceVM> sequenceVMList;
    private DBHandler DBHandler;

    private static final int ACTION_CREATE_SEQUENCE = 100;
    private static final int ACTION_EDIT_SEQUENCE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        testDB();


        loadTrackListView();
    }

    private void loadTrackListView(){
        RecyclerView sequenceListView = (RecyclerView)findViewById(R.id.trackList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        sequenceListView.setLayoutManager(llm);

        initializeSequenceList();

        SequenceRVAdapter adapter = new SequenceRVAdapter(sequenceVMList);
        sequenceListView.setAdapter(adapter);
    }

    private void initializeSequenceList(){
        sequenceVMList = new ArrayList<SequenceVM>();
        int imgBarId = R.mipmap.ic_short_text_white_48dp;
        int imgMeterId = R.mipmap.ic_add_white_48dp;
        int imgBpmId = R.mipmap.ic_music_note_white_48dp;

        DBHandler = new DBHandler(this);
        List<TrackModel> trackModels = DBHandler.getAllTracks();

        Track track = trackModels.get(0);

        for (int i = 0; i < track.size(); i++){
            sequenceVMList.add(new SequenceVM(track.get(i), imgBarId, imgMeterId, imgBpmId));
        }
    }

    private void testDB() {
        DBHandler tdh = new DBHandler(this);

        String trackID = "1";
        Track track = new Track();
        track.appendSequence(new Sequence(100, 4, 4, 10));
        track.appendSequence(new Sequence(150, 3, 8, 20));
        tdh.upsertTrack(trackID, track);
        Track res = tdh.getTrack(trackID);

        for (int i = 0; i < res.size(); i++) {
            System.out.println(i + ": " + res.get(i).toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private Track getCurrentTrack() {
        // TODO implement working version, this is a dummy
        Track track = new Track();
        track.appendSequence(new Sequence(100, 4, 4, 10));
        track.appendSequence(new Sequence(150, 3, 8, 20));
        return track;
    }

    private void editSequence(int sequenceIndex) {
        // TODO use this method when we want to edit a sequence
        Sequence seq = getCurrentTrack().get(sequenceIndex);

        Intent intent = new Intent(this, EditSequenceActivity.class);
        intent.setAction(EditSequenceActivity.ACTION_EDIT);
        intent.putExtra("sequenceIndex", sequenceIndex);
        intent.putExtra("sequence", seq.pack());
        startActivityForResult(intent, ACTION_EDIT_SEQUENCE);
    }

    public void onCreateSequence(View view){
        Intent intent = new Intent(this, EditSequenceActivity.class);
        intent.setAction(EditSequenceActivity.ACTION_CREATE);
        startActivityForResult(intent, ACTION_CREATE_SEQUENCE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_CREATE_SEQUENCE:
                System.out.println("Returned as CREATE");
                // If everything went well, we should get a new sequence to insert in the track
                if (resultCode == Activity.RESULT_OK) {
                    Sequence res = extractSequence(data);
                    getCurrentTrack().appendSequence(res);
                    System.out.println("OK after creating sequence " + res);
                } else {
                    System.out.println("Not OK after creating sequence");
                    // Nothing new: User cancelled. Do nothing.
                }
                break;
            case ACTION_EDIT_SEQUENCE:
                System.out.println("Returned as EDIT");
                if (resultCode == Activity.RESULT_OK) {
                    // Extract sequence and index from Intent
                    Sequence res = extractSequence(data);
                    int index = data.getIntExtra("sequenceIndex", -1);
                    System.out.println("Index " + index);
                    // replace existing with new
                    getCurrentTrack().set(index, res);

                    System.out.println("OK after editing sequence " + res.toString());
                } else if(resultCode == EditSequenceActivity.DELETE) {
                    int index = data.getIntExtra("sequenceIndex", -1);
                    if (index >= 0) {
                        System.out.println("Deleting sequence with index " + index);
                        getCurrentTrack().remove(index);
                    }
                } else {
                    System.out.println("Not OK, but not deleting, after editing sequence");
                    // Nothing new: User cancelled. Do nothing.
                }
                break;
        }
    }

    private Sequence extractSequence(Intent data) {
        // Use our hack of sending an int array
        int[] payload = data.getIntArrayExtra("sequence");
        return new Sequence(payload);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
