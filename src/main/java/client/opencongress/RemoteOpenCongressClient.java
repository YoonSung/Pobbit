package client.opencongress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import support.JsoupUtils;

public class RemoteOpenCongressClient extends OpenCongressClient {
	private static final String LINK_PREFIX = "/?mid=Member&member_seq=";
	private static final Pattern KOREAN_PATTERN = Pattern.compile("[\\x{ac00}-\\x{d7af}]+");

	public RemoteOpenCongressClient(HttpClient httpClient) {
		super(httpClient);
	}

	@Override
	Map<String, List<Integer>> initializeIdListByName() {
		final Map<String, List<Integer>> idsByNameAll = new HashMap<>();
		int pageNumber = 1;
		final Map<String, List<Integer>> idsByName = new HashMap<>();
		do {
			idsByName.clear();
			HttpGet httpGet = new HttpGet("http://watch.peoplepower21.org/?mid=AssemblyMembers&mode=search&party=&region=&sangim=&gender=&elect_num=&page="
			                              + pageNumber);
			httpGet.addHeader("Accept-encoding", "");
			super.executeToDocument(httpGet)
					.map(document -> document.getElementsByAttributeValueStarting("href", "/?mid=Member&member_seq="))
					.filter(es -> !es.isEmpty())
					.ifPresent(elements -> elements.stream()
							.filter(e -> JsoupUtils.getFirstElement(e, "h4").isPresent())
							.forEach(element -> {
								JsoupUtils.getFirstElement(element, "h4").ifPresent(h4Element -> {
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
