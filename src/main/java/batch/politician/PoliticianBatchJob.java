package batch.politician;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import batch.BatchJob;
import client.opencongress.OpenCongressClient;
import client.opencongress.OpenCongressClient.PoliticianDetailPage;
import domain.politician.Politician;

@RequiredArgsConstructor
public abstract class PoliticianBatchJob implements BatchJob<List<Politician>> {
	private final OpenCongressClient openCongressClient;

	@Override
	public List<Politician> execute() {
		return openCongressClient.getAllPoliticianPage().values().stream().map(eachDo()).collect(Collectors.toList());
	}
	
	abstract Function<PoliticianDetailPage, Politician> eachDo();
}
