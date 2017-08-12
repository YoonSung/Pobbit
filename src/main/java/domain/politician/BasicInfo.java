package domain.politician;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.apache.commons.collections4.CollectionUtils;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class BasicInfo {
	private String name;
	@Enumerated(EnumType.STRING)
	private Party party;
	private String contact;
	private String email;
	private String imageUrl;
	private String academyHistory;
	private String careers;
	@ManyToMany(targetEntity = StandingCommittee.class)
	private Set<StandingCommittee> standingCommittees = new HashSet<>();

	public void setStandingCommittees(List<StandingCommittee> standingCommittees) {
		if (CollectionUtils.isNotEmpty(standingCommittees)) {
			this.standingCommittees.clear();
			this.standingCommittees.addAll(standingCommittees);
		}
	}
}
