package tvtimeapi.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import tvtimeapi.beans.TVTimeShow;

import java.util.Date;

/**
 * Created by Romain on 27/12/2018.
 */
class TVTimeEpisodeJackson {
    private String result;

    private Integer id;

    private String name;

    @JsonProperty("air_date")
    private Date date;

    @JsonProperty("season_number")
    private Integer season;

    private Integer number;

    private TVTimeShow show;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public TVTimeShow getShow() {
        return show;
    }

    public void setShow(TVTimeShow show) {
        this.show = show;
    }
}
