package client.opencongress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.nodes.Document;

import support.HangeulUtils;
import support.JsoupUtils;

public class RemoteOpenCongressClient extends OpenCongressClient {

	public RemoteOpenCongressClient(HttpClient httpClient) {
		super(httpClient);
	}

	@Override
	protected Document fetchHtml(int id) {
		HttpGet httpGet = new HttpGet(HOST + LINK_PREFIX + id);
		httpGet.addHeader("Accept-encoding", "");
		return executeToDocument(httpGet).orElseThrow(IllegalStateException::new);
	}

	@Override
	Map<String, List<Integer>> initializeIdListByName() {
		final Map<String, List<Integer>> idsByNameAll = new HashMap<>();
		int pageNumber = 1;
		final Map<String, List<Integer>> idsByName = new HashMap<>();
		do {
			idsByName.clear();
			HttpGet httpGet = new HttpGet(HOST + SEARCH_POLITICIAN_PATH
			                              + pageNumber);
			httpGet.addHeader("Accept-encoding", "");
			super.executeToDocument(httpGet)
					.map(document -> document.getElementsByAttributeValueStarting("href", LINK_PREFIX))
					.filter(es -> !es.isEmpty())
					.ifPresent(elements -> elements.stream()
							.filter(e -> JsoupUtils.firstElementByTagName(e, "h4").isPresent())
							.forEach(element -> {
								JsoupUtils.firstElementByTagName(element, "h4").ifPresent(h4Element -> {
									String link = element.attr("href");
									String h4Text = h4Element.text();
									String name = HangeulUtils.removeNonHangeul(h4Text);
									Integer num = NumberUtils.toInt(StringUtils.trim(StringUtils.substring(link, LINK_PREFIX.length())));
									List<Integer> idList = idsByName.getOrDefault(name, new ArrayList<>());
									idList.add(num);
									idsByName.put(name, idList);
								});
							})
					);
			idsByNameAll.putAll(idsByName);
			pageNumber++;
		} while(!idsByName.isEmpty());
		
		return idsByNameAll;
	}
}
