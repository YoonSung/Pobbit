package domain.politician;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.commons.collections4.CollectionUtils;

import support.JsonUtils;

@Converter(autoApply = true)
public class DeclaredPropertiesAttributeConverter implements AttributeConverter<DeclaredProperties, String> {
	@Override
	public String convertToDatabaseColumn(DeclaredProperties attribute) {
		return Optional.ofNullable(attribute)
				.map(DeclaredProperties::getList)
				.filter(CollectionUtils::isNotEmpty)
				.map(list -> "[" + list.stream().map(JsonUtils::toJson).collect(Collectors.joining(",")) + "]")
				.orElse(null);
	}

	@Override
	public DeclaredProperties convertToEntityAttribute(String dbData) {
		List<DeclaredProperty> list = JsonUtils.fromJsonArray(dbData, DeclaredProperty.class);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return new DeclaredProperties(list);
	}
}
