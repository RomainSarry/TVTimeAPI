package tvtimeapi.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Romain on 01/11/2017.
 */
public class TVTimeSeason {
    private Map<Integer, TVTimeEpisode> episodes;

    private String downloadLinkPart;

    public TVTimeSeason(Integer seasonNumber) {
        this.downloadLinkPart = generateLinkPart(seasonNumber);
    }

    public String generateLinkPart(Integer seasonNumber) {
        return "S" + String.format("%2d", seasonNumber).replace(' ', '0');
    }

    public void addEpisode(Integer episodeNumber, TVTimeEpisode episode) {
        if (episodes == null) {
            episodes = new HashMap<Integer, TVTimeEpisode>();
        }
        episodes.put(episodeNumber, episode);
    }

    public Map<Integer, TVTimeEpisode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(Map<Integer, TVTimeEpisode> episodes) {
        this.episodes = episodes;
    }
    
    public Boolean hasEpisodes() {
    	if (episodes == null) {
    		return false;
    	}
    	return !episodes.isEmpty();
    }

    public String getDownloadLinkPart() {
        return downloadLinkPart;
    }

    public void setDownloadLinkPart(String downloadLinkPart) {
        this.downloadLinkPart = downloadLinkPart;
    }
}
