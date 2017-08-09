package domain.politician;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandingCommitteeRepository extends JpaRepository<StandingCommittee, Integer>, StandingCommitteeRepositoryCustom {
}
