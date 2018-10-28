package tvtimeapi.exceptions;

import tvtimeapi.services.TVTimeAPI;

/**
 * Created by Romain on 28/10/2018.
 */
public class TVTimeURLException extends TVTimeAPIException {
    public TVTimeURLException(String urlString) {
        super("Cannot connect to TVTime with URL : " + urlString);
    }
}
