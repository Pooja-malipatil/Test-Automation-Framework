package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retries a failed test up to MAX_RETRY_COUNT times before letting it
 * report as a final failure. Useful for UI tests, where occasional
 * timing/network flakiness can cause a false failure even with proper waits.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 2;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            return true; // tells TestNG to run this test again
        }
        return false; // give up after MAX_RETRY_COUNT retries, report as failed
    }
}