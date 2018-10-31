package tvtimeapi.exceptions;

import tvtimeapi.beans.TVTimeShow;

/**
 * Created by Romain on 28/10/2018.
 */
public class TVTimeSeasonFetchingException extends TVTimeAPIException {

	private static final long serialVersionUID = 1L;

	private TVTimeShow show;
	
	private Integer seasonNumber;

	public TVTimeSeasonFetchingException(TVTimeShow show, Integer seasonNumber) {
		super("Error fetching season : " + show.getName() + " season " + seasonNumber);
		this.show = show;
		this.seasonNumber = seasonNumber;
	}

	public TVTimeSeasonFetchingException(Integer seasonNumber) {
		super();
		this.seasonNumber = seasonNumber;
	}

	public TVTimeSeasonFetchingException() {
		
	}

	public TVTimeShow getShow() {
		return show;
	}

	public Integer getSeasonNumber() {
		return seasonNumber;
	}
}
