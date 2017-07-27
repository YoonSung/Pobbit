package client.opendata;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

@Slf4j
public class RemoteOpenDataClient extends OpenDataClient {
	private final String serviceKey;
	private final String host;

	public RemoteOpenDataClient(HttpClient httpClient, String host, String serviceKey) {
		super(httpClient);
		this.host = host;
		this.serviceKey = serviceKey;
	}

	@Override
	public List<BasicInfoIndex> getAllPolitician(int page, int size) {
		try {
			StringBuilder urlBuilder = new StringBuilder(host + "/9710000/NationalAssemblyInfoService/getMemberCurrStateList");
			urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey); /*Service Key*/
			urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("" + size, "UTF-8"));
			urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("" + page, "UTF-8"));

			HttpGet httpGet = new HttpGet(urlBuilder.toString());
			httpGet.addHeader("content-type", "application/json");
			return super.executeToDocument(httpGet).map(super::toBasicInfoIndexList).orElse(new ArrayList<>());
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException(e);
		}
	}
}
