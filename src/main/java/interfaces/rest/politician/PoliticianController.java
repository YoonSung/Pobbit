package interfaces.rest.politician;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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
import service.PoliticianService;

// TODO InitAllPoliticianJobSubscriber 를 Update 로 바꿀것
@RequiredArgsConstructor
@RestController
@RequestMapping("politicians")
public class PoliticianController extends InitAllPoliticianJobSubscriber implements InitializingBean {
	private final PoliticianService politicianService;
	private final PoliticianViewMapper viewMapper;
	private final InitAllPoliticianJobExecutor initAllPoliticianJobExecutor;
	private Map<Integer, PoliticianView> viewById;

	@PutMapping("reset")
	public ResponseEntity reset() {
		initAllPoliticianJobExecutor.execute();
		return ResponseEntity.ok().build();
	}

	@GetMapping
	public Map<Integer, PoliticianView> list() {
		return viewById;
	}

	public void init(List<Politician> politicians) {
		politicianService.deleteAll();
		politicianService.save(politicians);
		this.viewById = viewMapper.entitiesToViewList(politicians)
				.stream()
				.collect(Collectors.toMap(PoliticianView::getId, Function.identity()));
	}

	@Override
	protected void afterBatchJob(List<Politician> results) {
		init(results);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initAllPoliticianJobExecutor.subscribe(this);

		boolean initAtFirst = Optional.ofNullable(System.getProperty("initialize"))
				.map(Boolean::valueOf)
				.orElse(false);
		if (initAtFirst) {
			initAllPoliticianJobExecutor.execute();
		}
	}
}
