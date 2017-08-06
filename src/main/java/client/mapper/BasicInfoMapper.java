package client.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import client.opencongress.OpenCongressClient.PoliticianDetailPage;
import client.opendata.OpenDataClient.BasicInfoIndex;
import domain.politician.BasicInfo;
import domain.politician.Party;
import domain.politician.StandingCommitteeRepository;

@RequiredArgsConstructor
@Component
public class BasicInfoMapper implements EntityMapper<Pair<BasicInfoIndex, PoliticianDetailPage>, BasicInfo>{
	private final StandingCommitteeRepository standingCommitteeRepository;
	@Override
	public BasicInfo create(Pair<BasicInfoIndex, PoliticianDetailPage> pair) {
		BasicInfoIndex view1 = pair.getLeft();
		PoliticianDetailPage view2 = pair.getRight();

		BasicInfo entity = new BasicInfo();
		entity.setName(view1.getName());
		entity.setParty(Party.fromKorean(view2.getParty()));
		entity.setContact(view2.getContact());
		entity.setEmail(view2.getEmail());
		entity.setImageUrl(Optional.ofNullable(view2.getImageUrl()).orElse(view1.getImageUrl()));
		
		entity.setAcademyHistory(listToString(view2.getAcademyHistory()));
		entity.setCareers(listToString(view2.getMajorCareers()));
		
		//TODO ing
//		Optional.ofNullable(view2.getStandingCommitteeAttendanceRates())
//		List<StandingCommittee> standingCommitteeList = standingCommitteeRepository.findAllByName();
//		entity.setStandingCommittees(Optional.ofNullable(view.getStandingCommitteeAttendanceRates())
//				                             .filter(CollectionUtils::isNotEmpty)
//				                             .map(StandingCommitteeAttendances::new)
//				                             .orElse(null)
//		);
		
		return entity;
	}
	
	 private String listToString(List<String> list) {
		return Optional.ofNullable(list)
				.filter(CollectionUtils::isNotEmpty)
				.map(l -> l.stream().collect(Collectors.joining(" ")))
				.orElse(null);
	}
}
