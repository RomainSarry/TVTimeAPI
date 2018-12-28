package tvtimeapi.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Created by Romain on 01/11/2017.
 */
public class TVTimeShow {
	private Integer id;

	private String name;

	private String overview;

	private List<TVTimeEpisode> episodes;

	private TVTimeEpisode nextEpisode;

	private String banner;

	private String poster;

	@JsonProperty("aired_episodes")
	private Integer airedEpisodes;

	@JsonProperty("seen_episodes")
	private Integer seenEpisodes;

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

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public List<TVTimeEpisode> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(List<TVTimeEpisode> episodes) {
		this.episodes = episodes;
	}

	public TVTimeEpisode getNextEpisode() {
		return nextEpisode;
	}

	public void setNextEpisode(TVTimeEpisode nextEpisode) {
		this.nextEpisode = nextEpisode;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	@JsonProperty("images")
	private void setImages(Map<String, Object> images) {
		this.banner = ((List<String>) images.get("banner")).get(0);
		this.poster = ((Map<String, String>) images.get("poster")).get("0");
	}

	@JsonProperty("all_images")
	private void setAllImages(Map<String, Object> images) {
		setImages(images);
	}

	public Integer getAiredEpisodes() {
		return airedEpisodes;
	}

	public void setAiredEpisodes(Integer airedEpisodes) {
		this.airedEpisodes = airedEpisodes;
	}

	public Integer getSeenEpisodes() {
		return seenEpisodes;
	}

	public void setSeenEpisodes(Integer seenEpisodes) {
		this.seenEpisodes = seenEpisodes;
	}
}
