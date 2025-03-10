package br.com.vrsoftware.config;

import br.com.vrsoftware.exceptions.SerializationException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperConfig {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static String toJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Falha ao serializar objeto.", e);
        }
    }

    public static <T> T fromJson(String content, Class<T> valueType) {
        try {
            // Special case for String.class
            if (String.class.equals(valueType)) {
                // If caller wants a String, we should still validate that it's proper JSON
                // We can do this by parsing and then ignoring the result
                MAPPER.readTree(content); // This will throw if not valid JSON
                return valueType.cast(content);
            }
            return MAPPER.readValue(content, valueType);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Falha ao deserializar objeto.", e);
        }
    }
}
