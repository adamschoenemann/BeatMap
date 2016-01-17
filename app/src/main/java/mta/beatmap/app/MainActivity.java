package mta.beatmap.app;

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
import mta.beatmap.app.sequenceListRV.SequenceRVAdapter;
import mta.beatmap.app.sequenceListRV.SequenceVM;
import mta.beatmap.app.track.Sequence;
import mta.beatmap.app.track.Track;

public class MainActivity extends AppCompatActivity {

    private List<SequenceVM> sequenceVMList;
    private DBHandler DBHandler;

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
        sequenceVMList = new ArrayList<SequenceVM>();
        int imgBarId = R.mipmap.ic_short_text_white_48dp;
        int imgMeterId = R.mipmap.ic_add_white_48dp;
        int imgBpmId = R.mipmap.ic_music_note_white_48dp;

        DBHandler = new DBHandler(this);
        Track track = DBHandler.getTrack(1);

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
