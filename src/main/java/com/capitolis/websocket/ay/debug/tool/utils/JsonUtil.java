package com.capitolis.websocket.ay.debug.tool.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonUtil {

    @SneakyThrows
    public static String formatJsonRemoveKeys(String json, String removeKey) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);

        JsonNode jsonNode = objectMapper.readTree(json);
        ObjectNode object = (ObjectNode) jsonNode;
        object.remove(removeKey);

        return objectMapper.writeValueAsString(object);
    }

    @SneakyThrows
    public static String jsonGetByKeys(String json, String key) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);

        JsonNode jsonNode = objectMapper.readTree(json);
        ObjectNode object = (ObjectNode) jsonNode;
        jsonNode = object.get(key);
        return jsonNode.toString();
    }

    @SneakyThrows
    public static String jsonGetArrayByKeys(String json, String key) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);

        JsonNode rootNode = objectMapper.readTree(json);
//        return ((ArrayNode) rootNode)._children;
        StringBuilder stringBuilder = new StringBuilder();
        for (JsonNode node : rootNode) {
            JsonNode accrualPeriods = node.get("accrualPeriods");
            stringBuilder.append(accrualPeriods.toString());
        }

        return stringBuilder.toString();
    }

}
