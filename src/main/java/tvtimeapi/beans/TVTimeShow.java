package tvtimeapi.beans;

import org.jsoup.nodes.Element;
import tvtimeapi.services.TVTimeAPI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.AbstractMap.SimpleImmutableEntry;

/**
 * Created by Romain on 01/11/2017.
 */
public class TVTimeShow {
    private static final String NUMBER_OF_SEASONS_SELECTOR = "#seasons .dropdown-menu li a";

    private static final String FUTURE_EPISODES_SELECTOR = "li";

    private static final String BANNER_SELECTOR = ".banner-image img";

    private static final String EPISODE_NUMBER_SELECTOR = ".episode-nb-label";

    private static final String EPISODE_TITLE_SELECTOR = ".episode-name";

    private static final String EPISODE_DATE_SELECTOR = ".episode-air-date";

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private TVTimePage page;

    private String id;

    private String name;

    private Map<Integer, TVTimeSeason> seasons;

    private String episode;
    
    private Map.Entry<Integer, Integer> nextEpisodeInfos;

    private Boolean remainingEpisodes;
    
    private String poster;

    private String banner;

    private String link;

	public TVTimeShow(String id, String name, String episode, Boolean remainingEpisodes, String poster, String link, String tvstRemember) {
        this(id, name, episode, remainingEpisodes, poster, link);
        buildSeasons(tvstRemember);
    }

    public TVTimeShow(String id, String name, String episode, Boolean remainingEpisodes, String poster, String link) {
        this.id = id;
        this.name = name;
        this.episode = episode;
        this.remainingEpisodes = remainingEpisodes;
        this.poster = poster;
        this.link = link;
        setNextEpisodeInfos(episode);
    }

    private void buildSeasons(String tvstRemember) {
        page = TVTimeAPI.getPageByUrl(TVTimeAPI.URL_BASE + link, tvstRemember);
        banner = page.getField(BANNER_SELECTOR).attr("src");
        Integer numberOfSeasons = Integer.valueOf(page.getLastField(NUMBER_OF_SEASONS_SELECTOR).attr("data-season"));
        for (int s = nextEpisodeInfos.getKey(); s <= numberOfSeasons; s++) {
            buildSeason(s);
        }
    }

    private void buildSeason(Integer seasonNumber) {
        Element seasonContent = page.getField("#season" + seasonNumber + "-content");
        TVTimeSeason season = new TVTimeSeason(seasonNumber);

        for (Element el : seasonContent.select(FUTURE_EPISODES_SELECTOR)) {
            if (!el.hasClass("future")) {
                Integer episodeNumber = Integer.valueOf(el.select(EPISODE_NUMBER_SELECTOR).text());
                if (seasonNumber > getNextEpisodeInfos().getKey() || episodeNumber >= getNextEpisodeInfos().getValue()) {
                    season.addEpisode(episodeNumber, buildEpisode(episodeNumber, el));
                }
            }
        }
        addSeason(seasonNumber, season);
    }

    private TVTimeEpisode buildEpisode(Integer episodeNumber, Element el) {
        String title = el.select(EPISODE_TITLE_SELECTOR).text();
        Date date;
        try {
            date = formatter.parse(el.select(EPISODE_DATE_SELECTOR).text());
        } catch (ParseException e) {
            date = null;
        }
        return new TVTimeEpisode(title, date, episodeNumber);
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

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public Map.Entry<Integer, Integer> getNextEpisodeInfos() {
		return nextEpisodeInfos;
	}

	public void setNextEpisodeInfos(Map.Entry<Integer, Integer> nextEpisodeInfos) {
		this.nextEpisodeInfos = nextEpisodeInfos;
	}

	private void setNextEpisodeInfos(String episodeString) {
        String[] episodeParts = episodeString.split("E");

        Integer season = Integer.valueOf(episodeParts[0].substring(1));
        Integer episode = Integer.valueOf(episodeParts[1]);

        this.nextEpisodeInfos = new SimpleImmutableEntry<Integer, Integer>(season, episode);
    }

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

    public Boolean getRemainingEpisodes() {
        return remainingEpisodes;
    }

    public void setRemainingEpisodes(Boolean remainingEpisodes) {
        this.remainingEpisodes = remainingEpisodes;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
