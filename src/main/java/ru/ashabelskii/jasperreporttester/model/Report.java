package ru.ashabelskii.jasperreporttester.model;

import java.util.Map;

public record Report(
        Map<String, JasperField> fields,
        String content
) {
}
