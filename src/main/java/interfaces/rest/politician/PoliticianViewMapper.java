package interfaces.rest.politician;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import domain.generic.Money;
import domain.generic.Percent;
import domain.politician.Activity;
import domain.politician.BasicInfo;
import domain.politician.CriminalInfo;
import domain.politician.DeclaredProperties;
import domain.politician.DeclaredProperty;
import domain.politician.ElectionInfo;
import domain.politician.ElectionTurns;
import domain.politician.PoliticalContribution;
import domain.politician.Politician;
import domain.politician.PropertyInfo;
import domain.politician.StandingCommittee;
import domain.politician.StandingCommitteeAttendance;
import interfaces.rest.ViewMapper;

@Component
public class PoliticianViewMapper implements ViewMapper<Politician, PoliticianView> {

	public List<PoliticianView> entitiesToViewList(List<Politician> entities) {
		List<PoliticianView> viewList = entities.stream().map(this::entityToView).collect(Collectors.toList());
		double mostAttendanceRate = viewList.stream()
				.reduce((v1, v2) -> v1.isGreaterAttendanceRateThan(v2) ? v1 : v2)
				.map(PoliticianView::getAverageAttendanceRate)
				.orElseThrow(UnsupportedOperationException::new);

		double mostElectionCount = viewList.stream()
				.reduce((v1, v2) -> v1.isElectedMoreThan(v2) ? v1 : v2)
				.map(PoliticianView::getElectionCount)
				.orElseThrow(UnsupportedOperationException::new);

		double mostBillProposalCount = viewList.stream()
				.reduce((v1, v2) -> v1.isBillProposalMoreThan(v2) ? v1 : v2)
				.map(PoliticianView::getBillProposalCount)
				.orElseThrow(UnsupportedOperationException::new);

		double mostCriminalRecordCount = viewList.stream()
				.reduce((v1, v2) -> v1.hasMoreCriminalRecordCountThan(v2) ? v1 : v2)
				.map(PoliticianView::getCriminalRecordCount)
				.orElseThrow(UnsupportedOperationException::new);

		double mostWealth = viewList.stream()
				.reduce((v1, v2) -> v1.isRicherThan(v2) ? v1 : v2)
				.map(PoliticianView::getProperty)
				.orElseThrow(UnsupportedOperationException::new);

		double mostContributedMoney = viewList.stream()
				.reduce((v1, v2) -> v1.isMoreContributedThan(v2) ? v1 : v2)
				.map(PoliticianView::getContribution)
				.orElseThrow(UnsupportedOperationException::new);
		
		viewList.forEach(view -> {
			SenatorView senatorView = new SenatorView();
			senatorView.setAttendance(view.getAverageAttendanceRate() / mostAttendanceRate);
			senatorView.setElection(view.getElectionCount() / mostElectionCount);
			senatorView.setBillProposal(view.getBillProposalCount() / mostBillProposalCount);
			senatorView.setLawAbiding((mostCriminalRecordCount - view.getCriminalRecordCount()) / mostCriminalRecordCount);
			senatorView.setProperty(view.getProperty() / mostWealth);
			senatorView.setContribution(view.getContribution() / mostContributedMoney);
			
			view.setSenator(senatorView);
		});
		return viewList;
	}
	
	@Override
	public PoliticianView entityToView(Politician entity) {
		PoliticianView view = new PoliticianView();
		view.setId(entity.getPoliticianId());
		setBasicInfo(entity.getBasicInfo(), view);
		setElectionInfo(entity.getElectionInfo(), view);
		setActivityInfo(entity.getActivity(), view);
		setPropertyInfo(entity.getPropertyInfo(), view);
		setCriminalInfo(entity.getCriminalInfo(), view);
		
		return view;
	}

	private void setBasicInfo(BasicInfo info, PoliticianView view) {
		view.setName(info.getName());
		view.setParty(info.getParty().korean);
		view.setContact(info.getContact());
		view.setEmail(info.getEmail());
		view.setImageUrl(info.getImageUrl());
		view.setAcademyHistory(info.getAcademyHistory());
		view.setCareers(info.getCareers());
		view.setCommittee(info.getStandingCommittees().stream().map(StandingCommittee::getName).collect(Collectors.toList()));
	}

	private void setElectionInfo(ElectionInfo info, PoliticianView view) {
		Optional.ofNullable(info.getElectionTurns()).map(ElectionTurns::getTurns)
				.ifPresent(turns -> view.setElectionTurns(new ArrayList<>(turns)));
		view.setDistrict(info.getDistrictOfElection());
		Optional.ofNullable(info.getTotalVoteTurnOutInDistrict()).map(Percent::doubleValue).ifPresent(view::setTotalVotePercent);
		Optional.ofNullable(info.getElectionVoteTurnOut()).map(Percent::doubleValue).ifPresent(view::setElectionVotePercent);
		view.setRepresentation(info.isProportionalRepresentation());
	}

	private void setActivityInfo(Activity activity, PoliticianView view) {
		view.setPlenaryMeetingAttendanceRate(activity.getPlenaryMeetingAttendanceRate().doubleValue());

		List<StandingCommitteeAttendance> attendanceList = activity.getStandingCommitteeAttendances();
		if (CollectionUtils.isNotEmpty(attendanceList)) {
			double sum = attendanceList.stream()
					.map(StandingCommitteeAttendance::getRate)
					.mapToDouble(Percent::doubleValue)
					.sum();

			view.setAverageCommitteeAttendanceRate(sum / attendanceList.size());
		}
		view.setBillProposalCount(activity.getRepresentativeBillProposalCount());
	}

	private void setPropertyInfo(PropertyInfo info, PoliticianView view) {
		Optional.ofNullable(info.getDeclaredProperties())
				.map(DeclaredProperties::getList)
				.filter(CollectionUtils::isNotEmpty)
				.ifPresent(properties -> {
			if (CollectionUtils.isNotEmpty(properties)) {
				long sum = properties.stream()
						.map(DeclaredProperty::getTotal)
						.mapToLong(Money::longValue)
						.sum();
				view.setProperty(sum / properties.size());
			}
		});
		
		PoliticalContribution contribution = info.getPoliticalContribution();
		long contributionSum = 0;
		contributionSum += Optional.ofNullable(contribution.getHighAmount()).map(Money::longValue).orElse(0L);
		contributionSum += Optional.ofNullable(contribution.getSmallAmount()).map(Money::longValue).orElse(0L);
		view.setContribution(contributionSum);
	}

	private void setCriminalInfo(CriminalInfo criminalInfo, PoliticianView view) {
		view.setCriminalRecordCount(Optional.ofNullable(criminalInfo)
				.map(info -> info.getRecords().size())
				.orElse(0));
	}
}
