package tvtimeapi.beans;

import org.jsoup.nodes.Element;
import tvtimeapi.services.TVTimeAPI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Romain on 01/11/2017.
 */
public class TVTimeShow {
    private static final String URL_SHOW = TVTimeAPI.URL_BASE + "show/";

    private static final String NUMBER_OF_SEASONS_SELECTOR = "#seasons .dropdown-menu li a";

    private static final String FUTURE_EPISODES_SELECTOR = "li.future, li.current";

    private static final String EPISODE_NUMBER_SELECTOR = ".episode-nb-label";

    private static final String EPISODE_TITLE_SELECTOR = ".episode-name";

    private static final String EPISODE_DATE_SELECTOR = ".episode-air-date";

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private TVTimePage page;

    private String id;

    private String name;

    private Map<Integer, TVTimeSeason> seasons;

    public TVTimeShow(String id, String name, String episode, Boolean remainingEpisodes) {
        this.id = id;
        this.name = name;
        Map.Entry<Integer, Integer> nextEpisodeInfos = getNextEpisodeInfos(episode);
        if (!remainingEpisodes) {
            buildSingleEpisodeSeason(nextEpisodeInfos);
        } else {
            buildSeasons(id, nextEpisodeInfos);
        }
    }

    private Map.Entry<Integer, Integer> getNextEpisodeInfos(String episodeString) {
        String[] episodeParts = episodeString.split("E");

        Integer season = Integer.valueOf(episodeParts[0].substring(1));
        Integer episode = Integer.valueOf(episodeParts[1]);

        return new AbstractMap.SimpleImmutableEntry<Integer, Integer>(season, episode);
    }

    private void buildSingleEpisodeSeason(Map.Entry<Integer, Integer> nextEpisodeInfos) {
        TVTimeSeason season = new TVTimeSeason();
        season.put(nextEpisodeInfos.getValue(), new TVTimeEpisode());
        addSeason(nextEpisodeInfos.getKey(), season);
    }

    private void buildSeasons(String id, Map.Entry<Integer, Integer> nextEpisodeInfos) {
        page = TVTimeAPI.getPageByUrl(URL_SHOW + id);
        Integer numberOfSeasons = Integer.valueOf(page.getLastField(NUMBER_OF_SEASONS_SELECTOR).attr("data-season"));
        for (int s = nextEpisodeInfos.getKey(); s <= numberOfSeasons; s++) {
            buildSeason(s);
        }
    }

    private void buildSeason(Integer seasonNumber) {
        Element seasonContent = page.getField("season" + seasonNumber + "-content");
        TVTimeSeason season = new TVTimeSeason();

        for (Element el : seasonContent.select(FUTURE_EPISODES_SELECTOR)) {
            Integer number = Integer.valueOf(el.select(EPISODE_NUMBER_SELECTOR).text());
            season.put(number, buildEpisode(el));
        }
    }

    private TVTimeEpisode buildEpisode(Element el) {
        String title = el.select(EPISODE_TITLE_SELECTOR).text();
        Date date;
        try {
            date = formatter.parse(el.select(EPISODE_DATE_SELECTOR).text());
        } catch (ParseException e) {
            date = null;
        }
        return new TVTimeEpisode(title, date);
    }

    public TVTimePage getPage() {
        return page;
    }

    public void setPage(TVTimePage page) {
        this.page = page;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addSeason(Integer number, TVTimeSeason season) {
        if (this.seasons == null) {
            this.seasons = new HashMap<Integer, TVTimeSeason>();
        }
        this.seasons.put(number, season);
    }

    public Map<Integer, TVTimeSeason> getSeasons() {
        return seasons;
    }

    public void setSeasons(Map<Integer, TVTimeSeason> seasons) {
        this.seasons = seasons;
    }
}
