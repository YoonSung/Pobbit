package batch;

import java.util.List;

public interface BatchJobSubscriber<T> {
	 void notice(List<T> results);
}
