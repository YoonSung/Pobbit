package service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import domain.politician.Politician;
import domain.politician.PoliticianRepository;

@RequiredArgsConstructor
@Service
public class PoliticianService {
	private final PoliticianRepository politicianRepository;

	public void deleteAll() {
		politicianRepository.deleteAll();
	}

	public void save(List<Politician> politicians) {
		politicianRepository.save(politicians);
	}
}
