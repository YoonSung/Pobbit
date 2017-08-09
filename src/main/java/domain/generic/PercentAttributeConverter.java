package domain.generic;

import java.math.BigDecimal;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PercentAttributeConverter implements AttributeConverter<Percent, BigDecimal> {
	@Override
	public BigDecimal convertToDatabaseColumn(Percent percent) {
		return percent.value();
	}

	@Override
	public Percent convertToEntityAttribute(BigDecimal percent) {
		return Percent.of(percent, 2);
	}
}
