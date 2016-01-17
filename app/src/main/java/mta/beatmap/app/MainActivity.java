package mta.beatmap.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mta.beatmap.app.persistence.db.DBHandler;
import mta.beatmap.app.persistence.db.models.TrackModel;
import mta.beatmap.app.sequenceListRV.SequenceRVAdapter;
import mta.beatmap.app.sequenceListRV.SequenceVM;
import mta.beatmap.app.track.Track;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_SEQUENCE = 100;
    private List<SequenceVM> sequenceVMList;
    private DBHandler dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        int imgBarId = R.mipmap.ic_short_text_white_48dp;
        int imgMeterId = R.mipmap.ic_add_white_48dp;
        int imgBpmId = R.mipmap.ic_music_note_white_48dp;

        dbh = new DBHandler(this);

        List<TrackModel> tracks = dbh.getAllTracks();

        Track track = tracks.get(0);
        sequenceVMList = new ArrayList<SequenceVM>();

        for (int i = 0; i < track.size(); i++){
            sequenceVMList.add(new SequenceVM(track.get(i), imgBarId, imgMeterId, imgBpmId));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onNewBeatClick(View view){
        Intent intent = new Intent(this, EditBeatActivity.class);
        startActivityForResult(intent, ADD_SEQUENCE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("RESULT", "req: " + requestCode + ", result: " + resultCode);
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
