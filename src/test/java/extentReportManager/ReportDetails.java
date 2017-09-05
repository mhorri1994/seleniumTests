package extentReportManager;

import com.aventstack.extentreports.reporter.configuration.Theme;
import lombok.Data;

@Data
public class ReportDetails {

    private final String reportFilePath, documentTitle, reportName;

    private Theme theme;

    public ReportDetails(String reportFilePath, String documentTitle, String reportName){
        this.reportFilePath = reportFilePath;
        this.documentTitle = documentTitle;
        this.reportName = reportName;

        System.out.println("Report created here: " + reportFilePath);
    }


}
