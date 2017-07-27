package client.opendata;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.InitializingBean;

public class LocalOpenDataClient extends OpenDataClient implements InitializingBean {

	private List<BasicInfoIndex> basicInfoIndexList;

	public LocalOpenDataClient(HttpClient httpClient) {
		super(httpClient);
	}

	@Override
	protected List<BasicInfoIndex> getAllPolitician(int page, int size) {
		return basicInfoIndexList.stream().skip((page - 1) * size).limit(size).collect(Collectors.toList());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		InputStream inputStream = getClass().getResourceAsStream("/client/politicians.xml");
		this.basicInfoIndexList = super.toBasicInfoIndexList(Jsoup.parse(IOUtils.toString(inputStream, StandardCharsets.UTF_8.name())));
	}
}
