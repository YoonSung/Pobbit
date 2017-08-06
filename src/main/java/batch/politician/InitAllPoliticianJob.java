package batch.politician;

import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import client.mapper.PoliticianMapper;
import client.opencongress.OpenCongressClient;
import client.opencongress.OpenCongressClient.PoliticianDetailPage;
import client.opendata.OpenDataClient;
import client.opendata.OpenDataClient.BasicInfoIndex;
import domain.politician.Politician;

public class InitAllPoliticianJob extends PoliticianBatchJob {
	private final PoliticianMapper politicianMapper;
	public InitAllPoliticianJob(OpenCongressClient openCongressClient,
	                            OpenDataClient openDataClient, PoliticianMapper politicianMapper) {
		super(openCongressClient, openDataClient);
		this.politicianMapper = politicianMapper;
	}

	@Override
	Function<Pair<BasicInfoIndex, PoliticianDetailPage>, Politician> eachDo() {
		return politicianMapper::create;
	}
}
