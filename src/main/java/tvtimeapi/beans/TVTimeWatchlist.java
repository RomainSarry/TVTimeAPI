package tvtimeapi.beans;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

/**
 * Created by Romain on 01/11/2017.
 */
@SuppressWarnings("serial")
public class TVTimeWatchlist extends TVTimeSimplifiedWatchlist {
    private static final String EPISODE_WRAPPER_SELECTOR = ".episode-details h2";

    private static final String EPISODE_SELECTOR = "a";

    private static final String REMAINING_EPISODES_SELECTOR = "span";

    public TVTimeWatchlist(TVTimePage page, String tvstRemember) {
    	super();
        for (Element el : page.getFields(SHOWS_SELECTOR)) {
            if (el.id() != "") {
                String name = el.select(SHOW_NAME_SELECTOR).first().text();
                String link = el.select(SHOW_NAME_SELECTOR).first().attr("href");
                String[] linkParts = link.split("/");
                String id = linkParts[linkParts.length - 1];
                Element episodeWrapper = el.select(EPISODE_WRAPPER_SELECTOR).first();
                String episode = episodeWrapper.select(EPISODE_SELECTOR).first().text();
                Boolean remainingEpisodes = !episodeWrapper.select(REMAINING_EPISODES_SELECTOR).isEmpty();

                TVTimeShow show = new TVTimeShow(id, name, episode, remainingEpisodes, tvstRemember);
                add(show);
            }
        }
    }
}
