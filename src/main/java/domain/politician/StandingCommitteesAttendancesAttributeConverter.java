package domain.politician;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.commons.collections4.CollectionUtils;

import support.JsonUtils;

@Converter(autoApply = true)
public class StandingCommitteesAttendancesAttributeConverter implements AttributeConverter<StandingCommitteeAttendances, String> {
	@Override
	public String convertToDatabaseColumn(StandingCommitteeAttendances attribute) {
		return Optional.ofNullable(attribute)
				.map(StandingCommitteeAttendances::getList)
				.filter(CollectionUtils::isNotEmpty)
				.map(list -> "[" + list.stream().map(JsonUtils::toJson).collect(Collectors.joining(",")) + "]")
				.orElse(null);
	}

	@Override
	public StandingCommitteeAttendances convertToEntityAttribute(String dbData) {
		List<StandingCommitteeAttendance> list = JsonUtils.fromJsonArray(dbData, StandingCommitteeAttendance.class);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return new StandingCommitteeAttendances(list);
	}
}
