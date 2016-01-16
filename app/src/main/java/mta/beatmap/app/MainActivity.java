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
        fillDB();
        initializeListView();

        testReadDB();


    }

    private void testReadDB() {
        SessionDB mDbHelper = new SessionDB(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
            DBContract.SessionsTable.COLUMN_NAME_BPM,
            DBContract.SessionsTable.COLUMN_NAME_METER_NUMERATOR,
            DBContract.SessionsTable.COLUMN_NAME_METER_DENOMINATOR,
            DBContract.SessionsTable.COLUMN_NAME_BARS
        };

        String selection = DBContract.SessionsTable.COLUMN_NAME_SESSION_ID + " = ?";
        String[] selectionVals = {"1"};

        Cursor c = db.query(
                DBContract.SessionsTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The string for the WHERE clause
                selectionVals,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null // The sort order
        );

        int bpm_index = c.getColumnIndexOrThrow(DBContract.SessionsTable.COLUMN_NAME_BPM);

        c.moveToFirst();
        long itemId = c.getLong(bpm_index);

        System.out.println("itemId" + itemId);

    }

    private void fillDB() {
        // Gets the data repository in write mode
        SessionDB mDbHelper = new SessionDB(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        System.out.println("Yay!");
        
//        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DBContract.SessionsTable.COLUMN_NAME_SESSION_ID, 1);
        values.put(DBContract.SessionsTable.COLUMN_NAME_BEAT_ID, 12);
        values.put(DBContract.SessionsTable.COLUMN_NAME_BPM, 120);
        values.put(DBContract.SessionsTable.COLUMN_NAME_METER_NUMERATOR, 5);
        values.put(DBContract.SessionsTable.COLUMN_NAME_METER_DENOMINATOR, 8);
        values.put(DBContract.SessionsTable.COLUMN_NAME_BARS, 10);

        long newRowId;
        newRowId = db.insert(
                DBContract.SessionsTable.TABLE_NAME,
                null,
                values);
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
