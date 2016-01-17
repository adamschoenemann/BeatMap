package mta.beatmap.app.ui.views.util;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mta.beatmap.app.R;
import mta.beatmap.app.track.SequenceVM;

/**
 * Created by mickneupart on 17/01/16.
 */
public class SequenceRVAdapter extends RecyclerView.Adapter<SequenceRVAdapter.TrackViewHolder> {

    public class TrackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView seqMeter;
        TextView seqBpm;
        TextView seqBars;
        ImageView imgMeter;
        ImageView imgBpm;
        ImageView imgBars;

        TrackViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.trackList);
            seqMeter = (TextView)itemView.findViewById(R.id.seqTextMeter);
            seqBpm = (TextView)itemView.findViewById(R.id.seqTextBpm);
            seqBars = (TextView)itemView.findViewById(R.id.seqTextBar);

            imgMeter = (ImageView)itemView.findViewById(R.id.seqImgMeter);
            imgBpm = (ImageView)itemView.findViewById(R.id.seqImgBpm);
            imgBars = (ImageView)itemView.findViewById(R.id.seqImgBar);

            this.itemView.setOnClickListener(this);
        }

        public void onClick(View v) {

            SequenceRVAdapter.this.sequences.
            System.out.println("sequence clicked!");
        }
    }

    private List<SequenceVM> sequences;

    public SequenceRVAdapter(List<SequenceVM> sequences){
        this.sequences = sequences;
    }

    @Override
    public int getItemCount(){
        return sequences.size();
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        TrackViewHolder pvh = new TrackViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(TrackViewHolder trackViewHolder, int i) {
        trackViewHolder.seqBars.setText(Integer.toString(sequences.get(i).getSeq().getBars()));
        trackViewHolder.seqBpm.setText(Integer.toString(sequences.get(i).getSeq().getBeat().getBPM()));
        trackViewHolder.seqMeter.setText(sequences.get(i).getSeq().getBeat().getMeter().toString());

        trackViewHolder.imgBars.setImageResource(sequences.get(i).getImgBar());
        trackViewHolder.imgBpm.setImageResource(sequences.get(i).getImgBpm());
        trackViewHolder.imgMeter.setImageResource(sequences.get(i).getImgMeter());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}

