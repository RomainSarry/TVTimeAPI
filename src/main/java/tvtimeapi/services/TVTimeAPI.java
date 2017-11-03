package tvtimeapi.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import tvtimeapi.beans.TVTimePage;
import tvtimeapi.beans.TVTimeSimplifiedWatchlist;
import tvtimeapi.beans.TVTimeWatchlist;

/**
 * Created by Romain on 01/11/2017.
 */
public class TVTimeAPI {
    public static final String URL_BASE = "https://www.tvtime.com/en/";

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
        return new TVTimeWatchlist(getPageByUrl(URL_BASE, tvstRemember), tvstRemember);
    }

    public TVTimeSimplifiedWatchlist getSimplifiedWatchlist(String tvstRemember) {
        return new TVTimeSimplifiedWatchlist(getPageByUrl(URL_BASE, tvstRemember), tvstRemember);
    }
}
