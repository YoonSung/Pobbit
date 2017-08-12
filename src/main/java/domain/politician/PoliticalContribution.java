package domain.politician;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import domain.generic.Money;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class PoliticalContribution {
	private Money highAmount;
	private Money smallAmount;
}
