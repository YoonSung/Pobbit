package batch;

public interface BatchJob<T> {
	T execute();
}
