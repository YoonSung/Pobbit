package domain.politician;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import domain.generic.Percent;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class ElectionInfo {
	private ElectionTurns electionTurns;
	private String districtOfElection;
	private Percent totalVoteTurnOutInDistrict;   // 선거구 총 투표율
	private Percent electionVoteTurnOut;    // 당선 투표율
	boolean proportionalRepresentation; // 비례대표 유무
}
