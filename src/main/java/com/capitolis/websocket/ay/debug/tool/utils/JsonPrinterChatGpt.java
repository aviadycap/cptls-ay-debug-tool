//package com.capitolis.websocket.ay.debug.tool.utils;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.*;
//
//@Service
//@Slf4j
//public class JsonPrinterChatGpt {
//    public static void test() throws IOException {
//        // Load JSON file
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode root = mapper.readTree(new File("/Users/aviady/Downloads/test.json")); // Change path if needed
//
//        // Process each top-level section
//        for (Iterator<String> it = root.fieldNames(); it.hasNext(); ) {
//            String section = it.next();
//            JsonNode node = root.get(section);
//
//            System.out.println("\n=== " + section.toUpperCase() + " ===");
//            if (node.isObject()) {
//                System.out.println(printJsonObjectAsTable(Collections.singletonList(node)));
//            } else if (node.isArray() && node.size() > 0) {
//                System.out.println(printJsonObjectAsTable(node));
//            } else {
//                System.out.println("No data available.");
//            }
//        }
//    }
//
//    public static String printJsonObjectAsTable(Iterable<JsonNode> objects) {
//        if (objects == null) return "No data available.";
//
//        // Extract column names from the first object
//        Set<String> columns = new LinkedHashSet<>();
//        for (JsonNode obj : objects) {
//            columns.addAll(getFieldNames(obj));
//        }
//        List<String> columnList = new ArrayList<>(columns);
//
//        // Compute column widths
//        Map<String, Integer> columnWidths = new HashMap<>();
//        for (String col : columnList) {
//            columnWidths.put(col, col.length()); // Minimum width = column name length
//        }
//        for (JsonNode obj : objects) {
//            for (String col : columnList) {
//                String value = obj.has(col) ? obj.get(col).asText() : "";
//                columnWidths.put(col, Math.max(columnWidths.get(col), value.length()));
//            }
//        }
//
//        // Build the table
//        StringBuilder sb = new StringBuilder();
//        printSeparator(sb, columnWidths);
//        printRow(sb, columnList, columnWidths);
//        printSeparator(sb, columnWidths);
//
//        for (JsonNode obj : objects) {
//            List<String> rowValues = new ArrayList<>();
//            for (String col : columnList) {
//                rowValues.add(obj.has(col) ? obj.get(col).asText() : "NULL");
//            }
//            printRow(sb, rowValues, columnWidths);
//        }
//
//        printSeparator(sb, columnWidths);
//        return sb.toString();
//    }
//
//    private static Set<String> getFieldNames(JsonNode node) {
//        Set<String> fieldNames = new HashSet<>();
//        node.fieldNames().forEachRemaining(fieldNames::add);
//        return fieldNames;
//    }
//
//    private static void printRow(StringBuilder sb, List<String> values, List<String> columnList, Map<String, Integer> columnWidths) {
//        sb.append("|");
//        for (int i = 0; i < columnList.size(); i++) { // 🔥 Iterate over column names, NOT values
//            String colName = columnList.get(i);
//            String value = (i < values.size()) ? values.get(i) : "NULL"; // 🔥 Ensure correct mapping
//            int width = columnWidths.getOrDefault(colName, 10); // 🔥 Get column width from column name
//            sb.append(" ").append(padRight(value, width)).append(" |");
//        }
//        sb.append("\n");
//    }
//
//    private static void printSeparator(StringBuilder sb, List<String> columnList, Map<String, Integer> columnWidths) {
//        sb.append("+");
//        for (String col : columnList) { // 🔥 Iterate using column names for consistent width
//            int width = columnWidths.getOrDefault(col, 10);
//            sb.append("-".repeat(width + 2)).append("+");
//        }
//        sb.append("\n");
//    }
//
//    private static String padRight(String text, int length) {
//        return String.format("%-" + length + "s", text);
//    }
//}