package client.opencongress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.nodes.Document;

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
									Matcher matcher = KOREAN_PATTERN.matcher(h4Text);
									if (StringUtils.isNoneBlank(link) && StringUtils.startsWith(link, LINK_PREFIX) && matcher.find()) {
										String number = StringUtils.trim(StringUtils.substring(link, LINK_PREFIX.length()));
										if (NumberUtils.isNumber(number)) {
											String name = matcher.group();
											Integer num = NumberUtils.toInt(number);
											List<Integer> idList = idsByName.getOrDefault(name, new ArrayList<>());
											idList.add(num);
											idsByName.put(name, idList);
										}
									}
								});
							})
					);
			idsByNameAll.putAll(idsByName);
			pageNumber++;
		} while(!idsByName.isEmpty());
		
		return idsByNameAll;
	}
}
