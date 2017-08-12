package domain.generic;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class Percent {
	private BigDecimal value;
	
	private Percent (BigDecimal value, int scale) {
		if (BigDecimal.ZERO.compareTo(value) > 0 || BigDecimal.valueOf(100).compareTo(value) < 0) {
			throw new IllegalArgumentException("must be range in 0 ~ 100");
		}
		this.value = value.setScale(scale, BigDecimal.ROUND_FLOOR);
	}

	public static Percent of(int value) {
		return of(BigDecimal.valueOf(value));
	}

	public static Percent of(BigDecimal value) {
		return new Percent(value, 2);
	}

	public static Percent of(BigDecimal value, int scale) {
		return new Percent(value, scale);
	}
	
	public static Percent of(double value) {
		return of(BigDecimal.valueOf(value));
	}

	private int intValue() {
		return value.intValue();
	}

	public BigDecimal value() {
		return value;
	}

	public double doubleValue() {
		return value.doubleValue();
	}
}
