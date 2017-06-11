package client.datagov;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

public class OpenDataClientTest {

	private OpenDataClient dut;

	@Test
	public void test() {
		String host = "http://apis.data.go.kr";
		String serviceKey = "ih4O4s3bQH9kRS7o%2FIE0wyJsBkdTgLQztXKid%2B720cGszLwYhvaz2DvrXAK4WzhOjKcpWvc3IoXZbk2CWEYJIA%3D%3D";
		dut = new OpenDataClient(new RestTemplate(), serviceKey, host);
		
		dut.getAllPolitician();
	}
}