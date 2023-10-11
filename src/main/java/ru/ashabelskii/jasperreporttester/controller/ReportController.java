package ru.ashabelskii.jasperreporttester.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ashabelskii.jasperreporttester.dto.ReportContentRequest;
import ru.ashabelskii.jasperreporttester.service.ReportService;

@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/generate/pojo")
    public String generatePojo(@RequestBody ReportContentRequest request) {
        return reportService.generatePojo(request.report());
    }

    @PostMapping("/generate/testData")
    public String generateTestData(@RequestBody ReportContentRequest request) {
        return reportService.generateTestData(request.report());
    }
}
