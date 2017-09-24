package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import domain.politician.CriminalRecord;

@Component
public class CriminalRecordService {
	private static final Pattern pattern = Pattern.compile("\"(?![\",])(.*?)\"");
	private final Map<String, CriminalPolitician> mapByPoliticianName;

	public CriminalRecordService() {
		mapByPoliticianName = new HashMap<>();
		InputStream inputStream = getClass().getResourceAsStream("criminal_record.txt");
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
			String line;
			while((line = reader.readLine()) != null) {
				if (StringUtils.isBlank(line)) {
					continue;
				}
				List<String> split = new ArrayList<>();
				Matcher matcher = pattern.matcher(line);
				while(matcher.find()) {
					split.add(StringUtils.replace(matcher.group(), "\"", ""));
				}
				String politicianName = split.get(0);
				System.out.println(politicianName);
				CriminalPolitician criminal = mapByPoliticianName.get(politicianName);
				if (criminal == null) {
					criminal = new CriminalPolitician();
					criminal.setParty(split.get(1));
					criminal.setDistrictOfElection(split.get(2));
					criminal.setRecordCount(Integer.parseInt(split.get(3)));
					criminal.addRecord(CriminalRecord.of(split.get(4), split.get(5), split.get(6)));
					mapByPoliticianName.put(politicianName, criminal);
				} else {
					criminal.addRecord(CriminalRecord.of(split.get(4), split.get(5), split.get(6)));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public Optional<CriminalPolitician> getByName(String politicianName) {
		return Optional.ofNullable(mapByPoliticianName.get(StringUtils.trim(politicianName)));
	}

	List<CriminalPolitician> getAllPoliticians() {
		return new ArrayList<>(mapByPoliticianName.values());
	}
	
	@ToString
	@Data
	public static class CriminalPolitician {
		String number;
		String politicianName;
		String party;
		String districtOfElection;
		int recordCount;
		List<CriminalRecord> recordList = new ArrayList<>();

		void addRecord(CriminalRecord record) {
			this.recordList.add(record);
		}
	}
}
