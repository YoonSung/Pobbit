package service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.politician.StandingCommittee;
import domain.politician.StandingCommitteeRepository;

@RequiredArgsConstructor
@Service
public class CommitteeService {
	private final StandingCommitteeRepository standingCommitteeRepository;
	
	public StandingCommittee getOrCreatedCommittee(String committeeName) {
		if (StringUtils.isBlank(committeeName)) {
			throw new UnsupportedOperationException("committeeName must be exist");
		}
		committeeName = StringUtils.trim(committeeName);
		Optional<StandingCommittee> committeeOptional = standingCommitteeRepository.findByName(committeeName);
		if (!committeeOptional.isPresent()) {
			return standingCommitteeRepository.save(new StandingCommittee(committeeName));
		} else {
			return committeeOptional.get();
		}
	}

	@Transactional
	public List<StandingCommittee> find(List<String> names) {
		//TODO 성능
		return names.stream().map(this::getOrCreatedCommittee).collect(Collectors.toList());
	}
}
