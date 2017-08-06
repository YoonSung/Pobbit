package domain.politician;

import java.util.List;

import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;

public class StandingCommitteeRepositoryCustomImpl extends QueryDslRepositorySupport implements StandingCommitteeRepositoryCustom {
	public StandingCommitteeRepositoryCustomImpl() {
		super(StandingCommittee.class);
	}

	@Override
	public List<StandingCommittee> findAllByName(List<String> names) {
		return null;
	}
}
