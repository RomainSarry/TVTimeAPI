package tvtimeapi.exceptions;

/**
 * Created by Romain on 28/10/2018.
 */
public class TVTimeWatchlistFetchingException extends TVTimeAPIException {
    public TVTimeWatchlistFetchingException() {
        super("Error reading watchlist");
    }
}
