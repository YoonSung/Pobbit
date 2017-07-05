package client.opencongress;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;

import client.CommonHttpClient;

public abstract class OpenCongressClient extends CommonHttpClient {
	private final Map<String, List<Integer>> idsByNameAll;
	
	protected OpenCongressClient(HttpClient httpClient) {
		super(httpClient);
		idsByNameAll = Collections.unmodifiableMap(initializeIdListByName());
	}

	abstract Map<String, List<Integer>> initializeIdListByName();
}
