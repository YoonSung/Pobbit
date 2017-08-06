package batch;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BatchJobExecutor<T> {
	private final List<? extends BatchJob<T>> jobs;
	private final List<? extends BatchJobSubscriber<T>> subscribers;

	public void execute() {
		List<T> results = jobs.stream().map(BatchJob::execute).collect(Collectors.toList());
		subscribers.forEach(subscriber -> subscriber.notice(results));
	}
}
