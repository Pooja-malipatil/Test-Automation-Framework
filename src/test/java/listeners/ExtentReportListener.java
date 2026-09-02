package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.HashMap;
import java.util.Map;

/**
 * A TestNG ITestListener implementation that hooks into the test lifecycle
 * (start / pass / fail / skip / finish) and forwards each event into
 * ExtentReports, producing a self-contained HTML report after the run.
 *
 * Registered in testng.xml under <listeners>, so it applies automatically
 * to every test in the suite -- individual test classes don't need to
 * know it exists.
 */
public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent;

    // Maps each running thread to its current ExtentTest node, so that
    // parallel test execution logs results to the correct report entry.
    private static final Map<Long, ExtentTest> testMap = new HashMap<>();

    private static ExtentReports getReportInstance() {
        if (extent == null) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter("test-output/ExtentReport.html");
            sparkReporter.config().setDocumentTitle("Test Automation Report");
            sparkReporter.config().setReportName("API & UI Test Results");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Framework", "TestNG + REST Assured + Selenium");
        }
        return extent;
    }

    @Override
    public void onStart(ITestContext context) {
        getReportInstance();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = getReportInstance().createTest(
                result.getMethod().getMethodName(),
                result.getMethod().getDescription());
        test.assignCategory(result.getTestClass().getRealClass().getSimpleName());
        testMap.put(Thread.currentThread().getId(), test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testMap.get(Thread.currentThread().getId()).log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testMap.get(Thread.currentThread().getId())
                .log(Status.FAIL, "Test failed: " + result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testMap.get(Thread.currentThread().getId())
                .log(Status.SKIP, "Test skipped: " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        getReportInstance().flush();
    }
}
