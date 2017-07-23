package domain.generic;

import java.math.BigDecimal;

public class Money {
	private BigDecimal value;

	private Money(BigDecimal value, int scale) {
		this.value = value.setScale(scale, BigDecimal.ROUND_FLOOR);
	}
}
