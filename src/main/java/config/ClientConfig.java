package config;

import org.apache.http.client.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import client.opencongress.LocalOpenCongressClient;
import client.opencongress.OpenCongressClient;
import client.opencongress.RemoteOpenCongressClient;
import client.opendata.LocalOpenDataClient;
import client.opendata.OpenDataClient;
import client.opendata.RemoteOpenDataClient;

@Import(HttpConfig.class)
@Configuration
@ComponentScan("client")
public class ClientConfig {

	//TODO phase 다르게 관리
	@Bean
	public OpenDataClient openDataClient(HttpClient httpClient) {
		//return remoteOpenDataClient(httpClient);
		return new LocalOpenDataClient(httpClient);
	}

	@Bean
	public OpenCongressClient openCongressClient(HttpClient httpClient) {
		//return remoteOpenCongressClient(httpClient);
		return new LocalOpenCongressClient(httpClient);
	}

	// ------------------------------------------------------------------------------
	// --- private
	// ------------------------------------------------------------------------------

	private OpenCongressClient remoteOpenCongressClient(HttpClient httpClient) {
		return new RemoteOpenCongressClient(httpClient);
	}

	private RemoteOpenDataClient remoteOpenDataClient(HttpClient httpClient) {
		String host = "http://apis.data.go";
		// TODO 환경변수로 받기
		String serviceKey = "";
		return new RemoteOpenDataClient(httpClient, host, serviceKey);
	}
}
