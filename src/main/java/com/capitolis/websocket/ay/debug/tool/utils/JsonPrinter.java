package com.capitolis.websocket.ay.debug.tool.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JsonPrinter {


    public static String formatJsonAsTable(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);

            JsonNode rootNode = objectMapper.readTree(json);

            if (rootNode.isArray()) {
                return formatArrayAsTable(rootNode);
            } else if (rootNode.isObject()) {
                return formatObjectAsTable(rootNode);
            } else {
                return "Invalid JSON format.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error formatting JSON.";
        }
    }

    private static String formatObjectAsTable(JsonNode rootNode) {
        List<String> headers = new ArrayList<>();
        List<String> values = new ArrayList<>();
        List<Integer> columnWidths = new ArrayList<>();

        rootNode.fieldNames().forEachRemaining(headers::add);
        for (String key : headers) {
            String value = getJsonValue(rootNode.get(key));
            values.add(value);
            columnWidths.add(Math.max(key.length(), value.length()));
        }

        return buildTable(headers, Collections.singletonList(values), columnWidths);
    }

    private static String formatArrayAsTable(JsonNode arrayNode) {
        if (arrayNode.size() == 0) return "Empty array.";

        Set<String> allHeaders = new LinkedHashSet<>();
        for (JsonNode node : arrayNode) {
            if (node.isObject()) {
                node.fieldNames().forEachRemaining(allHeaders::add);
            }
        }

        List<String> headers = new ArrayList<>(allHeaders);
        List<List<String>> rows = new ArrayList<>();
        List<Integer> columnWidths = new ArrayList<>(Collections.nCopies(headers.size(), 0));

        for (JsonNode node : arrayNode) {
            List<String> row = new ArrayList<>();
            for (String key : headers) {
                String value = getJsonValue(node.get(key));
                row.add(value);
                int index = headers.indexOf(key);
                columnWidths.set(index, Math.max(columnWidths.get(index), value.length()));
            }
            rows.add(row);
        }

        for (int i = 0; i < headers.size(); i++) {
            columnWidths.set(i, Math.max(columnWidths.get(i), headers.get(i).length()));
        }

        return buildTable(headers, rows, columnWidths);
    }

    private static String getJsonValue(JsonNode node) {
        if (node == null || node.isNull()) {
            return "";
        } else if (node.isTextual() || node.isNumber()) {
            return node.asText();
        } else if (node.isArray()) {
            int size = node.size();

            if (size == 3 && node.get(0).isNumber() && node.get(1).isNumber() && node.get(2).isNumber()) {
                // Convert [YYYY, MM, DD] to "YYYY-MM-DD"
                return String.format("%04d-%02d-%02d", node.get(0).asInt(), node.get(1).asInt(), node.get(2).asInt());
            } else if (size >= 6 && node.get(0).isNumber()) {
                // Convert [YYYY, MM, DD, HH, mm, ss, (optional) nanos] to "YYYY-MM-DD HH:mm:ss.SSS"
                int year = node.get(0).asInt();
                int month = node.get(1).asInt();
                int day = node.get(2).asInt();
                int hour = node.get(3).asInt();
                int minute = node.get(4).asInt();
                int second = node.get(5).asInt();
                int nano = size > 6 ? node.get(6).asInt() : 0;

                LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, second, nano);
                return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
            }
        } else if (node.isObject()) {
            return node.toString(); // Convert objects to string
        }
        return "";
    }

    private static String buildTable(List<String> headers, List<List<String>> rows, List<Integer> columnWidths) {
        StringBuilder table = new StringBuilder();
        String border = buildBorder(columnWidths);
        String separator = buildSeparator(columnWidths);

        table.append(border).append("\n");
        table.append(buildRow(headers, columnWidths)).append("\n");
        table.append(separator).append("\n");

        for (int i = 0; i < rows.size(); i++) {
            table.append(buildRow(rows.get(i), columnWidths)).append("\n");
            if (i < rows.size() - 1) {
                table.append(separator).append("\n"); // Row separator
            }
        }

        table.append(border);
        return table.toString();
    }

    private static String buildRow(List<String> row, List<Integer> columnWidths) {
        StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < row.size(); i++) {
            sb.append(" ").append(String.format("%-" + columnWidths.get(i) + "s", row.get(i))).append(" |");
        }
        return sb.toString();
    }

    private static String buildSeparator(List<Integer> columnWidths) {
        return buildBorder(columnWidths).replace("+", "|");
    }

    private static String buildBorder(List<Integer> columnWidths) {
        StringBuilder sb = new StringBuilder("+");
        for (int width : columnWidths) {
            sb.append("-".repeat(width + 2)).append("+");
        }
        return sb.toString();
    }
}
