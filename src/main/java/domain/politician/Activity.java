package domain.politician;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.apache.commons.collections4.CollectionUtils;

import domain.generic.Percent;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Activity {
	private Percent plenaryMeetingAttendanceRate;   // 본회의 출석률
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StandingCommitteeAttendance> standingCommitteeAttendances = new ArrayList<>();
	private int representativeBillProposalCount;

	public void setStandingCommitteeAttendances(List<StandingCommitteeAttendance> standingCommitteeAttendances) {
		if (CollectionUtils.isNotEmpty(standingCommitteeAttendances)) {
			this.standingCommitteeAttendances.clear();
			this.standingCommitteeAttendances.addAll(standingCommitteeAttendances);
		}
	}
}
