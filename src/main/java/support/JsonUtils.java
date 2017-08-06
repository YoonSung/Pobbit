package support;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonUtils {
	private static final ObjectMapper MAPPER;
	static {
		MAPPER = new ObjectMapper();
		MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		MAPPER.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
		MAPPER.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
		MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
	}
	
	public  static JsonNode fromJson(String json) {
		try {
			return MAPPER.readTree(json);
		} catch (IOException e) {
			throw new ParsingException(e);
		}
	}

	public static String toJson(Object object) {
		try {
			return MAPPER.writeValueAsString(object);
		} catch (IOException e) {
			throw new ParsingException(e);
		}
	}

	public static <T> List<T> fromJsonArray(String json, Class<T> clazz) {
		try {
			return MAPPER.readValue(json, TypeFactory.defaultInstance().constructCollectionType(List.class, clazz));
		} catch (IOException e) {
			throw new ParsingException(e);
		}
	}

	public static class ParsingException extends RuntimeException {
		public ParsingException(Exception e) {
			super(e);
		}
	}
}
