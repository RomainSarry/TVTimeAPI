package tvtimeapi.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;
import org.omg.CORBA.NameValuePair;
import tvtimeapi.beans.TVTimePage;
import tvtimeapi.beans.TVTimeShow;
import tvtimeapi.beans.TVTimeWatchlist;
import tvtimeapi.exceptions.TVTimeLoginException;
import tvtimeapi.exceptions.TVTimeURLException;
import tvtimeapi.exceptions.TVTimeWatchlistFetchingException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Romain on 01/11/2017.
 */
public class TVTimeAPI {

    private String urlString;

    private String tvstRemember;

    public TVTimeAPI(String urlString, String username, String password) throws TVTimeURLException, TVTimeLoginException {
        this.urlString = urlString == null ? "https://www.tvtime.com" : urlString;

        try {
            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);
            URL url = new URL(urlString + "/signin");

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write("username=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8"));
            writer.flush();
            writer.close();
            os.close();

            connection.getContent();

            List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
            for (HttpCookie cookie : cookies) {
                if (cookie.getName().equals("tvstRemember")) {
                    tvstRemember = cookie.getValue();
                }
            }
        } catch (IOException e) {
            throw new TVTimeURLException(urlString);
        }

        if (tvstRemember == null) {
            throw new TVTimeLoginException(username);
        }
    }

    private TVTimePage getPage(String url) {
        Document htmlDom = null;

        try {
            htmlDom = Jsoup.connect(url).followRedirects(true).cookie("tvstRemember", tvstRemember).get();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return new TVTimePage(htmlDom);
    }

    public TVTimeWatchlist getWatchlist() throws TVTimeWatchlistFetchingException {
        try {
            TVTimeWatchlist watchlist = new TVTimeWatchlist();

            for (Element el : getPage(urlString).getFields(TVTimeWatchlist.SHOWS_SELECTOR)) {
                if (el.id() != "") {
                    String name = el.select(TVTimeWatchlist.SHOW_NAME_SELECTOR).first().text();
                    String link = el.select(TVTimeWatchlist.SHOW_NAME_SELECTOR).first().attr("href");
                    String[] linkParts = link.split("/");
                    String id = linkParts[linkParts.length - 1];
                    Element episodeWrapper = el.select(TVTimeWatchlist.EPISODE_WRAPPER_SELECTOR).first();
                    String episode = episodeWrapper.select(TVTimeWatchlist.EPISODE_SELECTOR).first().text();
                    String poster = el.select(TVTimeWatchlist.POSTER_SELECTOR).first().attr("src");

                    Elements remainingEpisodesElements = episodeWrapper.select(TVTimeWatchlist.REMAINING_EPISODES_SELECTOR);
                    Integer remainingEpisodes;
                    if (remainingEpisodesElements == null || remainingEpisodesElements.isEmpty()) {
                        remainingEpisodes = 1;
                    } else {
                        String remainingEpisodesString = remainingEpisodesElements.first().text();
                        remainingEpisodes =  Integer.valueOf(remainingEpisodesString.split(" ")[1]) + 1;
                    }

                    TVTimeShow show = new TVTimeShow(id, name, episode, remainingEpisodes, poster, link);
                    watchlist.add(show);
                }
            }

            return watchlist;
        } catch (Selector.SelectorParseException e) {
            throw new TVTimeWatchlistFetchingException();
        }
    }

    public TVTimeShow getShow(String id, String name, String episode, Integer remainingEpisodes, String poster, String link) {
        return new TVTimeShow(id, name, episode, remainingEpisodes, poster, link, tvstRemember);
    }
}
