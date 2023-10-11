package ru.ashabelskii.jasperreporttester.model;

public record ReportResult(
        Report report,
        String classContent,
        String jsonDataSource
) {
}
