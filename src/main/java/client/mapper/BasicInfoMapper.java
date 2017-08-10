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
import service.CommitteeService;

@RequiredArgsConstructor
@Component
public class BasicInfoMapper implements EntityMapper<PoliticianDetailPage, BasicInfo>{
	private final CommitteeService committeeService;

	@Override
	public BasicInfo create(PoliticianDetailPage view) {

		BasicInfo entity = new BasicInfo();
		entity.setName(view.getKoreanName());
		entity.setParty(Party.fromKorean(view.getParty()));
		entity.setContact(view.getContact());
		entity.setEmail(view.getEmail());
		entity.setImageUrl(view.getImageUrl());
		Optional.ofNullable(view.getStandingCommittee())
				.filter(CollectionUtils::isNotEmpty)
				.ifPresent(names -> entity.setStandingCommittees(committeeService.find(names)));
				
		entity.setAcademyHistory(listToString(view.getAcademyHistory()));
		entity.setCareers(listToString(view.getMajorCareers()));
		
		return entity;
	}
	
	 private String listToString(List<String> list) {
		return Optional.ofNullable(list)
				.filter(CollectionUtils::isNotEmpty)
				.map(l -> l.stream().collect(Collectors.joining(" ")))
				.orElse(null);
	}
}
