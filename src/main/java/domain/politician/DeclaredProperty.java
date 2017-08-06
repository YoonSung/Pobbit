package domain.politician;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import domain.generic.Money;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeclaredProperty {
	private int year;
	private Money total;
}
