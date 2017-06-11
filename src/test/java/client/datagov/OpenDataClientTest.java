package client.datagov;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class OpenDataClientTest {

	private OpenDataClient dut;

	String host = "http://apis.data.go.kr";
	String serviceKey = "ih4O4s3bQH9kRS7o%2FIE0wyJsBkdTgLQztXKid%2B720cGszLwYhvaz2DvrXAK4WzhOjKcpWvc3IoXZbk2CWEYJIA%3D%3D";
	
	@Test
	public void test() {
		dut = new OpenDataClient(HttpClientBuilder.create().build(), serviceKey, host);
		
		dut.getAllPolitician();
	}
	
	@Test
	public void test2() throws IOException {

		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/9710000/NationalAssemblyInfoService/getMemberCurrStateList"); /*URL*/
		urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey); /*Service Key*/
		urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("30", "UTF-8")); /*파라미터설명*/
		urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*파라미터설명*/
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(urlBuilder.toString());
		HttpResponse httpResponse = httpClient.execute(httpGet);

		Element element = Jsoup.parse(EntityUtils.toString(httpResponse.getEntity(), "UTF-8"));
		Elements elements = element.getElementsByTag("body");
		elements.get(0).getElementsByTag("items").get(0).getElementsByTag("item").forEach(e -> {
			System.out.println(e.toString());
		});
	}
	
	@Test
	public void test3() throws IOException {
		Element element = Jsoup.connect("http://watch.peoplepower21.org/?mid=Member&member_seq=892").header("Accept-encoding", "").get().body();
		System.out.println(element.toString());
	}
}