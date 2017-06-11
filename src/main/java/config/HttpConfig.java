package config;

import java.util.Collections;
import java.util.List;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class HttpConfig {
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
	}

	@Bean
	public RestTemplate restTemplate(ObjectMapper objectMapper, ClientHttpRequestFactory clientHttpRequestFactory) {
		return createRestTemplate(objectMapper, clientHttpRequestFactory);
	}

	/**
	 * SYNC Client
	 */
	@Bean
	public ClientHttpRequestFactory httpRequestFactory(CloseableHttpClient httpClient) {
		return new HttpComponentsClientHttpRequestFactory(httpClient);
	}

	@Bean
	public CloseableHttpClient httpClient() {
		final int DEFAULT_MAX_TOTAL_CONNECTIONS = 60;
		final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5;
		final int DEFAULT_READ_TIMEOUT_MILLISECONDS = (60 * 1000);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTIONS);
		connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);
		RequestConfig config = RequestConfig.custom().setConnectTimeout(DEFAULT_READ_TIMEOUT_MILLISECONDS).build();

		return HttpClientBuilder.create()
				.setConnectionManager(connectionManager)
				.setDefaultRequestConfig(config).build();
	}

	private RestTemplate createRestTemplate(ObjectMapper objectMapper, ClientHttpRequestFactory httpRequestFactory) {
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
		List<HttpMessageConverter<?>> converters = restTemplate
				.getMessageConverters();

		converters.stream().filter(converter -> converter instanceof MappingJackson2HttpMessageConverter).forEach(converter -> {
			MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) converter;
			jsonConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
			jsonConverter.setObjectMapper(objectMapper);
		});
		return restTemplate;
	}

	@Bean
	public MappingJackson2JsonView jackson2JsonView() {
		return new MappingJackson2JsonView();
	}
}
