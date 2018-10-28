package tvtimeapi.beans;

import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Romain on 01/11/2017.
 */
@SuppressWarnings("serial")
public class TVTimeWatchlist extends ArrayList<TVTimeShow> {
	public static final String SHOWS_SELECTOR = ".to-watch-list li";

	public static final String SHOW_NAME_SELECTOR = ".episode-details .nb-reviews-link";

	public static final String POSTER_SELECTOR = "img";

	public static final String EPISODE_WRAPPER_SELECTOR = ".episode-details h2";

	public static final String EPISODE_SELECTOR = "a";

	public static final String REMAINING_EPISODES_SELECTOR = "span";
}
