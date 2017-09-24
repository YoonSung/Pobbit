package client.mapper;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import client.opencongress.OpenCongressClient.PoliticianDetailPage;
import domain.politician.CriminalInfo;
import domain.politician.Politician;
import service.CriminalRecordService;

@Component
@RequiredArgsConstructor
public class CriminalInfoMapper {
	private final CriminalRecordService criminalRecordService;
	
	public CriminalInfo create(Politician politician, PoliticianDetailPage politicianDetailPage) {
		return criminalRecordService.getByName(politicianDetailPage.getKoreanName())
				.map(criminalPolitician -> {
					CriminalInfo info = new CriminalInfo();
					criminalPolitician.getRecordList().forEach(criminalRecord -> criminalRecord.setPolitician(politician));
					info.setRecords(criminalPolitician.getRecordList());
					return info;
				}).orElse(null);
	}
}
