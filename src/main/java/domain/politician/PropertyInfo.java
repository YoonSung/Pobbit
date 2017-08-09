package domain.politician;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class PropertyInfo {
	private DeclaredProperties declaredProperties;
	@OneToOne(targetEntity = PoliticalContribution.class)
	@JoinColumn(name = "contribution_id")
	private PoliticalContribution politicalContribution;
}
