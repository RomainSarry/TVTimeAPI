package tvtimeapi.beans;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

/**
 * Created by Romain on 01/11/2017.
 */
@SuppressWarnings("serial")
public class TVTimeSimplifiedWatchlist extends ArrayList<TVTimeShow> {
    protected static final String SHOWS_SELECTOR = ".to-watch-list li";

    protected static final String SHOW_NAME_SELECTOR = ".episode-details .nb-reviews-link";
    
    protected static final String POSTER_SELECTOR = "img";

    protected TVTimePage page;

    public TVTimeSimplifiedWatchlist() {
		super();
	}

	public TVTimeSimplifiedWatchlist(TVTimePage page, String tvstRemember) {
		this.page = page;
        for (Element el : page.getFields(SHOWS_SELECTOR)) {
            if (el.id() != "") {
                String name = el.select(SHOW_NAME_SELECTOR).first().text();
                String link = el.select(SHOW_NAME_SELECTOR).first().attr("href");
                String poster = el.select(POSTER_SELECTOR).first().attr("src");
                String[] linkParts = link.split("/");
                String id = linkParts[linkParts.length - 1];

                TVTimeShow show = new TVTimeShow(id, name, poster);
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
