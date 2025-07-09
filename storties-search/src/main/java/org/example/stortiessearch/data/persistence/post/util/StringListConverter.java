package org.example.stortiessearch.data.persistence.post.util;

import jakarta.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) return "{}";
        return attribute.stream()
            .map(this::escapePostgresText)
            .collect(Collectors.joining(",", "{", "}"));
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.equals("{}")) return Collections.emptyList();

        String raw = dbData.substring(1, dbData.length() - 1);
        Matcher matcher = Pattern.compile("\"((?:\\\\.|[^\"])*)\"").matcher(raw);

        List<String> result = new ArrayList<>();
        while (matcher.find()) {
            result.add(unescapePostgresText(matcher.group(1)));
        }
        return result;
    }

    private String escapePostgresText(String s) {
        return "\"" + s
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("'", "''") + "\"";
    }

    private String unescapePostgresText(String s) {
        return s
            .replace("\\\"", "\"")
            .replace("\\\\", "\\")
            .replace("''", "'");
    }
}
