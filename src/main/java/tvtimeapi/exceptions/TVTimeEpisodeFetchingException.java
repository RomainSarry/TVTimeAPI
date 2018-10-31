package tvtimeapi.exceptions;

import tvtimeapi.beans.TVTimeShow;

/**
 * Created by Romain on 28/10/2018.
 */
public class TVTimeEpisodeFetchingException extends TVTimeAPIException {

	private static final long serialVersionUID = 1L;

	private TVTimeShow show;
	
	private Integer seasonNumber;
	
	private Integer episodeNumber;
	
    public TVTimeEpisodeFetchingException(TVTimeShow show, Integer seasonNumber, Integer episodeNumber) {
        super("Error fetching episode : " + show.getName() + " season " + seasonNumber + " episode " + episodeNumber);
        this.show = show;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
    }

	public TVTimeEpisodeFetchingException(Integer seasonNumber, Integer episodeNumber) {
		this.seasonNumber = seasonNumber;
		this.episodeNumber = episodeNumber;
	}

	public TVTimeEpisodeFetchingException(Integer episodeNumber) {
		this.episodeNumber = episodeNumber;
	}

	public TVTimeShow getShow() {
		return show;
	}

	public Integer getSeasonNumber() {
		return seasonNumber;
	}
	
	public Integer getEpisodeNumber() {
		return episodeNumber;
	}
}
