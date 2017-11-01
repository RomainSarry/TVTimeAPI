package tvtimeapi.beans;

import java.util.Date;

/**
 * Created by Romain on 01/11/2017.
 */
public class TVTimeEpisode {
    private String title;

    private Date date;

    public TVTimeEpisode() {
    }

    public TVTimeEpisode(String title, Date date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
