package batch.politician;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.tuple.Pair;

import batch.BatchJob;
import client.opencongress.OpenCongressClient;
import client.opencongress.OpenCongressClient.PoliticianDetailPage;
import client.opendata.OpenDataClient;
import client.opendata.OpenDataClient.BasicInfoIndex;
import domain.politician.Politician;

@RequiredArgsConstructor
public abstract class PoliticianBatchJob implements BatchJob<List<Politician>> {
	private final OpenCongressClient openCongressClient;
	private final OpenDataClient openDataClient;
	
	@Override
	public List<Politician> execute() {
		Map<String, BasicInfoIndex> basicIndexByName = openDataClient.getAllPolitician()
				.stream().collect(Collectors.toMap(BasicInfoIndex::getName, Function.identity()));
		return openCongressClient.getAllPoliticianPage().values().stream().map(page -> {
			BasicInfoIndex index = Optional.ofNullable(basicIndexByName.get(page.getKoreanName()))
					.orElseThrow(() -> new IllegalArgumentException("cannot find name : " + page.getKoreanName()));
			return Pair.of(index, page);
		}).map(eachDo()).collect(Collectors.toList());
	}
	
	abstract Function<Pair<BasicInfoIndex, PoliticianDetailPage>, Politician> eachDo();
}
