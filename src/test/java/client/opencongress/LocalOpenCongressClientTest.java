package client.opencongress;

import java.util.Map;

import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import client.opencongress.OpenCongressClient.OpenCongressPoliticianPage;

public class LocalOpenCongressClientTest {

	@Test
	public void test() {
		LocalOpenCongressClient dut = new LocalOpenCongressClient(HttpClientBuilder.create().build());
		dut.initializeIdListByName();
	}
	
	@Test
	public void detail() {
		LocalOpenCongressClient dut = new LocalOpenCongressClient(HttpClientBuilder.create().build());
		final Map<Integer, OpenCongressPoliticianPage> map = dut.getAllPoliticianPage();
		map.forEach((integer, openCongressPoliticianPage) -> {
			System.out.println(openCongressPoliticianPage.toString());
		});
	}
}