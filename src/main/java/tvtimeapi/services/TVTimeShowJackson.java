package tvtimeapi.services;

import tvtimeapi.beans.TVTimeShow;

/**
 * Created by Romain on 27/12/2018.
 */
class TVTimeShowJackson {
    private String result;

    private TVTimeShow show;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public TVTimeShow getShow() {
        return show;
    }

    public void setShow(TVTimeShow show) {
        this.show = show;
    }
}
