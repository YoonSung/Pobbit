package domain.politician;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "politicians")
public class Politician {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer politicianId;

	@Embedded
	private BasicInfo basicInfo;

	@Embedded
	private ElectionInfo electionInfo;

	@Embedded
	private Activity activity;

	@Embedded
	private PropertyInfo propertyInfo;
}
