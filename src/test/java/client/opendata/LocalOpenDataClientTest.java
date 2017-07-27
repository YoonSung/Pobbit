package client.opendata;

import static org.assertj.core.api.Java6Assertions.*;

import java.util.List;

import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import client.opendata.LocalOpenDataClient;
import client.opendata.OpenDataClient.BasicInfoIndex;

public class LocalOpenDataClientTest {

	private static LocalOpenDataClient dut;

	@BeforeClass
	public static void init() throws Exception {
		dut = new LocalOpenDataClient(HttpClientBuilder.create().build());
		dut.afterPropertiesSet();
	}

	@Test
	public void test() {
		List<BasicInfoIndex> list = dut.getAllPolitician();
		assertThat(list.size()).isEqualTo(299);
		//dut.getAllPolitician().stream().forEach(System.out::println);
	}
}