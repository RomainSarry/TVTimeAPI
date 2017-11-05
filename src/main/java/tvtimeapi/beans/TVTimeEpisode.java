package tvtimeapi.beans;

import java.util.Date;

/**
 * Created by Romain on 01/11/2017.
 */
public class TVTimeEpisode {
    private String title;

    private Date date;

    private String downloadLinkPart;

    public TVTimeEpisode() {
    }

    public TVTimeEpisode(String title, Date date, Integer episodeNumber) {
        this.title = title;
        this.date = date;
        this.downloadLinkPart = generateDownloadLinkPart(episodeNumber);
    }

    private String generateDownloadLinkPart(Integer episodeNumber) {
        return "E" + String.format("%2d", episodeNumber).replace(' ', '0');
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

    public void setDownloadLinkPart(String downloadLinkPart) {
        this.downloadLinkPart = downloadLinkPart;
    }
}
