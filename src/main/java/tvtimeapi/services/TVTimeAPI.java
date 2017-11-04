package tvtimeapi.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import tvtimeapi.beans.TVTimePage;
import tvtimeapi.beans.TVTimeShow;
import tvtimeapi.beans.TVTimeWatchlist;

/**
 * Created by Romain on 01/11/2017.
 */
public class TVTimeAPI {
    public static final String URL_BASE = "https://www.tvtime.com/";

    public static TVTimePage getPageByUrl(String url, String tvstRemember) {
        Document htmlDom = null;

        try {
            System.out.println("Fetching URL : " + url);
            htmlDom = Jsoup.connect(url).cookie("tvstRemember", tvstRemember).get();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return new TVTimePage(htmlDom);
    }

    public TVTimeWatchlist getWatchlist(String tvstRemember) {
        return new TVTimeWatchlist(getPageByUrl(URL_BASE, tvstRemember));
    }

    public TVTimeShow getShow(String id, String name, String episode, Boolean remainingEpisodes, String poster, String link, String tvstRemember) {
        return new TVTimeShow(id, name, episode, remainingEpisodes, poster, link, tvstRemember);
    }
}
