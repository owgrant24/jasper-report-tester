package ru.ashabelskii.jasperreporttester.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ashabelskii.jasperreporttester.model.JasperField;
import ru.ashabelskii.jasperreporttester.model.Report;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {
    private static final Pattern PATTERN_FIELD = Pattern.compile(
            "\t*<field name=\"([A-Za-z0-9]+)\" class=\"java.(lang|util|math|sql).([A-Za-z]+)\"/>\\n*");

    private final ObjectMapper objectMapper;

    public String generatePojo(String content) {
        var data = readReport(content);
        var stringBuilder = new StringBuilder();
        stringBuilder.append("public class TestClass {\n");
        for (var jasperField : data.fields().values()) {
            stringBuilder
                    .append("    private ")
                    .append(jasperField.type())
                    .append(" ")
                    .append(jasperField.name())
                    .append(";\n");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @SneakyThrows
    public String generateTestData(String content) {
        var data = readReport(content);
        var json = objectMapper.createObjectNode();
        for (var field : data.fields().values()) {
            fillNode(json, field);
        }
        return json.toPrettyString();
    }

    private void fillNode(ObjectNode json, JasperField field) {
        if (Boolean.class.getSimpleName().equals(field.type())) {
            json.put(field.name(), true);
        } else if (Double.class.getSimpleName().equals(field.type())) {
            json.put(field.name(), 20.0);
        } else if (Float.class.getSimpleName().equals(field.type())) {
            json.put(field.name(), 78.0);
        } else if (Integer.class.getSimpleName().equals(field.type())) {
            json.put(field.name(), 10);
        } else if (Long.class.getSimpleName().equals(field.type())) {
            json.put(field.name(), 88);
        } else if (Short.class.getSimpleName().equals(field.type())) {
            json.put(field.name(), 1);
        } else if (String.class.getSimpleName().equals(field.type())) {
            json.put(field.name(), "test data");
        } else if (BigDecimal.class.getSimpleName().equals(field.type())) {
            json.put(field.name(), 66.0);
        } else {
            log.warn("Неподдерживаемый тип {}", field.type());
        }
    }

    private Report readReport(String content) {
        var matcher = PATTERN_FIELD.matcher(content);
        var map = new LinkedHashMap<String, JasperField>();
        while (matcher.find()) {
            var mame = matcher.group(1);
            var type = matcher.group(3);
            var jasperField = new JasperField(mame, type);
            map.put(mame, jasperField);
            log.debug(jasperField.toString());
        }
        return new Report(map, content);
    }
}