package batch.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import batch.politician.InitAllPoliticianJob;
import batch.politician.InitAllPoliticianJobExecutor;
import client.mapper.PoliticianMapper;
import client.opencongress.OpenCongressClient;

@Configuration
public class InitAllPoliticianJobConfig {
	@Autowired
	private PoliticianMapper politicianMapper;

	@Autowired
	private OpenCongressClient openCongressClient;

	@Bean
	public InitAllPoliticianJobExecutor initializingAllPoliticianJobExecutor() {
		return new InitAllPoliticianJobExecutor(Collections.singletonList(new InitAllPoliticianJob(openCongressClient, politicianMapper)));
	}
}
