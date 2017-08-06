package domain.politician;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import domain.generic.Money;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "political_contributions")
public class PoliticalContribution {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Money highAmount;
	private Money smallAmount;
}
