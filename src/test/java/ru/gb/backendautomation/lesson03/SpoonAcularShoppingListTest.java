package ru.gb.backendautomation.lesson03;

import io.restassured.http.ContentType;
import static org.hamcrest.CoreMatchers.is;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

public class SpoonAcularShoppingListTest extends AbstractTest {

    private String id = "";

    @ParameterizedTest
    @DisplayName("Verify behaviour depending on 'parse' value")
    @CsvSource({"Something specific for false, false", "Something specific for true, true"})
    void testAddToShoppingList(String title, boolean value) {
        id = given()
                .baseUri(getBaseUrl())
                .queryParam("hash", "a3da66460bfb7e62ea1c96cfa0b7a634a346ccbf")
                .queryParam("apiKey", getApiKey())
            .contentType(ContentType.JSON)
                .body(String.format("{\n" +
                    "\t\"item\": %s,\n" +
                    "\t\"aisle\": \"Baking\",\n" +
                    "\t\"parse\": %b\n" +
                    "}", title, value))
            .when()
                .post("mealplanner/geekbrains/shopping-list/items")
            .then()
                .log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();

        String body =
            given()
            .baseUri(getBaseUrl())
            .queryParam("hash", "a3da66460bfb7e62ea1c96cfa0b7a634a346ccbf")
            .queryParam("apiKey", getApiKey())
        .when()
            .get("mealplanner/geekbrains/shopping-list")
            .getBody().asString();

        assertThat(body.contains(title), is(!value));
    }

    @AfterEach
    void tearDown() {
        if(!id.isEmpty()) {
            given()
                .baseUri(getBaseUrl())
                .queryParam("hash", "a3da66460bfb7e62ea1c96cfa0b7a634a346ccbf")
                .queryParam("apiKey", getApiKey())
                .delete("mealplanner/geekbrains/shopping-list/items/{id}", id)
            .then()
                .statusCode(200);
        }
    }
}
