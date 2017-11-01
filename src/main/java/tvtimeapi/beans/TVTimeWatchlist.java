package tvtimeapi.beans;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

/**
 * Created by Romain on 01/11/2017.
 */
public class TVTimeWatchlist extends ArrayList<TVTimeShow> {
    private static final String SHOWS_SELECTOR = ".to-watch-list li";

    private static final String SHOW_NAME_SELECTOR = "a";

    private static final String EPISODE_WRAPPER_SELECTOR = ".episode-details h2";

    private static final String EPISODE_SELECTOR = "a";

    private static final String REMAINING_EPISODES_SELECTOR = "span";

    private TVTimePage page;

    public TVTimeWatchlist(TVTimePage page) {
        for (Element el : page.getFields(SHOWS_SELECTOR)) {
            if (el.id() != null) {
                String name = el.select(SHOW_NAME_SELECTOR).first().text();
                String link = el.select(SHOW_NAME_SELECTOR).first().attr("href");
                String[] linkParts = link.split("/");
                String id = linkParts[linkParts.length - 1];
                Element episodeWrapper = el.select(EPISODE_WRAPPER_SELECTOR).first();
                String episode = episodeWrapper.select(EPISODE_SELECTOR).first().text();
                Boolean remainingEpisodes = !episodeWrapper.select(REMAINING_EPISODES_SELECTOR).isEmpty();

                TVTimeShow show = new TVTimeShow(id, name, episode, remainingEpisodes);
                add(show);
            }
        }
    }

    public TVTimePage getPage() {
        return page;
    }

    public void setPage(TVTimePage page) {
        this.page = page;
    }
}
