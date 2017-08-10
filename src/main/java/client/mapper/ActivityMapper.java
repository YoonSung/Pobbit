package client.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import client.opencongress.OpenCongressClient.PoliticianDetailPage;
import client.opencongress.OpenCongressClient.StandingCommitteeAttendanceDTO;
import domain.politician.Activity;
import domain.politician.StandingCommitteeAttendance;
import service.CommitteeService;

@RequiredArgsConstructor
@Component
public class ActivityMapper implements EntityMapper<PoliticianDetailPage, Activity> {
	private final CommitteeService committeeService;

	@Override
	public Activity create(PoliticianDetailPage view) {
		Activity entity = new Activity();
		entity.setPlenaryMeetingAttendanceRate(view.getPlenaryMeetingAttendanceRate());
		entity.setStandingCommitteeAttendances(standingCommitteeViewToEntity(view.getStandingCommitteeAttendanceRates()));
		entity.setRepresentativeBillProposalCount(view.getRepresentativeBillProposalCount());
		return entity;
	}

	private List<StandingCommitteeAttendance> standingCommitteeViewToEntity(List<StandingCommitteeAttendanceDTO> views) {
		if (CollectionUtils.isEmpty(views)) {
			return new ArrayList<>();
		}
		return views.stream().map(dto -> {
			StandingCommitteeAttendance attendance = new StandingCommitteeAttendance();
			attendance.setRate(dto.getRate());
			attendance.setCommittee(committeeService.getOrCreatedCommittee(dto.getCommittee()));
			return attendance;
		}).collect(Collectors.toList());
	}
}
