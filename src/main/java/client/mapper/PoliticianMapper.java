package client.mapper;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import client.opencongress.OpenCongressClient.PoliticianDetailPage;
import domain.politician.Politician;

@RequiredArgsConstructor
@Component
public class PoliticianMapper implements EntityMapper<PoliticianDetailPage, Politician> {
	private final BasicInfoMapper basicInfoMapper;
	private final ElectionInfoMapper electionInfoMapper;
	private final ActivityMapper activityMapper;
	private final PropertyInfoMapper propertyInfoMapper;
	private final CriminalInfoMapper criminalInfoMapper;

	@Override
	public Politician create(PoliticianDetailPage view) {
		Politician entity = new Politician();

		entity.setBasicInfo(basicInfoMapper.create(view));
		entity.setElectionInfo(electionInfoMapper.create(view));
		entity.setActivity(activityMapper.create(view));
		entity.setPropertyInfo(propertyInfoMapper.create(view));
		entity.setCriminalInfo(criminalInfoMapper.create(entity, view));
		
		return entity;
	}
}
