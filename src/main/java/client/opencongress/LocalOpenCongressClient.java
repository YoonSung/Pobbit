package client.opencongress;

import static org.apache.commons.lang3.CharEncoding.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class LocalOpenCongressClient extends OpenCongressClient {
	public LocalOpenCongressClient(HttpClient httpClient) {
		super(httpClient);
	}

	@Override
	protected Document fetchHtml(int id) {
		InputStream inputStream = getClass().getResourceAsStream(String.format("/client/opencongress/detail/%d.html", id));
		try {
			return Jsoup.parse(IOUtils.toString(inputStream, UTF_8));
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

	@Override
	Map<String, List<Integer>> initializeIdListByName() {
		Map<String, List<Integer>> idsByName = new HashMap<>();
		InputStream inputStream = getClass().getResourceAsStream("/client/opencongress/politician.txt");
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
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
