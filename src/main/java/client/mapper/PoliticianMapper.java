package client.mapper;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import client.opencongress.OpenCongressClient.PoliticianDetailPage;
import client.opendata.OpenDataClient.BasicInfoIndex;
import domain.politician.Politician;

@RequiredArgsConstructor
@Component
public class PoliticianMapper implements EntityMapper<Pair<BasicInfoIndex, PoliticianDetailPage>, Politician> {
	private final BasicInfoMapper basicInfoMapper;
	private final ElectionInfoMapper electionInfoMapper;
	private final ActivityMapper activityMapper;
	private final PropertyInfoMapper propertyInfoMapper;
	
	@Override
	public Politician create(Pair<BasicInfoIndex, PoliticianDetailPage> pair) {
		Politician entity = new Politician();

		entity.setBasicInfo(basicInfoMapper.create(pair));
		entity.setElectionInfo(electionInfoMapper.create(pair.getRight()));
		entity.setActivity(activityMapper.create(pair.getRight()));
		entity.setPropertyInfo(propertyInfoMapper.create(pair.getRight()));
		
		return entity;
	}
}
