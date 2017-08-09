package domain.politician;

import java.util.List;

public interface StandingCommitteeRepositoryCustom {
	List<StandingCommittee>  findAllByName(List<String> names);
}
