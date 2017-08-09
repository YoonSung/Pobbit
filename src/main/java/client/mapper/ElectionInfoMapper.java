package client.mapper;

import java.util.Optional;

import org.springframework.stereotype.Component;

import client.opencongress.OpenCongressClient.PoliticianDetailPage;
import domain.politician.ElectionInfo;
import domain.politician.ElectionTurns;

@Component
public class ElectionInfoMapper implements EntityMapper<PoliticianDetailPage, ElectionInfo> {
	@Override
	public ElectionInfo create(PoliticianDetailPage view) {
		ElectionInfo entity = new ElectionInfo();
		Optional.ofNullable(view.getElectionTurns()).ifPresent(integers -> entity.setElectionTurns(new ElectionTurns(integers)));
		entity.setDistrictOfElection(view.getDistrictOfElection());
		entity.setTotalVoteTurnOutInDistrict(view.getTotalVoteTurnOutInDistrict());
		entity.setElectionVoteTurnOut(view.getElectionVoteTurnOut());
		entity.setProportionalRepresentation(view.isProportionalRepresentation());
		return entity;
	}
}
