package tvtimeapi.exceptions;

/**
 * Created by Romain on 28/10/2018.
 */
public class TVTimeShowFetchingException extends TVTimeAPIException {
	public TVTimeShowFetchingException(Integer id) {
		super("Error fetching show with id : " + id);
	}
	public TVTimeShowFetchingException(String name) {
		super("Error fetching show : " + name);
	}
}
