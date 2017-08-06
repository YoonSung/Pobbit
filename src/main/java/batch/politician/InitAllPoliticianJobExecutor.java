package batch.politician;

import java.util.List;

import batch.BatchJobExecutor;
import domain.politician.Politician;

public class InitAllPoliticianJobExecutor extends BatchJobExecutor<List<Politician>> {
	public InitAllPoliticianJobExecutor(List<InitAllPoliticianJob> batchJobs) {
		super(batchJobs);
	}
}
