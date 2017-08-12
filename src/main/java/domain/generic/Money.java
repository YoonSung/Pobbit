package domain.generic;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Setter
@Getter
public class Money {
	private BigDecimal value;

	private Money(BigDecimal value) {
		this.value = value;
	}

	public static Money wons(long amount) {
		return new Money(BigDecimal.valueOf(amount));
	}

	public long longValue() {
		return this.value.longValue();
	}
}
