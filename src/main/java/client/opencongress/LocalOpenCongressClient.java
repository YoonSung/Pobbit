package client.opencongress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;

public class LocalOpenCongressClient extends OpenCongressClient {
	public LocalOpenCongressClient(HttpClient httpClient) {
		super(httpClient);
	}

	@Override
	Map<String, List<Integer>> initializeIdListByName() {
		Map<String, List<Integer>> idsByName = new HashMap<>();
		InputStream inputStream = getClass().getResourceAsStream("/client/opencongress_politician.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		try {
			String line;
			while((line = reader.readLine()) != null) {
				if (StringUtils.isBlank(line)) {
					continue;
				}
				String[] split = StringUtils.split(line, ",");
				String name = split[1];
				int num = Integer.parseInt(split[0]);
				List<Integer> ids = idsByName.getOrDefault(name, new ArrayList<>());
				ids.add(num);
				idsByName.put(name, ids);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return idsByName;
	}
}
