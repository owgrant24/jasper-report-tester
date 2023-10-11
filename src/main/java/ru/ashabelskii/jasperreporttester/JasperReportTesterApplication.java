package ru.ashabelskii.jasperreporttester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.ashabelskii.jasperreporttester.service.ReportReader;

@SpringBootApplication
public class JasperReportTesterApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(JasperReportTesterApplication.class, args);
        context.getBean(ReportReader.class).readFields();
    }

}
