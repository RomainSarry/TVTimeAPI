package tvtimeapi.beans;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

/**
 * Created by Romain on 01/11/2017.
 */
@SuppressWarnings("serial")
public class TVTimeWatchlist extends ArrayList<TVTimeShow> {
	protected static final String SHOWS_SELECTOR = ".to-watch-list li";

	protected static final String SHOW_NAME_SELECTOR = ".episode-details .nb-reviews-link";

	protected static final String POSTER_SELECTOR = "img";

	protected static final String EPISODE_WRAPPER_SELECTOR = ".episode-details h2";

	protected static final String EPISODE_SELECTOR = "a";

	protected static final String REMAINING_EPISODES_SELECTOR = "span";

	public TVTimeWatchlist() {
	}

	public TVTimeWatchlist(TVTimePage page) {
		super();
		for (Element el : page.getFields(SHOWS_SELECTOR)) {
			if (el.id() != "") {
				String name = el.select(SHOW_NAME_SELECTOR).first().text();
				String link = el.select(SHOW_NAME_SELECTOR).first().attr("href");
				String[] linkParts = link.split("/");
				String id = linkParts[linkParts.length - 1];
				Element episodeWrapper = el.select(EPISODE_WRAPPER_SELECTOR).first();
				String episode = episodeWrapper.select(EPISODE_SELECTOR).first().text();
				Integer remainingEpisodes = Integer
						.valueOf(episodeWrapper.select(REMAINING_EPISODES_SELECTOR).first().text());
				String poster = el.select(POSTER_SELECTOR).first().attr("src");

				TVTimeShow show = new TVTimeShow(id, name, episode, remainingEpisodes, poster, link);
				add(show);
			}
		}
	}
}
