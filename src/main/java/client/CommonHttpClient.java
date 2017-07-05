package client;

import java.io.IOException;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@Slf4j
@RequiredArgsConstructor
public class CommonHttpClient {
	
	private final HttpClient httpClient;
	
	protected Optional<Document> executeToDocument(HttpRequestBase httpRequestBase) {
		try {
			HttpResponse response = httpClient.execute(httpRequestBase);
			if (response.getStatusLine().getStatusCode() == 200) {
				String body = EntityUtils.toString(response.getEntity(), "UTF-8");
				log.debug(body);
				return Optional.of(Jsoup.parse(body));
			}
		} catch (IOException e) {
			log.error("cannot get open data", e);
			return Optional.empty();
		}
		return Optional.empty();
	}
}
