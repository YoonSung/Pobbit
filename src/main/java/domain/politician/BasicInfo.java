package domain.politician;

import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	@OneToMany
	private List<StandingCommittee> standingCommittees;
}
