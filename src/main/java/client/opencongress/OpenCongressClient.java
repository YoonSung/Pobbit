package client.opencongress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.ToString;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import client.CommonHttpClient;
import domain.generic.Money;
import domain.generic.Percent;
import support.HangeulUtils;
import support.JsoupUtils;

public abstract class OpenCongressClient extends CommonHttpClient {
	static final String LINK_PREFIX = "/?mid=Member&member_seq=";
	static final String SEARCH_POLITICIAN_PATH = "/?mid=AssemblyMembers&mode=search&party=&region=&sangim=&gender=&elect_num=&page=";
	static final String HOST = "http://watch.peoplepower21.org";
	
	private final Map<String, List<Integer>> idsByNameAll;
	private final Map<Integer, String> nameById;

	OpenCongressClient(HttpClient httpClient) {
		super(httpClient);
		idsByNameAll = Collections.unmodifiableMap(initializeIdListByName());
		
		Map<Integer, String> nameById = new HashMap<>();
		idsByNameAll.forEach((name, ids) -> ids.forEach(id -> nameById.put(id, name)));
		this.nameById = Collections.unmodifiableMap(nameById);
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
		page.id = id;
		page.url = String.format("%s/%s%d", HOST, LINK_PREFIX, id); 
		page.koreanName = nameById.get(id);
		Document document = fetchHtml(id);
		JsoupUtils.firstElementByClassName(document, "table-user-information")
				.flatMap(element -> JsoupUtils.firstElementByTagName(element, "tbody"))
				.ifPresent(element -> element.children().forEach(trElement -> {
					findTdValueText("정당", trElement)
							.ifPresent(value -> page.party = HangeulUtils.removeNonHangeul(value));
					findTdValueText("선거구", trElement)
							.ifPresent(value -> page.districtOfElection = HangeulUtils.removeNonHangeul(value));
					findTdValueText("당선횟수", trElement)
							.ifPresent(value -> {
								int numberOfRepetition = Integer.parseInt(String.valueOf(value.charAt(0)));
								if (numberOfRepetition > 1) {
									String trimmed = value.substring(value.indexOf("[") + 1, value.indexOf("]"))
											.replaceAll("대", "")
											.replaceAll(" ", "");
									page.electionTurns = Arrays.stream(trimmed.split(","))
											.map(String::trim)
											.map(Integer::parseInt)
											.collect(Collectors.toList());
								}
								page.numberOfElection = numberOfRepetition;
							});
					findTdValueElement("소속위원회", trElement)
							.ifPresent(e -> {
								List<String> list = new ArrayList<>();
								e.getElementsByTag("a").forEach(ae -> list.add(ae.text()));
								page.standingCommittee = list;
							});
					
					findTdValueHtmlString("학력", trElement)
							.ifPresent(value -> page.arcademyHistory = splitBy(value, "<br>"));

					findTdValueHtmlString("주요경력", trElement)
							.ifPresent(value -> page.majorCareers = splitBy(value, "<br>"));

					findTdValueText("연락처", trElement)
							.ifPresent(value -> page.contact = value);

					findTdValueElement("Email", trElement)
							.flatMap(e -> JsoupUtils.firstElementByTagName(e, "a"))
							.ifPresent(e -> {
								String email = e.text();
								if (StringUtils.isNoneBlank(email)) {
									page.email = email;
								}
							});
				}));

		// 법안 발의
		page.representativeBillProposalCount = JsoupUtils.firstElementByClassName(document.getElementById("collapse1"), "panel-body")
				.flatMap(panelElement -> JsoupUtils.firstElementByTagName(panelElement, "h3"))
				.flatMap(h3Element -> JsoupUtils.firstElementByTagName(h3Element, "span"))
				.map(Element::text)
				.map(Integer::parseInt)
				.orElseThrow(IllegalStateException::new);

		// 상임위원회 활동
		JsoupUtils.firstElementByClassName(document.getElementById("collapse2"), "panel-body")
				.map(panelElement -> panelElement.getElementsByClass("iGraph"))
				.ifPresent(graphElements -> {
					List<StandingCommitteeAttendance> standingCommitteeAttendances = new ArrayList<>(page.standingCommittee.size());
					graphElements.stream()
							.filter(e -> StringUtils.equals(e.previousElementSibling().tagName(), "div"))
							.forEach(graphElement -> {
								Element divElement = graphElement.previousElementSibling();
								String committeeName = StringUtils.trim(divElement.text());
								Percent percent = stringToPercent(JsoupUtils.firstElementByClassName(graphElement, "ptext")
										                                  .orElseThrow(IllegalStateException::new).text());
								
								if (page.standingCommittee.contains(committeeName)) {
									StandingCommitteeAttendance attendance = new StandingCommitteeAttendance();
									attendance.committeeName = committeeName;
									attendance.rate = percent;
									standingCommitteeAttendances.add(attendance);
								}
							});
					page.standingCommitteeAttendanceRate = standingCommitteeAttendances;
				});
		
		// 본회의 활동
		page.plenaryMeetingAttendanceRate = JsoupUtils.firstElementByClassName(document.getElementById("collapse3"), "panel-body")
				.flatMap(panelElement -> JsoupUtils.firstElementByClassName(panelElement, "iGraph2"))
				.flatMap(graph2Element -> JsoupUtils.firstElementByClassName(graph2Element, "ptext"))
				.map(spanElement -> stringToPercent(spanElement.text()))
				.orElseThrow(IllegalStateException::new);
		
		// 선거정보
		Element electionInfo = document.getElementById("collapse4-1");
		if (electionInfo == null) {
			page.proportionalRepresentation = true;
		} else {
			JsoupUtils.firstElementByClassName(document.getElementById("collapse4-1"), "panel-body")
					.flatMap(panelElement -> JsoupUtils.firstElementByTagName(panelElement, "h3"))
					.map(h3Element -> h3Element.getElementsByTag("span"))
					.ifPresent(spanTags -> {
						if (spanTags.size() != 3) {
							throw new IllegalStateException();
						}
						page.totalVoteTurnOutInDistriction = stringToPercent(spanTags.get(0).text());
						page.electionVoteTurnOut = stringToPercent(spanTags.get(2).text());
					});
		}

		// 재산
		JsoupUtils.firstElementByClassName(document.getElementById("collapse5"), "panel-body")
				.flatMap(panelElement -> JsoupUtils.firstElementByTagName(panelElement, "table"))
				.ifPresent(tableElement -> {
					List<DeclaredProperty> properties = new ArrayList<>();
					Elements thElements = JsoupUtils.firstElementByTagName(tableElement, "thead")
							.map(tHeadElement -> tHeadElement.getElementsByTag("th"))
							.orElseThrow(IllegalStateException::new);
					
					IntStream.range(1, thElements.size()).forEach(index -> {
						DeclaredProperty property = new DeclaredProperty();
						property.year = Integer.parseInt(thElements.get(index).text());
						properties.add(property);
					});
					Elements tdElements = JsoupUtils.firstElementByClassName(tableElement, "info")
							.filter(trElement -> StringUtils.equals(trElement.tagName(), "tr"))
							.map(trElement -> trElement.getElementsByTag("td"))
							.orElseThrow(IllegalStateException::new);

					IntStream.range(1, tdElements.size()).forEach(index -> {
						DeclaredProperty property = properties.get(index -1);
						property.total = stringToMoney(tdElements.get(index).text());
					});
					
					page.declaredPropertyHistory = properties;
				});
		
		// 후원금
		Elements elements = document.getElementsByTag("script");
		for (Element element : elements) {
			String content = element.html();
			if (!content.contains("new CanvasJS.Chart(\"largeContainer\",")) {
				continue;
			}

			BiFunction<String, String, Money> moneyExtractor = (string, name) -> {
				String endKeyword = "\",";
				String nameKeyword = "name: \"";
				int nameIndex = string.indexOf(nameKeyword);
				if (nameIndex == -1) {
					return null;
				}
				String nameValue = string.substring(nameIndex + nameKeyword.length(), string.indexOf(endKeyword, nameIndex));
				if (!StringUtils.equals(name, nameValue)) {
					return null;
				}
				/* 필요할때 사용
				String labelKeyword = "{label: \"";
				int labelIndex = content.indexOf(labelKeyword);
				content.substring(labelIndex + labelKeyword.length(), content.indexOf(endKeyword, labelIndex));
				*/

				String valueKeyword = "y: ";
				int valueIndex = string.indexOf(valueKeyword);
				if (valueIndex == -1) {
					return null;
				}
				int cand1 = string.indexOf("},", valueIndex);
				int cand2 = string.indexOf(",", valueIndex);
				int endIndex = cand2 < cand1 ? cand2 : cand1;
				String value = string.substring(valueIndex + valueKeyword.length(), endIndex);
				return Money.wons(Long.parseLong(value));
			};

			PoliticalContribution contribution = new PoliticalContribution();
			Money highAmount = moneyExtractor.apply(content, "고액");
			contribution.highAmount = highAmount;
			if (highAmount != null) {
				contribution.smallAmount = moneyExtractor.apply(content.substring(content.indexOf("y: ") + "y: ".length()), "소액");;
			}
			page.politicalContribution = contribution;
		}
		return page;
	}

