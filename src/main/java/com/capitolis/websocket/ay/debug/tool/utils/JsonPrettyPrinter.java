package com.capitolis.websocket.ay.debug.tool.utils;

import com.capitolis.websocket.ay.debug.tool.model.CpiEnvelopeReportDao;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class JsonPrettyPrinter {
//
//    public static void printJsonAsTable(String json) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode rootNode = mapper.readTree(json);
//
//        AsciiTable table = new AsciiTable();
//        table.addRule();
//        table.addRow("Platoon", "Home Days", "Base Days");
//        table.addRule();
//
//        for (JsonNode platoon : rootNode.get("platoonSchedules")) {
//            int platoonNumber = platoon.get("platoon").asInt();
//            String homeDays = platoon.get("home").toString();
//            String baseDays = platoon.get("base").toString();
//            table.addRow(platoonNumber, homeDays, baseDays);
//            table.addRule();
//        }
//
//        log.info(table.render());
//    }
//

    public class JsonUtil {
        private static final ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule()); // Enable Java 8 Date/Time support

        public static String toJson(Object obj) {
            try {
                return objectMapper.writeValueAsString(obj);
            } catch (Exception e) {
                throw new RuntimeException("Failed to convert object to JSON", e);
            }
        }

        public static String toJsonArray(List<?> objList) {
            return toJson(objList); // Simply calls the existing method
        }
    }

    public static String printJsonAsTable(CpiEnvelopeReportDao object) throws IOException {
        String json = JsonUtil.toJson(object);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        // Get first array in JSON object
        JsonNode firstArray = null;
        for (JsonNode value : root) {
            if (value.isArray() && value.size() > 0) {
                firstArray = value;
                break;
            }
        }
        if (firstArray == null) {
            return "No valid JSON array found.";
        }

        // Extract column names dynamically
        Set<String> columns = new LinkedHashSet<>();
        for (JsonNode node : firstArray) {
            columns.addAll(getFieldNames(node));
        }
        List<String> columnList = new ArrayList<>(columns);

        // Compute column widths
        Map<String, Integer> columnWidths = new HashMap<>();
        for (String col : columnList) {
            columnWidths.put(col, col.length()); // Minimum width = column name length
        }
        for (JsonNode node : firstArray) {
            for (String col : columnList) {
                String value = node.has(col) ? node.get(col).asText() : "";
                columnWidths.put(col, Math.max(columnWidths.get(col), value.length()));
            }
        }

        // Build the table
        StringBuilder sb = new StringBuilder();

        // Print header
        printRow(sb, columnList, columnWidths);
        printSeparator(sb, columnWidths);

        // Print data rows
        for (JsonNode node : firstArray) {
            List<String> rowValues = new ArrayList<>();
            for (String col : columnList) {
                rowValues.add(node.has(col) ? node.get(col).asText() : "");
            }
            printRow(sb, rowValues, columnWidths);
        }

        printSeparator(sb, columnWidths);
        return sb.toString();
    }

    private static Set<String> getFieldNames(JsonNode node) {
        Set<String> fieldNames = new HashSet<>();
        node.fieldNames().forEachRemaining(fieldNames::add);
        return fieldNames;
    }

    private static void printRow(StringBuilder sb, List<String> values, Map<String, Integer> columnWidths) {
        sb.append("|");
        for (String col : values) {
            sb.append(" ").append(padRight(col, columnWidths.get(col))).append(" |");
        }
        sb.append("\n");
    }

    private static void printSeparator(StringBuilder sb, Map<String, Integer> columnWidths) {
        sb.append("+");
        for (String col : columnWidths.keySet()) {
            sb.append("-".repeat(columnWidths.get(col) + 2)).append("+");
        }
        sb.append("\n");
    }

    private static String padRight(String text, int length) {
        return String.format("%-" + length + "s", text);
    }
}
