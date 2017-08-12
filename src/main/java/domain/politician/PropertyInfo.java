package domain.politician;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class PropertyInfo {
	private DeclaredProperties declaredProperties;
	@Embedded
	private PoliticalContribution politicalContribution;
}
