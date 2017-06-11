package client.datagov;

import java.util.HashMap;
import java.util.Map;

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
		Map<String, String> urlVariables = new HashMap<>();
		urlVariables.put("numOfRows", "" + size);
		urlVariables.put("pageNo", "" + page);
		urlVariables.put("ServiceKey", serviceKey);
				
		String url = host + "/9710000/NationalAssemblyInfoService/getMemberCurrStateList";
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class, urlVariables);
		
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			Element element = Jsoup.parse(responseEntity.getBody()).body(); 
		}
		
		return null;
	}

	static class BasicInfoIndex {
		
	}
}
