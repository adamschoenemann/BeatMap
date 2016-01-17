package mta.beatmap.app.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.TextView;
import mta.beatmap.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mta.beatmap.app.ui.views.util.ContextMenuRecyclerView;
import static mta.beatmap.app.ui.views.util.ContextMenuRecyclerView.RecyclerViewContextMenuInfo;

public class TracksListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TracksListAdapter mAdapter;
    private List<String> tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks_list);
        String[] initialTracks = {
            "Track1","Track2","Track3"
        };

        tracks = new ArrayList<String>();
        tracks.addAll(Arrays.asList(initialTracks));

        mRecyclerView = (RecyclerView) findViewById(R.id.tracksRecyclerView);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new TracksListAdapter(tracks);
        mRecyclerView.setAdapter(mAdapter);
        registerForContextMenu(mRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tracks_list, menu);
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

    public void handleNewTrack(View view) {
        tracks.add("Track" + (tracks.size() + 1));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            // THIs is illegal -> AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            // item.getMenuInfo() is null from RecycleView
            RecyclerViewContextMenuInfo info = (RecyclerViewContextMenuInfo) item.getMenuInfo();
            int position = info.position;
            tracks.remove(position);
            mAdapter.notifyDataSetChanged();
            System.out.println("Clicked pos: " + position);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // v.getId() == id of RecyclerView
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater()
                .inflate(R.menu.menu_track_list, menu);

    }

    public class TracksListAdapter extends RecyclerView.Adapter<TracksListAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder
                implements View.OnLongClickListener, View.OnClickListener {

            // just a string for now
            public TextView mTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView.setOnLongClickListener(this);
                mTextView = (TextView)itemView.findViewById(R.id.trackListTrackTitle);
            }

            @Override
            public boolean onLongClick(View v) {
                mRecyclerView.showContextMenuForChild(v);
                return true;
            }

            @Override
            public void onClick(View v) {
               Log.d(getPackageName(), "Edit track") ;
            }
        }


        private List<String> mTracks;

        // constructor for dataset
        public TracksListAdapter(List<String> tracks) {
            mTracks = tracks;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public TracksListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tracks_list_item_view, parent, false);
            // set the view's size, margins, paddings and layout parameters

            ViewHolder vh = new TracksListAdapter.ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mTextView.setText(mTracks.get(position));

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mTracks.size();
        }
    }
}
