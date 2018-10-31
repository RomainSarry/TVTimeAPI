package tvtimeapi.exceptions;

/**
 * Created by Romain on 28/10/2018.
 */
public class TVTimeURLException extends TVTimeAPIException {

	private static final long serialVersionUID = 1L;

    public TVTimeURLException(String urlString) {
        super("Cannot connect to TVTime with URL : " + urlString);
    }
}
