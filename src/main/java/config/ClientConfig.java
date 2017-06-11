package config;

import org.apache.http.client.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import client.datagov.OpenDataClient;

@Import(HttpConfig.class)
@Configuration
@ComponentScan("client")
public class ClientConfig {
	
	@Bean
	public OpenDataClient openDataClient(HttpClient httpClient) {
		String host = "http://apis.data.go";
		String serviceKey = "ih4O4s3bQH9kRS7o%2FIE0wyJsBkdTgLQztXKid%2B720cGszLwYhvaz2DvrXAK4WzhOjKcpWvc3IoXZbk2CWEYJIA%3D%3D";
		return new OpenDataClient(httpClient, serviceKey, host);
	}
}
