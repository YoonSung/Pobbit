package client.opencongress;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import client.CommonHttpClient;
import domain.generic.Money;
import domain.generic.Percent;
import support.JsoupUtils;

public abstract class OpenCongressClient extends CommonHttpClient {
	static final String LINK_PREFIX = "/?mid=Member&member_seq=";
	static final Pattern KOREAN_PATTERN = Pattern.compile("[\\x{ac00}-\\x{d7af}]+");
	static final String SEARCH_POLITICIAN_PATH = "/?mid=AssemblyMembers&mode=search&party=&region=&sangim=&gender=&elect_num=&page=";
	static final String HOST = "http://watch.peoplepower21.org";
	
	private final Map<String, List<Integer>> idsByNameAll;

	OpenCongressClient(HttpClient httpClient) {
		super(httpClient);
		idsByNameAll = Collections.unmodifiableMap(initializeIdListByName());
	}

	final Map<Integer, OpenCongressPoliticianPage> getAllPoliticianPage() {
		final Map<Integer, OpenCongressPoliticianPage> pageById = new HashMap<>();
		idsByNameAll.forEach((name, ids) -> ids.forEach(id -> {
			pageById.put(id, getPoliticianDetailPage(id));
		}));
		return Collections.unmodifiableMap(pageById);
	}

	OpenCongressPoliticianPage getPoliticianDetailPage(int id) {
		final OpenCongressPoliticianPage page = new OpenCongressPoliticianPage();
		Document document = fetchHtml(id);
		JsoupUtils.firstElementByClassName(document, "table-user-information")
				.flatMap(element -> JsoupUtils.firstElementByTagName(element, "tbody"))
				.ifPresent(element -> element.children().forEach(trElement -> {
					findValue("정당", trElement)
							.ifPresent(value -> page.party = value);
					findValue("선거구", trElement)
							.ifPresent(value -> page.districtOfElection = value);
					findValue("당선횟수", trElement)
							.ifPresent(value -> {
								int numberOfRepetition = Integer.parseInt(value.substring(0, value.indexOf("선")));
								if (numberOfRepetition > 1) {
									String trimmed = value.substring(value.indexOf("[") + 1, value.indexOf("]"))
											.replaceAll("대", "")
											.replaceAll(" ", "");
									Arrays.stream(trimmed.split(","))
											.map(String::trim)
											.map(Integer::parseInt)
											.collect(Collectors.toList());
								}
							});
				}));
		return null;
	}

	private Optional<String> findValue(String criteria, Element trElement) {
		Elements tds = trElement.children();
		if (StringUtils.equals(criteria, getTdValueFromIndex(0, tds))) {
			return Optional.of(getTdValueFromIndex(1, tds));
		} else {
			return Optional.empty();
		}
	}

	private String getTdValueFromIndex(int index, Elements tds) {
		return StringUtils.trim(tds.get(index).text());
	}

	protected abstract Document fetchHtml(int id);

	abstract Map<String, List<Integer>> initializeIdListByName();

	static class OpenCongressPoliticianPage {
		int id; // 아이디
		String url;
		String koreanName;  // 한국어 이름
		String chinessName; // 한문 이름
		String party;   // 소속정당
		int numberOfElection;   // 당선횟수
		List<Integer> electionTurns;  // 당선회차
		List<String> arcademyHistory;   // 학력
		List<String> majorCareers;  // 주요경력
		String contact; // 연락처
		String email;   // 연락처
		int representativeBillProposalCount;    // 대표발의 법안 갯수
		List<StandingCommitteeAttendance> standingCommitteeAttendanceRate;  //상임위 출석률
		Percent plenaryMeetingAttendanceRate;   // 본회의 출석률
		String districtOfElection; // 선거구
		Percent totalVoteTurnOutInDistriction;   // 선거구 총 투표율
		Percent electionVoteTurnOut;    // 당선 투표율
		List<DeclaredProperty> declaredPropertyHistory; // 연도별 신고재산
		PoliticalContribution politicalContribution;    // 후원금
	}

	// TODO 상세한 건물 등의 데이터도 필요하면 바꿔야함. 현재는 총합
	static class DeclaredProperty {
		private int year;
		private Money total;
	}

	static class PoliticalContribution {
		private Money averageAmount;    // TODO 전역적으로 사용
		private Money highAmount;
		private Money smallAmount;
		private Money totalAmount;
	}
	
	static class StandingCommitteeAttendance {
		String committeeName;
		Percent rate;
	}
}
