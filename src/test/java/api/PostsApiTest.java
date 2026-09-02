package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertTrue;

/**
 * API tests against JSONPlaceholder (https://jsonplaceholder.typicode.com),
 * a free fake REST API used widely for practicing test automation.
 *
 * These tests cover:
 *  - Status code validation (200, 201, 404)
 *  - Response body/field validation
 *  - Basic latency assertion
 *  - Data-driven testing via @DataProvider
 */
public class PostsApiTest {

    @BeforeClass
    public void setup() {
        // Setting a base URI once means every test below only needs the path.
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test(description = "GET a single existing post returns 200 and correct fields")
    public void testGetPostById_returns200AndValidFields() {
        given()
            .pathParam("id", 1)
        .when()
            .get("/posts/{id}")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("userId", notNullValue())
            .body("title", not(emptyString()))
            .body("body", not(emptyString()));
    }

    @Test(description = "GET a non-existent post returns 404")
    public void testGetNonExistentPost_returns404() {
        given()
        .when()
            .get("/posts/99999")
        .then()
            .statusCode(404);
    }

    @Test(description = "POST creates a new post and returns 201 with the submitted data echoed back")
    public void testCreatePost_returns201() {
        String requestBody = """
            {
              "title": "foo",
              "body": "bar",
              "userId": 1
            }
            """;

        given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/posts")
        .then()
            .statusCode(201)
            .body("title", equalTo("foo"))
            .body("body", equalTo("bar"))
            .body("userId", equalTo(1))
            .body("id", notNullValue());
    }

    @Test(description = "PUT updates an existing post and returns 200 with the updated title")
    public void testUpdatePost_returns200() {
        String requestBody = """
            {
              "id": 1,
              "title": "updated title",
              "body": "updated body",
              "userId": 1
            }
            """;

        given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .put("/posts/1")
        .then()
            .statusCode(200)
            .body("title", equalTo("updated title"));
    }

    @Test(description = "DELETE removes a post and returns 200")
    public void testDeletePost_returns200() {
        given()
        .when()
            .delete("/posts/1")
        .then()
            .statusCode(200);
    }

    @Test(description = "GET all posts responds within an acceptable latency and returns a non-empty list")
    public void testGetAllPosts_responseTimeAndPayload() {
        Response response =
            given()
            .when()
                .get("/posts")
            .then()
                .statusCode(200)
                .extract().response();

        long responseTimeMs = response.getTime();
        assertTrue(responseTimeMs < 3000,
                "Expected response time under 3000ms but was " + responseTimeMs + "ms");

        int postCount = response.jsonPath().getList("$").size();
        assertTrue(postCount > 0, "Expected at least one post in the response body");
    }

    @DataProvider(name = "invalidPostIds")
    public Object[][] invalidPostIds() {
        // Each inner array is one "row" of test data passed to the test method below.
        return new Object[][] {
            { 9999 },
            { 100000 },
            { -1 },
            { 0 }
        };
    }

    @Test(dataProvider = "invalidPostIds",
          description = "GET with various invalid/non-existent post IDs returns 404")
    public void testGetPost_withInvalidIds_returns404(int invalidId) {
        given()
            .pathParam("id", invalidId)
        .when()
            .get("/posts/{id}")
        .then()
            .statusCode(404);
    }
}