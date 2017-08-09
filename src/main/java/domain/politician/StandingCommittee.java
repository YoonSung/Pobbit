package domain.politician;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "standing_committees")
public class StandingCommittee {
	@Id
	private String name;

	public StandingCommittee(String name) {
		this.name = name;
	}
}
