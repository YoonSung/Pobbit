package domain.politician;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@Entity
@Table(name = "criminal_records")
public class CriminalRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String penalty;
	private String executionDate;

	@ManyToOne
	@JoinColumn(name = "politician_id", updatable = false)
	private Politician politician;

	public static CriminalRecord of(String name, String penalty, String executionDate) {
		CriminalRecord record = new CriminalRecord();
		record.name = removeTrimAndNewLine(name);
		record.penalty = removeTrimAndNewLine(penalty);
		record.executionDate = StringUtils.replace(removeTrimAndNewLine(executionDate), "、", "");
		return record;
	}

	private static String removeTrimAndNewLine(String text) {
		return StringUtils.trim(text).replaceAll("\n", "");
	}
}
