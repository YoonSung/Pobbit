package client.datagov;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenDataClient {

	private final HttpClient httpClient;
	private final String serviceKey;
	private final String host;
	
	//TODO 
	public List<BasicInfoIndex> getAllPolitician() {
		 List<BasicInfoIndex> indexList = _getAllPolitician(1, 299);
//		Map<String, BasicInfoIndex> index = _getAllPolitician.stream()
//				.collect(Collectors.toMap(BasicInfoIndex::getName, Function.identity()));
		// test debugging
		//indexList.forEach(basicInfoIndex -> System.out.println(basicInfoIndex.toString()));
		return null;
	}

	private List<BasicInfoIndex> _getAllPolitician(int page, int size) {

		try {
			StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/9710000/NationalAssemblyInfoService/getMemberCurrStateList"); /*URL*/
			urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey); /*Service Key*/
			urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("" + size, "UTF-8")); /*파라미터설명*/
			urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("" + page, "UTF-8")); /*파라미터설명*/

			HttpGet httpGet = new HttpGet(urlBuilder.toString());
			httpGet.addHeader("content-type", "application/json");

			return execute(httpGet)
					//.filter(element -> element.getElementsByTag("resultCode").get(0).is("00"))
					.map(element -> element.getElementsByTag("body").get(0))
					.filter(Element::hasText)
					.map(element -> element.getElementsByTag("items").get(0))
					.filter(Element::hasText)
					.map(items -> items.getElementsByTag("item").stream().map(BasicInfoIndex::new).collect(Collectors.toList()))
					.orElse(new ArrayList<>());
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	private Optional<Element> execute(HttpRequestBase httpRequestBase) {
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

	@Getter
	@AllArgsConstructor
	@ToString
	static class BasicInfoIndex {
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
