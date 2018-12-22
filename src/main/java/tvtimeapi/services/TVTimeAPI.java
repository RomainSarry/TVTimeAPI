package tvtimeapi.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Selector;
import tvtimeapi.beans.*;
import tvtimeapi.exceptions.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.*;

/**
 * Created by Romain on 01/11/2017.
 */
public class TVTimeAPI {

	private static final String UTF_8 = "UTF-8";

	private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

	private String urlString;

	private String tvstRemember;

	public TVTimeAPI(String urlString, String username, String password)
			throws TVTimeURLException, TVTimeLoginException {
		this.urlString = urlString == null ? "https://www.tvtime.com" : urlString;

		if (username == null || password == null) {
			throw new TVTimeLoginException(username);
		}

		try {
			CookieManager cookieManager = new CookieManager();
			CookieHandler.setDefault(cookieManager);
			URL url = new URL(this.urlString + "/signin");

			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);

			OutputStream os = connection.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, UTF_8));
			writer.write("username=" + URLEncoder.encode(username, UTF_8) + "&password="
					+ URLEncoder.encode(password, UTF_8));
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
			throw new TVTimeURLException(this.urlString);
		}

		if (tvstRemember == null) {
			throw new TVTimeLoginException(username);
		}
	}

	private TVTimePage getPage(String urlString) throws TVTimeURLException {
		Document htmlDom = null;

		try {
			htmlDom = Jsoup.connect(urlString).followRedirects(true).cookie("tvstRemember", tvstRemember).get();
		} catch (Exception e) {
			throw new TVTimeURLException(urlString);
		}

		return new TVTimePage(htmlDom);
	}

	public TVTimeWatchlist getWatchlist() throws TVTimeURLException, TVTimeWatchlistFetchingException {
		try {
			TVTimeWatchlist watchlist = new TVTimeWatchlist();

			for (Element el : getPage(urlString).getFields(TVTimeWatchlist.SHOWS_SELECTOR)) {
				if (el.id() != "") {
					String name = el.select(TVTimeWatchlist.SHOW_NAME_SELECTOR).first().text();
					String link = el.select(TVTimeWatchlist.SHOW_NAME_SELECTOR).first().attr("href");
					String[] linkParts = link.split("/");
					String id = linkParts[linkParts.length - 1];
					String[] nextEpisodeParts = el.select(TVTimeWatchlist.NEXT_EPISODE_SELECTOR).first().text()
							.split("E");
					Map.Entry<Integer, Integer> nextEpisode = new SimpleImmutableEntry<>(
							Integer.valueOf(nextEpisodeParts[0].substring(1)), Integer.valueOf(nextEpisodeParts[1]));

					TVTimeShow show = new TVTimeShow(id, name, nextEpisode, link);
					watchlist.add(show);
				}
			}

			return watchlist;
		} catch (Selector.SelectorParseException | NumberFormatException e) {
			throw new TVTimeWatchlistFetchingException();
		}
	}

	public TVTimeWatchlist getDetailedWatchlist()
			throws TVTimeURLException, TVTimeEpisodeFetchingException, TVTimeSeasonFetchingException,
			TVTimeWatchlistFetchingException {
		try {
			TVTimeWatchlist watchlist = new TVTimeWatchlist();

			for (Element el : getPage(urlString).getFields(TVTimeWatchlist.SHOWS_SELECTOR)) {
				if (el.id() != "") {
					watchlist.add(getUnwatchedShow(el.select(TVTimeWatchlist.SHOW_NAME_SELECTOR).first().attr("href")));
				}
			}

			return watchlist;
		} catch (Selector.SelectorParseException e) {
			throw new TVTimeWatchlistFetchingException();
		}
	}

	public TVTimeShow getShow(String link)
			throws TVTimeURLException, TVTimeEpisodeFetchingException, TVTimeSeasonFetchingException {
		TVTimePage page = getPage(urlString + link);

		String name = page.getField(TVTimeShow.SHOW_NAME_SELECTOR).text();
		String[] linkParts = link.split("/");
		String id = linkParts[linkParts.length - 1];
		Integer nextSeasonNumber = Integer
				.valueOf(page.getField(TVTimeShow.NEXT_SEASON_NUMBER_SELECTOR).attr("data-season"));
		Integer nextEpisodeNumber = Integer.valueOf(page.getField(TVTimeShow.NEXT_EPISODE_NUMBER_SELECTOR).text());
		Map.Entry<Integer, Integer> nextEpisode = new SimpleImmutableEntry<>(nextSeasonNumber, nextEpisodeNumber);
		String banner = page.getField(TVTimeShow.BANNER_SELECTOR).attr("src");

		TVTimeShow show = new TVTimeShow(page, id, name, nextEpisode, link, banner);
		show.setSeasons(getSeasons(show));

		return show;
	}

	public TVTimeShow getUnwatchedShow(String link)
			throws TVTimeURLException, TVTimeEpisodeFetchingException, TVTimeSeasonFetchingException {
		TVTimePage page = getPage(urlString + link);

		String name = page.getField(TVTimeShow.SHOW_NAME_SELECTOR).text();
		String[] linkParts = link.split("/");
		String id = linkParts[linkParts.length - 1];
		Integer nextSeasonNumber = Integer
				.valueOf(page.getField(TVTimeShow.NEXT_SEASON_NUMBER_SELECTOR).attr("data-season"));
		Integer nextEpisodeNumber = Integer.valueOf(page.getField(TVTimeShow.NEXT_EPISODE_NUMBER_SELECTOR).text());
		Map.Entry<Integer, Integer> nextEpisode = new SimpleImmutableEntry<>(nextSeasonNumber, nextEpisodeNumber);
		String banner = page.getField(TVTimeShow.BANNER_SELECTOR).attr("src");

		TVTimeShow show = new TVTimeShow(page, id, name, nextEpisode, link, banner);
		show.setSeasons(getUnwatchedSeasons(show));

		return show;
	}

	private Map<Integer, TVTimeSeason> getSeasons(TVTimePage page)
			throws TVTimeEpisodeFetchingException, TVTimeSeasonFetchingException {
		Map<Integer, TVTimeSeason> seasons = new HashMap<>();
		Integer seasonNumber = null;

		try {
			Integer numberOfSeasons = Integer
					.valueOf(page.getLastField(TVTimeShow.NUMBER_OF_SEASONS_SELECTOR).attr("data-season"));
			for (seasonNumber = 1; seasonNumber <= numberOfSeasons; seasonNumber++) {
				TVTimeSeason season = new TVTimeSeason(seasonNumber);
				Element seasonContent = page.getField("#season" + seasonNumber + "-content");
				season.setEpisodes(getEpisodes(seasonContent));
				seasons.put(seasonNumber, season);
			}

			return seasons;
		} catch (TVTimeEpisodeFetchingException e) {
			throw new TVTimeEpisodeFetchingException(seasonNumber, e.getEpisodeNumber());
		} catch (Exception e) {
			throw new TVTimeSeasonFetchingException(seasonNumber);
		}
	}

	public Map<Integer, TVTimeSeason> getSeasons(TVTimeShow show)
			throws TVTimeEpisodeFetchingException, TVTimeSeasonFetchingException, TVTimeURLException {
		try {
			return getSeasons(getPage(urlString + show.getLink()));
		} catch (TVTimeEpisodeFetchingException e) {
			throw new TVTimeEpisodeFetchingException(show, e.getSeasonNumber(), e.getEpisodeNumber());
		} catch (TVTimeSeasonFetchingException e) {
			throw new TVTimeSeasonFetchingException(show, e.getSeasonNumber());
		} catch (Exception e) {
			throw e;
		}
	}

	private Map<Integer, TVTimeSeason> getUnwatchedSeasons(TVTimeShow show, TVTimePage page)
			throws TVTimeEpisodeFetchingException, TVTimeSeasonFetchingException {
		Map<Integer, TVTimeSeason> seasons = new HashMap<>();
		Integer seasonNumber = null;

		try {
			Integer numberOfSeasons = Integer
					.valueOf(page.getLastField(TVTimeShow.NUMBER_OF_SEASONS_SELECTOR).attr("data-season"));
			for (seasonNumber = show.getNextEpisode().getKey(); seasonNumber <= numberOfSeasons; seasonNumber++) {
				TVTimeSeason season = new TVTimeSeason(seasonNumber);
				Element seasonContent = page.getField("#season" + seasonNumber + "-content");
				season.setEpisodes(getUnwatchedEpisodes(seasonContent, seasonNumber, show.getNextEpisode()));
				seasons.put(seasonNumber, season);
			}

			return seasons;
		} catch (TVTimeEpisodeFetchingException e) {
			throw new TVTimeEpisodeFetchingException(show, seasonNumber, e.getEpisodeNumber());
		} catch (Exception e) {
			throw new TVTimeSeasonFetchingException(show, seasonNumber);
		}
	}

	public Map<Integer, TVTimeSeason> getUnwatchedSeasons(TVTimeShow show)
			throws TVTimeEpisodeFetchingException, TVTimeSeasonFetchingException, TVTimeURLException {
		return getUnwatchedSeasons(show, getPage(urlString + show.getLink()));
	}

	private Map<Integer, TVTimeEpisode> getEpisodes(Element seasonContent)
			throws TVTimeSeasonFetchingException, TVTimeEpisodeFetchingException {
		Integer episodeNumber = null;
		Map<Integer, TVTimeEpisode> episodes = new HashMap<>();

		try {
			for (Element episodeContent : seasonContent.select(TVTimeSeason.EPISODES_SELECTOR)) {
				String title = episodeContent.select(TVTimeEpisode.EPISODE_TITLE_SELECTOR).text();
				Date date;
				try {
					date = formatter.parse(episodeContent.select(TVTimeEpisode.EPISODE_DATE_SELECTOR).text());
				} catch (ParseException e) {
					date = null;
				}

				episodeNumber = Integer.valueOf(episodeContent.select(TVTimeEpisode.EPISODE_NUMBER_SELECTOR).text());
				episodes.put(episodeNumber, new TVTimeEpisode(title, date, episodeNumber));
			}

			return episodes;
		} catch (Selector.SelectorParseException e) {
			throw new TVTimeSeasonFetchingException();
		} catch (Exception e) {
			throw new TVTimeEpisodeFetchingException(episodeNumber);
		}
	}

	public Map<Integer, TVTimeEpisode> getEpisodes(TVTimeShow show, Integer seasonNumber)
			throws TVTimeURLException, TVTimeEpisodeFetchingException, TVTimeSeasonFetchingException {
		try {
			TVTimePage page = getPage(urlString + show.getLink());
			Element seasonContent = page.getField("#season" + seasonNumber + "-content");

			return getEpisodes(seasonContent);
		} catch (TVTimeURLException e) {
			throw e;
		} catch (TVTimeEpisodeFetchingException e) {
			throw new TVTimeEpisodeFetchingException(show, seasonNumber, e.getEpisodeNumber());
		} catch (Exception e) {
			throw new TVTimeSeasonFetchingException(show, seasonNumber);
		}
	}

	private Map<Integer, TVTimeEpisode> getUnwatchedEpisodes(Element seasonContent, Integer seasonNumber,
			Map.Entry<Integer, Integer> nextEpisode)
			throws TVTimeSeasonFetchingException, TVTimeEpisodeFetchingException {
		Integer episodeNumber = null;
		Map<Integer, TVTimeEpisode> episodes = new HashMap<>();

		try {
			for (Element episodeContent : seasonContent.select(TVTimeSeason.EPISODES_SELECTOR)) {
				if (!episodeContent.hasClass("future")) {
					episodeNumber = Integer
							.valueOf(episodeContent.select(TVTimeEpisode.EPISODE_NUMBER_SELECTOR).text());

					if (episodeNumber >= nextEpisode.getValue() || seasonNumber > nextEpisode.getKey()) {
						String title = episodeContent.select(TVTimeEpisode.EPISODE_TITLE_SELECTOR).text();
						Date date;
						try {
							date = formatter.parse(episodeContent.select(TVTimeEpisode.EPISODE_DATE_SELECTOR).text());
						} catch (ParseException e) {
							date = null;
						}

						episodes.put(episodeNumber, new TVTimeEpisode(title, date, episodeNumber));
					}
				}
			}

			return episodes;
		} catch (Selector.SelectorParseException e) {
			throw new TVTimeSeasonFetchingException(seasonNumber);
		} catch (Exception e) {
			throw new TVTimeEpisodeFetchingException(seasonNumber, episodeNumber);
		}
	}

	public Map<Integer, TVTimeEpisode> getUnwatchedEpisodes(TVTimeShow show, Integer seasonNumber,
			Map.Entry<Integer, Integer> nextEpisode)
			throws TVTimeURLException, TVTimeEpisodeFetchingException, TVTimeSeasonFetchingException {
		try {
			TVTimePage page = getPage(urlString + show.getLink());
			Element seasonContent = page.getField("#season" + seasonNumber + "-content");

			return getUnwatchedEpisodes(seasonContent, seasonNumber, nextEpisode);
		} catch (TVTimeURLException e) {
			throw e;
		} catch (TVTimeEpisodeFetchingException e) {
			throw new TVTimeEpisodeFetchingException(show, seasonNumber, e.getEpisodeNumber());
		} catch (Exception e) {
			throw new TVTimeSeasonFetchingException(show, seasonNumber);
		}
	}
}
