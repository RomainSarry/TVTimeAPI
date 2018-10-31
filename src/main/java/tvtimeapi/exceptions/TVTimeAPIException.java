package tvtimeapi.exceptions;

/**
 * Created by Romain on 28/10/2018.
 */
public abstract class TVTimeAPIException extends Exception {

	private static final long serialVersionUID = 1L;

	public TVTimeAPIException() {
		super();
	}
	
    public TVTimeAPIException(String message) {
        super(message);
    }
}
