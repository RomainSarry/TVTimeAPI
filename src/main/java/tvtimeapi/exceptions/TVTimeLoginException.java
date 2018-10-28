package tvtimeapi.exceptions;

/**
 * Created by Romain on 28/10/2018.
 */
public class TVTimeLoginException extends TVTimeAPIException {
    public TVTimeLoginException(String username) {
        super("Cannot connect to TVTime with user : " + username);
    }
}
