package batch.politician;

import java.util.function.Function;

import client.mapper.PoliticianMapper;
import client.opencongress.OpenCongressClient;
import client.opencongress.OpenCongressClient.PoliticianDetailPage;
import domain.politician.Politician;

public class InitAllPoliticianJob extends PoliticianBatchJob {
	private final PoliticianMapper politicianMapper;
	public InitAllPoliticianJob(OpenCongressClient openCongressClient, PoliticianMapper politicianMapper) {
		super(openCongressClient);
		this.politicianMapper = politicianMapper;
	}

	@Override
	Function<PoliticianDetailPage, Politician> eachDo() {
		return politicianMapper::create;
	}
}
