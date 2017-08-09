package batch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BatchJobExecutor<T> {
	//TODO set 으로 변경
	private final List<? extends BatchJob<T>> jobs;
	private final ArrayList<BatchJobSubscriber<T>> subscribers = new ArrayList<>();

	public void execute() {
		List<T> results = jobs.stream().map(BatchJob::execute).collect(Collectors.toList());
		subscribers.forEach(subscriber -> subscriber.notice(results));
	}

	public void subscribe(BatchJobSubscriber<T> subscriber) {
		this.subscribers.add(subscriber);
	} 
}
