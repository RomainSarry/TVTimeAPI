package tvtimeapi.exceptions;

/**
 * Created by Romain on 28/10/2018.
 */
public abstract class TVTimeAPIException extends Exception {
    public TVTimeAPIException(String message) {
        super(message);
    }
}
