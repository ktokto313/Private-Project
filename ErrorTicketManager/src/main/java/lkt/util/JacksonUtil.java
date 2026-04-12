package lkt.util;

import tools.jackson.databind.ObjectMapper;

public class JacksonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
    }

    public static String parseObjectToJSONString(Object object) {
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T parseJSONToObject(String json, Class<T> clazz) {
        return objectMapper.readValue(json, clazz);
    }
}
