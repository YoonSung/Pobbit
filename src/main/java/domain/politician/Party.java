package domain.politician;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Party {
	JUSTICE("정의당"),
	THEMINJOO("더불어민주당"),
	LIBERTY_KOREA("자유한국당"),
	PEOPLE21("국민의당"),
	BAREUN("바른정당"),
	ETC("기타정당"),
	NO_PARTY("무소속")
	;

	private static final Map<String, Party> mapByKorean = Arrays.stream(values())
			.collect(Collectors.toMap(Party::korean, Function.identity()));

	public final String korean;

	Party(String korean) {
		this.korean = korean.toLowerCase();
	}

	public String korean() {
		return korean;
	}

	public static Party fromKorean(String korean) {
		return Optional.ofNullable(mapByKorean.get(korean.toLowerCase()))
				.orElse(Party.ETC);
	}
}
