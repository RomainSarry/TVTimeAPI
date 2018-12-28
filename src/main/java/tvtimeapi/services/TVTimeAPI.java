package tvtimeapi.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import sun.misc.BASE64Encoder;
import tvtimeapi.beans.TVTimeShow;
import tvtimeapi.exceptions.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Romain on 01/11/2017.
 */
public class TVTimeAPI {

	private static final String UTF_8 = "UTF-8";

	private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

	private String urlString;

	private String username;

	private String password;

	public TVTimeAPI(String urlString, String username, String password)
			throws TVTimeURLException, TVTimeLoginException {
		this.urlString = urlString == null ? "https://api.tvtime.com/v1" : urlString;

		if (username == null || password == null) {
			throw new TVTimeLoginException(username);
		}

		this.username = username;
		this.password = password;
	}

	public List<TVTimeShow> getWatchlist(Map<String, String> parameters) throws TVTimeWatchlistFetchingException {
		try {
			return getRequest(urlString + "/to_watch?" + getParamsAsString(parameters), TVTimeToWatchJackson.class).getWatchlist();
		} catch (Exception e) {
			throw new TVTimeWatchlistFetchingException();
		}
	}

	public List<TVTimeShow> getDetailedWatchlist(Map<String, String> parameters) throws TVTimeWatchlistFetchingException {
		try {
			List<TVTimeShow> detailedWatchlist = new ArrayList<>();

			for (TVTimeShow show : getWatchlist(parameters)) {
				detailedWatchlist.add(getShow(show.getId(), true));
			}

			return detailedWatchlist;
		} catch (Exception e) {
			throw new TVTimeWatchlistFetchingException();
		}
	}

	public TVTimeShow getShow(Integer id, Boolean includeEpisodes) throws TVTimeShowFetchingException {
		try {
			Map<String, String> parameters = new HashMap<>();
			parameters.put("show_id", String.valueOf(id));
			return getShow(parameters, includeEpisodes);
		} catch (Exception e) {
			throw new TVTimeShowFetchingException(id);
		}
	}

	public TVTimeShow getShow(String name, Boolean includeEpisodes) throws TVTimeShowFetchingException {
		try {
			Map<String, String> parameters = new HashMap<>();
			parameters.put("show_name", name);
			return getShow(parameters, includeEpisodes);
		} catch (Exception e) {
			throw new TVTimeShowFetchingException(name);
		}
	}

	private TVTimeShow getShow(Map<String, String> parameters, Boolean includeEpisodes) throws TVTimeGetException, TVTimeParametersException {
		parameters.put("include_episodes", String.valueOf(includeEpisodes ? 1 : 0));
		TVTimeShow show = getRequest(urlString + "/show?" + getParamsAsString(parameters), TVTimeShowJackson.class).getShow();
		show.setEpisodes(show.getEpisodes().stream().filter(e -> !e.getWatched()).collect(Collectors.toList()));
		return show;
	}

	private String getParamsAsString(Map<String, String> parameters) throws TVTimeParametersException {
		try {
			List<NameValuePair> paramsAsNameValuePairs = new LinkedList<NameValuePair>();
			if (parameters != null && !parameters.isEmpty()) {
				for (Map.Entry<String, String> entry : parameters.entrySet()) {
					paramsAsNameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
			}

			return URLEncodedUtils.format(paramsAsNameValuePairs, UTF_8);
		} catch (Exception e) {
			throw new TVTimeParametersException(parameters);
		}
	}

	private <T extends Object> T getRequest(String url, Class<T> clazz) throws TVTimeGetException {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestProperty("Authorization", "Basic " + new BASE64Encoder().encode(new String(username + ":" + password).getBytes()));

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return mapper.readValue(connection.getInputStream(), clazz);
		} catch (Exception e) {
			throw new TVTimeGetException(url);
		}
	}
}
