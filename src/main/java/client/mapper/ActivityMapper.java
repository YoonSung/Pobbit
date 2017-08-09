package client.mapper;

import org.springframework.stereotype.Component;

import client.opencongress.OpenCongressClient.PoliticianDetailPage;
import domain.politician.Activity;

@Component
public class ActivityMapper implements EntityMapper<PoliticianDetailPage, Activity> {
	@Override
	public Activity create(PoliticianDetailPage view) {
		Activity entity = new Activity();
		entity.setPlenaryMeetingAttendanceRate(view.getPlenaryMeetingAttendanceRate());
		// TODO ing
		//entity.setStandingCommitteeAttendances(view.getStandingCommitteeAttendanceRates());
		entity.setRepresentativeBillProposalCount(view.getRepresentativeBillProposalCount());
		return entity;
	}
}
