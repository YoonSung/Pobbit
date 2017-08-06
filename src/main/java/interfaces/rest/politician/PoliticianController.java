package interfaces.rest.politician;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import batch.politician.InitAllPoliticianJobExecutor;
import batch.politician.InitAllPoliticianJobSubscriber;
import domain.politician.Politician;
import domain.politician.PoliticianRepository;

@RequiredArgsConstructor
@RestController
@RequestMapping("politicians")
public class PoliticianController extends InitAllPoliticianJobSubscriber implements InitializingBean {
	
	private final PoliticianRepository politicianRepository;
	private final InitAllPoliticianJobExecutor initAllPoliticianJobExecutor;
	
	@PutMapping("reset")
	public ResponseEntity reset() {
		politicianRepository.deleteAll();
		initAllPoliticianJobExecutor.execute();
		return ResponseEntity.ok().build();
	}

	private void init(List<Politician> politicians) {
		
	}

	@Override
	protected void afterBatchJob(List<Politician> results) {
		init(results);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initAllPoliticianJobExecutor.subscribe(this);
		init(politicianRepository.findAll());
	}
}
