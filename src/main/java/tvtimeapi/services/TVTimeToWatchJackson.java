package tvtimeapi.services;

import tvtimeapi.beans.TVTimeEpisode;
import tvtimeapi.beans.TVTimeShow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Romain on 27/12/2018.
 */
class TVTimeToWatchJackson {
    private String result;

    private List<TVTimeEpisodeJackson> episodes;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<TVTimeEpisodeJackson> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<TVTimeEpisodeJackson> episodes) {
        this.episodes = episodes;
    }

    public List<TVTimeShow> getWatchlist() {
        List<TVTimeShow> shows = new ArrayList<>();

        for (TVTimeEpisodeJackson jacksonEpisode : episodes) {
            TVTimeShow show = jacksonEpisode.getShow();

            TVTimeEpisode episode = new TVTimeEpisode();
            episode.setId(jacksonEpisode.getId());
            episode.setName(jacksonEpisode.getName());
            episode.setSeason(jacksonEpisode.getSeason());
            episode.setNumber(jacksonEpisode.getNumber());
            episode.setDate(jacksonEpisode.getDate());
            episode.setWatched(false);

            show.setNextEpisode(episode);

            shows.add(show);
        }

        return shows;
    }
}
