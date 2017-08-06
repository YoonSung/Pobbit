package batch.politician;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import batch.BatchJobSubscriber;
import domain.politician.Politician;

public abstract class InitAllPoliticianJobSubscriber implements BatchJobSubscriber<List<Politician>> {
	@Override
	public final void notice(List<List<Politician>> results) {
		afterBatchJob(results.stream().flatMap(Collection::stream).collect(Collectors.toList()));
	}

	protected abstract void afterBatchJob(List<Politician> results);
}
