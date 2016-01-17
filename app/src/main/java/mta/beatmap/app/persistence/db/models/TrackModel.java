package mta.beatmap.app.persistence.db.models;

import mta.beatmap.app.track.Track;

/**
 * Created by Adam on 2016-01-17.
 */
public class TrackModel extends Track {

    public final int id;
    private String title;

    public TrackModel(int id, String title) {
        super();
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
