package domain.politician;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandingCommitteeRepository extends JpaRepository<StandingCommittee, String> {
	Optional<StandingCommittee> findByName(String name); 
}
