package mta.beatmap.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import mta.beatmap.app.metro.config.Beat;
import mta.beatmap.app.metro.config.Meter;
import mta.beatmap.app.persistence.db.TrackDBHandler;
import mta.beatmap.app.sequenceListRV.SequenceRVAdapter;
import mta.beatmap.app.sequenceListRV.SequenceVM;
import mta.beatmap.app.track.Sequence;
import mta.beatmap.app.track.Track;

public class MainActivity extends AppCompatActivity {

    private List<SequenceVM> sequenceVMList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testDB();

        RecyclerView sequenceListView = (RecyclerView)findViewById(R.id.trackList);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
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
        Sequence seq = new Sequence(new Beat(new Meter(1,4),120),10);

        sequenceVMList.add(new SequenceVM(seq,imgBarId,imgMeterId,imgBpmId));
        sequenceVMList.add(new SequenceVM(seq,imgBarId,imgMeterId,imgBpmId));
        sequenceVMList.add(new SequenceVM(seq,imgBarId,imgMeterId,imgBpmId));
        sequenceVMList.add(new SequenceVM(seq,imgBarId,imgMeterId,imgBpmId));
        sequenceVMList.add(new SequenceVM(seq,imgBarId,imgMeterId,imgBpmId));
        sequenceVMList.add(new SequenceVM(seq,imgBarId,imgMeterId,imgBpmId));
    }

    private void testDB() {
        TrackDBHandler tdh = new TrackDBHandler(this);

        String trackID = "1";
        Track track = new Track();
        track.appendSequence(new Sequence(100, 4, 4, 10));
        track.appendSequence(new Sequence(150, 3, 8, 20));
        tdh.upsert(trackID, track);
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

    public void onNewBeatClick(View view){
        /*
        Context context = getApplicationContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        */

        Intent intent = new Intent(this, EditBeatActivity.class);
        startActivity(intent);
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
