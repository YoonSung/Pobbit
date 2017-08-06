package client.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import client.opencongress.OpenCongressClient.PoliticianDetailPage;
import domain.politician.BasicInfo;
import domain.politician.Party;
import domain.politician.StandingCommitteeRepository;

@RequiredArgsConstructor
@Component
public class BasicInfoMapper implements EntityMapper<PoliticianDetailPage, BasicInfo>{
	private final StandingCommitteeRepository standingCommitteeRepository;
	@Override
	public BasicInfo create(PoliticianDetailPage view) {

		BasicInfo entity = new BasicInfo();
		entity.setName(view.getKoreanName());
		entity.setParty(Party.fromKorean(view.getParty()));
		entity.setContact(view.getContact());
		entity.setEmail(view.getEmail());
		entity.setImageUrl(view.getImageUrl());
		
		entity.setAcademyHistory(listToString(view.getAcademyHistory()));
		entity.setCareers(listToString(view.getMajorCareers()));
		
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
