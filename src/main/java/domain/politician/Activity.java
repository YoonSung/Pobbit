package domain.politician;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import domain.generic.Percent;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Activity {
	private Percent plenaryMeetingAttendanceRate;   // 본회의 출석률
	@ElementCollection(fetch = FetchType.EAGER)
	private List<StandingCommitteeAttendance> standingCommitteeAttendances;
	private int representativeBillProposalCount;
}
