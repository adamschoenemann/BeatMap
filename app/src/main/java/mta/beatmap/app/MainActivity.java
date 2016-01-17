package mta.beatmap.app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import mta.beatmap.app.persistence.db.TrackDBHandler;
import mta.beatmap.app.track.Sequence;
import mta.beatmap.app.track.Track;

public class MainActivity extends AppCompatActivity {

    private static final int ACTION_CREATE_SEQUENCE = 0;
    private static final int ACTION_EDIT_SEQUENCE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testDB();
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
        intent.setAction(EditBeatActivity.ACTION_CREATE);
        startActivityForResult(intent, ACTION_CREATE_SEQUENCE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_CREATE_SEQUENCE:
                // If everything went well, we should get a new sequence to insert in the track
                if (resultCode == Activity.RESULT_OK) {
                    System.out.println("OK after creating sequence");
                    // TODO read sequence from data
                    // TODO append read sequence to track
                } else {
                    System.out.println("Not OK after creating sequence");
                    // Nothing new: User cancelled. Do nothing.
                }
            case ACTION_EDIT_SEQUENCE:
                System.out.println("Returned after editing sequence");
        }
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
