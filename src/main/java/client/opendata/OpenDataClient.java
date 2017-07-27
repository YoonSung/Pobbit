package client.opendata;

import static support.JsoupUtils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import org.apache.http.client.HttpClient;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import client.CommonHttpClient;

public abstract class OpenDataClient extends CommonHttpClient {

	public OpenDataClient(HttpClient httpClient) {
		super(httpClient);
	}

	public List<BasicInfoIndex> getAllPolitician() {
		return getAllPolitician(1, 299);
	}

	protected abstract List<BasicInfoIndex> getAllPolitician(int page, int size);

	protected List<BasicInfoIndex> toBasicInfoIndexList(Document document) {
		return firstElementByTagName(document, "body")
				.flatMap(e -> firstElementByTagName(e, "items"))
				.map(e -> e.getElementsByTag("item"))
				.filter(e -> !e.isEmpty())
				.map(es -> es.stream().map(BasicInfoIndex::new).collect(Collectors.toList()))
				.orElse(new ArrayList<>());
	}

	@Getter
	@AllArgsConstructor
	@ToString
	public static class BasicInfoIndex {
		final String code;
		final String name;
		final String imageUrl;
		final String number;

		public BasicInfoIndex(Element item) {
			code = item.getElementsByTag("deptCd").get(0).text();
			name = item.getElementsByTag("empNm").get(0).text();
			imageUrl = item.getElementsByTag("jpgLink").get(0).text();
			number = item.getElementsByTag("num").get(0).text();
		}
	}
}
