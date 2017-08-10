package interfaces.rest.politician;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import batch.politician.InitAllPoliticianJobExecutor;
import batch.politician.InitAllPoliticianJobSubscriber;
import domain.politician.Politician;
import domain.politician.PoliticianRepository;

// TODO InitAllPoliticianJobSubscriber 를 Update 로 바꿀것
@RequiredArgsConstructor
@RestController
@RequestMapping("politicians")
public class PoliticianController extends InitAllPoliticianJobSubscriber implements InitializingBean {
	
	private final PoliticianRepository politicianRepository;
	private final PoliticianViewMapper viewMapper;
	private final InitAllPoliticianJobExecutor initAllPoliticianJobExecutor;
	private List<PoliticianView> views;

	@PutMapping("reset")
	public ResponseEntity reset() {
		politicianRepository.deleteAll();
		initAllPoliticianJobExecutor.execute();
		return ResponseEntity.ok().build();
	}

	@GetMapping
	public List<PoliticianView> list() {
		return views;
	}

	private void init(List<Politician> politicians) {
		this.views = politicians.stream().map(viewMapper::entityToView).collect(Collectors.toList());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initAllPoliticianJobExecutor.subscribe(this);
		init(politicianRepository.findAll());
	}

	@Override
	protected void afterBatchJob(List<Politician> results) {
		init(results);
	}
}
