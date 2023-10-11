package ru.ashabelskii.jasperreporttester.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.ashabelskii.jasperreporttester.model.JasperField;
import ru.ashabelskii.jasperreporttester.model.Report;

import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportReader {

    private static final String PATH = "/data/input.txt";
    private static final Pattern PATTERN_FIELD = Pattern.compile(
            "\t*<field name=\"([A-Za-z0-9]+)\" class=\"java.lang.([A-Za-z]+)\"/>\\n*");

    private final ObjectMapper objectMapper;

    public void readFields() {
        var data = readReport();
        var s = buildClass(data);
        var v = buildTestJSON(data);
        log.info(s);
        log.info(v);
    }

    @SneakyThrows
    private String buildTestJSON(Report report) {
        var json = objectMapper.createObjectNode();
        report.fields().values()
                .forEach(field -> generateNode(json, field));
        return json.toPrettyString();
    }

    private void generateNode(ObjectNode json, JasperField field) {
        if (Boolean.class.getSimpleName().equals(field.type())) {
            json.put(field.name(), true);
        }
        if (String.class.getSimpleName().equals(field.type())) {
            json.put(field.name(), "test data");
        }
    }

    private String buildClass(Report report) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("public class TestClass {\n");
        report.fields().values()
                .forEach(jasperField -> stringBuilder
                        .append("private ")
                        .append(jasperField.type())
                        .append(" ")
                        .append(jasperField.name())
                        .append(";\n"));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private Report readReport() {
        var content = readFile();
        var matcher = PATTERN_FIELD.matcher(content);
        var map = new LinkedHashMap<String, JasperField>();
        while (matcher.find()) {
            var mame = matcher.group(1);
            var type = matcher.group(2);
            var jasperField = new JasperField(mame, type);
            map.put(mame, jasperField);
            log.debug(jasperField.toString());
        }
        return new Report(map, content);
    }

    @SneakyThrows
    private static String readFile() {
        var resource = new ClassPathResource(PATH).getFile();
        return new String(Files.readAllBytes(resource.toPath()));
    }
}
