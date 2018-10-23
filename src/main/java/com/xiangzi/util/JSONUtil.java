package com.xiangzi.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JSONUtil {

	protected static Logger logger = LoggerFactory.getLogger(JSONUtil.class);

	private static ObjectMapper objectMapper = new ObjectMapper();
	private static final String charsetName = "UTF-8";

	static {
		objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		SerializationConfig serializationConfig = objectMapper.getSerializationConfig();
		serializationConfig = serializationConfig.without(SerializationFeature.WRAP_ROOT_VALUE).with(SerializationFeature.INDENT_OUTPUT);
		SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
		serializerProvider.setNullValueSerializer(new JsonSerializer<Object>() {

			@Override
			public void serialize(Object value, JsonGenerator jgen, SerializerProvider serializers) throws IOException, JsonProcessingException {
				jgen.writeString("");
			}
			
		});
	}

	// public static ObjectMapper getObjectMapper() {
	// return CollectionUtils.deepCopy(objectMapper);
	// }

	public static String toJSONString(Object object) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
		try {
			JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(bos, JsonEncoding.UTF8);
			objectMapper.writeValue(jsonGenerator, object);
			return bos.toString(charsetName);
		} catch (Exception e) {
			throw new JSONException(e.getMessage(), e);
		} finally {
			try {
				bos.close();
			} catch (Exception ex) {
				logger.error("toJSONString(bos.close) Exception ", ex);
			}
		}
	}

	public static JsonNode parseJsonNode(String text) {
		try {
			return objectMapper.readTree(text);
		} catch (Exception e) {
			throw new JSONException(e.getMessage(), e);
		}
	}

	public static <T> T parseObject(String text, Class<T> clazz) {
		try {
			return objectMapper.readValue(text, clazz);
		} catch (Exception e) {
			throw new JSONException(e.getMessage(), e);
		}
	}

	public static <T> T parseObject(String text, Class<T> clazz, String charsetName) {
		try {
			return objectMapper.readValue(text.getBytes(charsetName), clazz);
		} catch (Exception e) {
			throw new JSONException(e.getMessage(), e);
		}
	}

	public static <T> T parseObject(String text, TypeReference<T> valueTypeRef) {
		try {
			return objectMapper.readValue(objectMapper.getFactory().createParser(text), valueTypeRef);
		} catch (Exception e) {
			throw new JSONException(e.getMessage(), e);
		}
	}

	public static ObjectNode createObjectNode() {
		try {
			return objectMapper.createObjectNode();
		} catch (Exception e) {
			throw new JSONException(e.getMessage(), e);
		}
	}

	/**
	 * 从json中读取tagPath处的值 tagPath用 :分隔
	 * 
	 * @param json
	 * @param tagPath
	 * @return
	 * @throws Exception
	 */
	public static List<String> readValueFromJson(String json, String tagPath) throws Exception {
		// 返回值
		List<String> value = new ArrayList<String>();
		if (StringUtils.isBlank(json) || StringUtils.isBlank(tagPath)) {
			return value;
		}
		String[] path = tagPath.split(":");
		JsonNode node = objectMapper.readTree(json);
		getJsonValue(node, path, value, 1);
		return value;
	}

	public static void getJsonValue(JsonNode node, String[] path, List<String> values, int nextIndex) {
		if (node == null) {
			return;
		}
		// 是路径的最后就直接取值
		if (nextIndex == path.length) {
			if (node.isArray()) {
				for (int i = 0; i < node.size(); i++) {
					JsonNode child = node.get(i).get(path[nextIndex - 1]);
					if (child == null) {
						continue;
					}
					values.add(child.toString());
				}
			} else {
				JsonNode child = node.get(path[nextIndex - 1]);
				if (child != null) {
					values.add(child.toString());
				}
			}
			return;
		}
		// 判断是Node下是集合还是一个节点
		node = node.get(path[nextIndex - 1]);
		if (node.isArray()) {
			for (int i = 0; i < node.size(); i++) {
				getJsonValue(node.get(i), path, values, nextIndex + 1);
			}
		} else {
			getJsonValue(node, path, values, nextIndex + 1);
		}
	}

}
