package tvtimeapi.exceptions;

import java.util.Map;

/**
 * Created by Romain on 02/11/2018.
 */
public class TVTimeParametersException extends TVTimeAPIException {

    private static final long serialVersionUID = 1L;

    public TVTimeParametersException(Map<String, String> parameters) {
        super("Error parsing parameters : " + parameters.toString());
    }
}
