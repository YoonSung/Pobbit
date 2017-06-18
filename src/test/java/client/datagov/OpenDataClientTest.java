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

	@Test
	public void test4() throws IOException {
		/** 에러났던 java 크롤러
		 * 일단 의견은 HttpPost를 사용하기 때문에 서버에서 편집인 줄 알고 id/pass를 물어보는 곳으로 이동하는 걸로..
		 * 하지만 GET으로 바꾸면 코드가 에러라 수정 중
		 // 2. 가져올 HTTP 주소 세팅
		 HttpGet http = new HttpGet("http://finance.naver.com/item/coinfo.nhn?code=045510&target=finsum_more");

		 // 3. 가져오기를 실행할 클라이언트 객체 생성
		 HttpClient httpClient = HttpClientBuilder.create().build();

		 // 4. 실행 및 실행 데이터를 Response 객체에 담음
		 HttpResponse response = httpClient.execute(http);

		 // 5. Response 받은 데이터 중, DOM 데이터를 가져와 Entity에 담음
		 HttpEntity entity = response.getEntity();

		 // 6. Charset을 알아내기 위해 DOM의 컨텐트 타입을 가져와 담고 Charset을 가져옴
		 ContentType contentType = ContentType.getOrDefault(entity);
		 Charset charset = contentType.getCharset();

		 // 7. DOM 데이터를 한 줄씩 읽기 위해 Reader에 담음 (InputStream / Buffered 중 선택은 개인취향)
		 BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), charset));

		 // 8. 가져온 DOM 데이터를 담기위한 그릇
		 StringBuffer sb = new StringBuffer();

		 // 9. DOM 데이터 가져오기
		 String line = "";
		 while((line=br.readLine()) != null){

		 sb.append(line+"\n");
		 }

		 // 10. 가져온 아름다운 DOM을 보자
		 System.out.println(sb.toString());

		 // 11. Jsoup으로 파싱해보자.
		 Document doc = Jsoup.parse(sb.toString());

		 // 참고 - Jsoup에서 제공하는 Connect 처리
		 Document doc2 = Jsoup.connect("http://finance.naver.com/item/coinfo.nhn?code=045510&target=finsum_more").get();
		 */
	}
}