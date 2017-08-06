package domain.politician;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "open_congress_politicians")
public class OpenCongressPolitician {
	@Id
	private Integer openCongressId;
	private String url;
	private Integer politicianId;
}
