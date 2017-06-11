package client.datagov;

import lombok.RequiredArgsConstructor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class OpenDataClient {

	private final RestTemplate restTemplate;
	private final String serviceKey;
	private final String host;
	
	public BasicInfoIndex getAllPolitician() {
		return _getAllPolitician(1, 30);
	}

	private BasicInfoIndex _getAllPolitician(int page, int size) {
		String url = String.format(host + "/9710000/NationalAssemblyInfoService/getMemberCurrStateList?numOfRows=%d&pageNo=%d&ServiceKey=%s", size, page, serviceKey);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		
		if ( responseEntity.getStatusCode() == HttpStatus.OK) {
			Element element = Jsoup.parse(responseEntity.getBody()).body(); 
		}
		
		return null;
	}

	static class BasicInfoIndex {
		
	}
}
