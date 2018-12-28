package tvtimeapi.exceptions;

import java.util.Map;

/**
 * Created by Romain on 03/11/2018.
 */
public class TVTimeGetException extends TVTimeAPIException {

    private static final long serialVersionUID = 1L;

    public TVTimeGetException(String url) {
        super("Error getting from url : " + url);
    }

    public TVTimeGetException(String url, Map<String, String> parameters) {
        super("Error getting from url : " + url + " with parameters : " + parameters.toString());
    }
}
