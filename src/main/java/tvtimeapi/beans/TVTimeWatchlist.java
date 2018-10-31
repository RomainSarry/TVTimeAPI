package tvtimeapi.beans;

import java.util.ArrayList;

/**
 * Created by Romain on 01/11/2017.
 */
@SuppressWarnings("serial")
public class TVTimeWatchlist extends ArrayList<TVTimeShow> {
	public static final String SHOWS_SELECTOR = ".to-watch-list li";

	public static final String SHOW_NAME_SELECTOR = ".episode-details .nb-reviews-link";

	public static final String NEXT_EPISODE_SELECTOR = ".episode-details h2 a";
}
