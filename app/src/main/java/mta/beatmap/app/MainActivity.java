package mta.beatmap.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testDB();
        initializeListView();
    }

    private void testDB() {
        SessionDBHandler sdh = new SessionDBHandler(this);

        String sessionID = "1";
        sdh.upsert(sessionID , new String[] {"my ignored input"} );
        String[] res = sdh.getSession(sessionID );

        for (int i = 0; i < res.length; i++) {
            System.out.println(i + ": " + res[i]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    private void initializeListView(){
        ListView lv = (ListView) findViewById(R.id.listView);
        ArrayList<String> myStringArray1 = new ArrayList<String>();
        myStringArray1.add("something");
        Collections.addAll(myStringArray1, new String[]{"Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2"});

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myStringArray1);
        ArrayList<TextView> views = new ArrayList<TextView>();
        ArrayAdapter<TextView> adapter2 = new ArrayAdapter<TextView>(this, android.R.layout.simple_list_item_1, views);
        lv.setAdapter(adapter);

        // Create a message handling object as an anonymous class.
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // Do something in response to the click
                System.out.println("Clicked! " + ((TextView)v).getText().toString());
            }
        };

        lv.setOnItemClickListener(mMessageClickedHandler);
    }
}
