package domain.politician;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CriminalInfo {
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "politician")
	private Set<CriminalRecord> records = new HashSet<>();

	public void setRecords(List<CriminalRecord> records) {
		if (CollectionUtils.isNotEmpty(records)) {
			this.records.clear();
			this.records.addAll(records);
		}
	}
}
