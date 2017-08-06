package domain.politician;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ElectionTurns {
	private final List<Integer> turns;
}
