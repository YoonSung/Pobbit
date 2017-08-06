package batch.config;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import batch.politician.InitAllPoliticianJob;
import batch.politician.InitAllPoliticianJobExecutor;
import batch.politician.InitAllPoliticianJobSubscriber;
import client.mapper.PoliticianMapper;
import client.opencongress.OpenCongressClient;
import client.opendata.OpenDataClient;
import config.ClientConfig;
import config.WebConfig;

@Configuration
public class InitAllPoliticianJobConfig {
	@Autowired
	private PoliticianMapper politicianMapper;

	@Autowired
	private OpenCongressClient openCongressClient;

	@Autowired
	private OpenDataClient openDataClient;

	@Autowired
	private List<InitAllPoliticianJobSubscriber> subscribers;

	@Bean
	public InitAllPoliticianJobExecutor initializingAllPoliticianJobExecutor() {
		return new InitAllPoliticianJobExecutor(
				Collections.singletonList(new InitAllPoliticianJob(openCongressClient, openDataClient, politicianMapper)),
				subscribers
		);
	}
}
