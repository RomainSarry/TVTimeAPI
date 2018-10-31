package tvtimeapi.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Romain on 01/11/2017.
 */
public class TVTimeShow {
	public static final String NUMBER_OF_SEASONS_SELECTOR = "#seasons .dropdown-menu li a";

	public static final String BANNER_SELECTOR = ".banner-image img";

	public static final String SHOW_NAME_SELECTOR = "#top-banner h1";
	
	public static final String NEXT_SEASON_NUMBER_SELECTOR = "#seasons .dropdown-menu .active a";
	
	public static final String NEXT_EPISODE_NUMBER_SELECTOR = ".episode-list .current a";

	private TVTimePage page;

	private String id;

	private String name;

	private Map<Integer, TVTimeSeason> seasons;

	private Map.Entry<Integer, Integer> nextEpisode;

	private String banner;

	private String link;

	public TVTimeShow(TVTimePage page, String id, String name, Map.Entry<Integer, Integer> nextEpisode, String link, String banner) {
		this(id, name, nextEpisode, link);
		this.page = page;
		this.banner = banner;
	}

	public TVTimeShow(String id, String name, Map.Entry<Integer, Integer> nextEpisode, String link) {
		this.id = id;
		this.name = name;
		this.link = link;
		this.nextEpisode = nextEpisode;
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

	public Map.Entry<Integer, Integer> getNextEpisode() {
		return nextEpisode;
	}

	public void setNextEpisode(Map.Entry<Integer, Integer> nextEpisode) {
		this.nextEpisode = nextEpisode;
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
