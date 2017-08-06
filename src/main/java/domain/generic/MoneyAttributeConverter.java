package domain.generic;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MoneyAttributeConverter implements AttributeConverter<Money, Long> {
	@Override
	public Long convertToDatabaseColumn(Money money) {
		return money == null ? null : money.longValue();
	}

	@Override
	public Money convertToEntityAttribute(Long amount) {
		return amount != null ? Money.wons(amount) : null;
	}
}
