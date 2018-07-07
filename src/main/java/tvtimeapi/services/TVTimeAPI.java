package tvtimeapi.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import tvtimeapi.beans.TVTimePage;
import tvtimeapi.beans.TVTimeShow;
import tvtimeapi.beans.TVTimeWatchlist;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Romain on 01/11/2017.
 */
public class TVTimeAPI {
    private static final Logger LOGGER = Logger.getLogger(TVTimeAPI.class.getName());

    public static final String URL_BASE = "https://www.tvtime.com/";

    public static TVTimePage getPageByUrl(String url, String tvstRemember) {
        Document htmlDom = null;

        try {
            htmlDom = Jsoup.connect(url).followRedirects(true).cookie("tvstRemember", tvstRemember).get();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return new TVTimePage(htmlDom);
    }

    public TVTimeWatchlist getWatchlist(String tvstRemember) {
        LOGGER.log(Level.INFO, "Fetching TVTime watchlist : " + URL_BASE);
        return new TVTimeWatchlist(getPageByUrl(URL_BASE, tvstRemember));
    }

    public TVTimeShow getShow(String id, String name, String episode, Integer remainingEpisodes, String poster, String link, String tvstRemember) {
        LOGGER.log(Level.INFO, "Fetching TVShow \"" + name + "\" : " + URL_BASE + link);
        return new TVTimeShow(id, name, episode, remainingEpisodes, poster, link, tvstRemember);
    }
}