	private Money stringToMoney(String money) {
		return Money.wons(Long.parseLong(StringUtils.trim(money.replace(",", ""))) * 1000);
	}

	private Percent stringToPercent(String percent) {
		return Percent.of(Double.parseDouble(StringUtils.trim(percent.replace("%", ""))));
	}

	private List<String> splitBy(String value, String splitter) {
		return Arrays.stream(StringUtils.split(value, splitter))
				.map(StringUtils::trim)
				.collect(Collectors.toList());
	}

	private Optional<String> findTdValueHtmlString(String criteria, Element trElement) {
		return findTdValueElement(criteria, trElement).map(Element::html);
	}
	
	private Optional<Element> findTdValueElement(String criteria, Element trElement) {
		Elements tds = trElement.children();
		if (StringUtils.equals(criteria, getTdValueFromIndex(0, tds))) {
			return Optional.of(getTdChildFromIndex(1, tds));
		} else {
			return Optional.empty();
		}	
	}
	
	private Optional<String> findTdValueText(String criteria, Element trElement) {
		return findTdValueElement(criteria, trElement).map(Element::text);
	}

	private String getTdValueFromIndex(int index, Elements tds) {
		return StringUtils.trim(StringUtils.trim(tds.get(index).text()));
	}

	private Element getTdChildFromIndex(int index, Elements tds) {
		return tds.get(index);
	}

	protected abstract Document fetchHtml(int id);

	abstract Map<String, List<Integer>> initializeIdListByName();

	@ToString
	static class OpenCongressPoliticianPage {
		int id; // 아이디
		String url;
		String koreanName;  // 한국어 이름
		String party;   // 소속정당
		int numberOfElection;   // 당선횟수
		List<Integer> electionTurns;  // 당선회차
		List<String> standingCommittee; // 소속 상임위
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
		boolean proportionalRepresentation; // 비례대표 유무
		List<DeclaredProperty> declaredPropertyHistory; // 연도별 신고재산
		PoliticalContribution politicalContribution;    // 후원금
	}

	// TODO 상세한 건물 등의 데이터도 필요하면 바꿔야함. 현재는 총합
	@ToString
	static class DeclaredProperty {
		private int year;
		private Money total;
	}

	@ToString
	static class PoliticalContribution {
		private Money highAmount;
		private Money smallAmount;
	}
	
	@ToString
	static class StandingCommitteeAttendance {
		String committeeName;
		Percent rate;
	}
}
