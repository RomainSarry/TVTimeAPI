package tvtimeapi.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Romain on 01/11/2017.
 */
public class TVTimeSeason {
	public static final String EPISODES_SELECTOR = "li";

	private Map<Integer, TVTimeEpisode> episodes;

	private String downloadLinkPart;

	public TVTimeSeason(Integer seasonNumber) {
//		for (Element el : seasonContent.select(FUTURE_EPISODES_SELECTOR)) {
//			Integer episodeNumber = Integer.valueOf(el.select(EPISODE_NUMBER_SELECTOR).text());
//			addEpisode(episodeNumber, buildEpisode(episodeNumber, el));
//		}

//		for (Element el : seasonContent.select(FUTURE_EPISODES_SELECTOR)) {
//			if (!el.hasClass("future")) {
//				Integer episodeNumber = Integer.valueOf(el.select(EPISODE_NUMBER_SELECTOR).text());
//				if (seasonNumber > nextEpisodeInfos.getKey() || episodeNumber >= nextEpisodeInfos.getValue()) {
//					addEpisode(episodeNumber, buildEpisode(episodeNumber, el));
//				}
//			}
//		}
		
		downloadLinkPart = "S" + String.format("%2d", seasonNumber).replace(' ', '0');
	}

	public void addEpisode(Integer episodeNumber, TVTimeEpisode episode) {
		if (episodes == null) {
			episodes = new HashMap<Integer, TVTimeEpisode>();
		}
		episodes.put(episodeNumber, episode);
	}

	public Map<Integer, TVTimeEpisode> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(Map<Integer, TVTimeEpisode> episodes) {
		this.episodes = episodes;
	}

	public Boolean hasEpisodes() {
		if (episodes == null) {
			return false;
		}
		return !episodes.isEmpty();
	}

	public String getDownloadLinkPart() {
		return downloadLinkPart;
	}
}
