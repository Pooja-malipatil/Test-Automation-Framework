# Test Automation Framework

![Run Tests](https://github.com/Pooja-malipatil/Test-Automation-Framework/actions/workflows/ci.yml/badge.svg)

A Java-based test automation project demonstrating API testing, UI testing,
and automated HTML reporting — built to showcase practical QA/SDET skills.

## What this project does

This is a self-contained Maven project with two independent test layers:

1. **API tests** (`src/test/java/api`) — REST Assured tests against
   [JSONPlaceholder](https://jsonplaceholder.typicode.com), a free public
   fake REST API. They validate:
   - Successful GET requests (`200`)
   - Resource-not-found handling (`404`)
   - Resource creation (`201`) with payload validation
   - Update (`PUT`) and delete (`DELETE`) flows
   - Basic response-time (latency) assertions

2. **UI tests** (`src/test/java/ui`) — Selenium WebDriver tests against
   [the-internet.herokuapp.com](https://the-internet.herokuapp.com), a
   public site built for Selenium practice. They cover:
   - A login form with both a valid-credentials and invalid-credentials flow
   - A dropdown form-element interaction

Both suites run together under **TestNG**, and results are automatically
compiled into a polished **HTML report** via **ExtentReports** every time
you run `mvn test`.

## Tech stack

| Purpose            | Tool                          |
|--------------------|--------------------------------|
| Build tool          | Maven                          |
| Test runner         | TestNG                         |
| API testing         | REST Assured                   |
| UI/browser testing  | Selenium WebDriver (Chrome)    |
| Driver management   | WebDriverManager (Bonigarcia)  |
| Reporting           | ExtentReports (Spark reporter) |
| Language / runtime  | Java 17                        |

## Project structure

```
test-automation-framework/
├── pom.xml                                  # Maven dependencies & build config
├── testng.xml                               # TestNG suite: which tests run, listeners
├── README.md
└── src/test/java/
    ├── api/
    │   └── PostsApiTest.java                # REST Assured API tests
    ├── ui/
    │   ├── LoginTest.java                   # Selenium login form tests
    │   └── DropdownFormTest.java            # Selenium dropdown tests
    └── listeners/
        └── ExtentReportListener.java        # TestNG -> ExtentReports bridge
```

## Prerequisites

- Java 17+ (`java -version`)
- Maven 3.8+ (`mvn -version`)
- Google Chrome installed locally (WebDriverManager downloads the matching
  driver automatically — you don't need to install chromedriver yourself)

## How to run

Clone or download the project, then from the project root:

```bash
mvn test
```

This will:
1. Download dependencies
2. Run all API tests (fast, no browser needed)
3. Run all UI tests in headless Chrome
4. Generate the HTML report at:

```
test-output/ExtentReport.html
```

Open that file in any browser to view the results.

### Running only one suite

To run just the API tests during development, you can temporarily comment
out the `<test name="UI Tests">` block in `testng.xml`, or run a single
class directly:

```bash
mvn test -Dtest=PostsApiTest
```



## Sample report

![Test report overview](screenshots/report-overview.png)

All 10 tests (6 API + 4 UI) passing via `mvn test`.
```

## Design notes / talking points

- **Given-When-Then structure** in the API tests mirrors REST Assured's
  fluent DSL and doubles as living documentation of each test's intent.
- **Headless Chrome** is used for UI tests so the suite can run in CI
  environments without a display.
- **WebDriverManager** removes the classic "chromedriver version mismatch"
  maintenance burden.
- **ExtentReports is wired in via a TestNG listener**, not inside the test
  classes — this keeps reporting concerns fully decoupled from test logic.
- JSONPlaceholder's write endpoints (`POST`/`PUT`/`DELETE`) are simulated:
  they return the correct status codes and echo the payload, but don't
  persist data server-side. That's expected and worth mentioning if asked.

## Possible extensions

- Add a Page Object Model layer for the UI tests as the suite grows
- Parameterize environments (dev/staging) via `testng.xml` or system properties
- Add retry logic for flaky UI tests via an `IRetryAnalyzer`
- Swap ExtentReports for Allure, or run both
- Add GitHub Actions CI to run `mvn test` on every push
