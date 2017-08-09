package domain.politician;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Converter(autoApply = true)
public class ElectionTurnAttributeConverter implements AttributeConverter<ElectionTurns, String> {
	private static final String DELIMITER = ",";

	@Override
	public String convertToDatabaseColumn(ElectionTurns attribute) {
		return Optional.ofNullable(attribute)
				.map(ElectionTurns::getTurns)
				.filter(CollectionUtils::isNotEmpty)
				.map(integers -> integers.stream().map(String::valueOf))
				.map(stream -> stream.collect(Collectors.joining(DELIMITER)))
				.orElse(null);
	}

	@Override
	public ElectionTurns convertToEntityAttribute(String dbData) {
		return Optional.ofNullable(dbData)
				.map(string -> Arrays.stream(StringUtils.split(string, DELIMITER)))
				.map(stream -> stream.map(Integer::parseInt).collect(Collectors.toList()))
				.map(ElectionTurns::new)
				.orElse(null);
	}
}
