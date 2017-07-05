package client.opencongress;

import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

public class LocalOpenCongressClientTest {

	@Test
	public void test() {
		LocalOpenCongressClient dut = new LocalOpenCongressClient(HttpClientBuilder.create().build());
		dut.initializeIdListByName();
	}
}