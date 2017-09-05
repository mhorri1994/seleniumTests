package extentReportManager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import lombok.Getter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ExtentReportManager {

    private ExtentHtmlReporter extentHtmlReporter;
    private ExtentReports report;
    private Map<String,ExtentTest> tests = new HashMap<String, ExtentTest>();

    public ExtentReportManager(ReportType reportType, ReportDetails reportDetails){
        report = new ExtentReports();
        attachReport(reportType, reportDetails);
    }

    private void attachReport(ReportType reportType, ReportDetails reportDetails) {
        switch (reportType){
            case HTML:
                report.attachReporter(getHtmlReporter(reportDetails));
        }
    }

    public void addScreenShot(String testName,String imageFilePath) throws IOException {
        ExtentTest extentTest = tests.get(testName);
        extentTest.addScreenCaptureFromPath(imageFilePath);
    }

    public ExtentTest getTest(String testName){
        return tests.get(testName);
    }

    public ExtentTest setUpTest(){
        Exception e = new Exception();
        e.fillInStackTrace();
        String testName = e.getStackTrace()[1].getMethodName();
        ExtentTest test = report.createTest(testName);
        tests.put(testName,test);
        return tests.get(testName);
    }

    private ExtentHtmlReporter getHtmlReporter(ReportDetails reportDetails) {
        String filePath = reportDetails.getReportFilePath() + ".html";
        extentHtmlReporter = new ExtentHtmlReporter(filePath);
        // make the charts visible on report open
        extentHtmlReporter.config().setChartVisibilityOnOpen(true);
        extentHtmlReporter.config().setDocumentTitle(reportDetails.getDocumentTitle());
        extentHtmlReporter.config().setReportName(reportDetails.getReportName());
        extentHtmlReporter.config().setTheme(reportDetails.getTheme());
        return extentHtmlReporter;
    }

    public void clearTests(){
        tests.clear();
        report.flush();
    }

    public enum ReportType{
        HTML,
    }

}
