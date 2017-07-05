package support;

import java.util.Optional;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupUtils {
	public static Optional<Element> getFirstElement(Element element, String tag) {
		Elements elements = element.getElementsByTag(tag);
		return elements.isEmpty() ? Optional.empty() : Optional.of(elements.get(0));
	}
}
