package support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class HangeulUtils {
	public static final String KOREAN_REGEX_STRING = "[\\x{ac00}-\\x{d7af}]+";
	public static final Pattern KOREAN_PATTERN = Pattern.compile(HangeulUtils.KOREAN_REGEX_STRING);

	public static String removeNonHangeul(String text) {
		if (StringUtils.isBlank(text)) {
			return text;
		}
		Matcher matcher = HangeulUtils.KOREAN_PATTERN.matcher(text);
		if (matcher.find()) {
			return matcher.group();
		}
		return text;
	}
}
