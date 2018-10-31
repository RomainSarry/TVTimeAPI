package tvtimeapi.beans;

import java.util.Date;

/**
 * Created by Romain on 01/11/2017.
 */
public class TVTimeEpisode {

	public static final String EPISODE_NUMBER_SELECTOR = ".episode-nb-label";

	public static final String EPISODE_TITLE_SELECTOR = ".episode-name";

	public static final String EPISODE_DATE_SELECTOR = ".episode-air-date";
	
    private String title;

    private Date date;

    private String downloadLinkPart;

    public TVTimeEpisode() {
    }

    public TVTimeEpisode(String title, Date date, Integer episodeNumber) {
        this.title = title;
        this.date = date;
        this.downloadLinkPart = "E" + String.format("%2d", episodeNumber).replace(' ', '0');
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

    public String getDownloadLinkPart() {
        return downloadLinkPart;
    }
}
