package support;

import java.util.Optional;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupUtils {
	public static Optional<Element> firstElementByTagName(Element element, String tag) {
		return getFirst(element.getElementsByTag(tag));
	}

	public static Optional<Element> firstElementByClassName(Element element, String className) {
		return getFirst(element.getElementsByClass(className));
	}
	
	private static Optional<Element> getFirst(Elements elements) {
		return elements.isEmpty() ? Optional.empty() : Optional.of(elements.get(0));
	}
}
