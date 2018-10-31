package tvtimeapi.exceptions;

/**
 * Created by Romain on 28/10/2018.
 */
public class TVTimeWatchlistFetchingException extends TVTimeAPIException {

	private static final long serialVersionUID = 1L;

    public TVTimeWatchlistFetchingException() {
        super("Error reading watchlist");
    }
}
