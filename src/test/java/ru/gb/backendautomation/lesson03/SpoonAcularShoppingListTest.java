package ru.gb.backendautomation.lesson03;

import io.restassured.http.ContentType;
import static org.hamcrest.CoreMatchers.is;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.gb.backendautomation.model.shoppingList.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

public class SpoonAcularShoppingListTest extends AbstractTest {

    private Integer id = 0;

    @ParameterizedTest
    @DisplayName("Verify behaviour depending on 'parse' value")
    @CsvSource({"Something specific for false, false", "Something specific for true, true"})
    void testAddToShoppingList(String title, Boolean value) {
        ShoppingListRequest shoppingListRequest = new ShoppingListRequestBuilder()
            .setItem(title)
            .setAisle("Baking")
            .setParse(value)
            .build();

        id = given()
                .queryParam("hash", "a3da66460bfb7e62ea1c96cfa0b7a634a346ccbf")
                .queryParam("apiKey", getApiKey())
                .contentType(ContentType.JSON)
                .body(shoppingListRequest)
            .when()
                .post(getBaseUrl() + "mealplanner/geekbrains/shopping-list/items")
            .then()
                .spec(getResponseSpecification())
                .extract()
                .jsonPath()
                .get("id");

        ShoppingListResponse shoppingListResponse =
            given()
            .queryParam("hash", "a3da66460bfb7e62ea1c96cfa0b7a634a346ccbf")
            .queryParam("apiKey", getApiKey())
        .when()
            .get(getBaseUrl() + "mealplanner/geekbrains/shopping-list")
        .then()
                .spec(getResponseSpecification())
                .extract()
                .response()
                .body()
                .as(ShoppingListResponse.class);

        boolean isExist = false;
        for(Aisle aisle : shoppingListResponse.getAisles()) {
            for(Item item : aisle.getItems()) {
                if(item.getId().equals(id) && item.getName().equals(title)) {
                    isExist = true;
                    break;
                }
            }
        }
        assertThat(isExist, is(!value));
    }

    @AfterEach
    void tearDown() {
        if(!id.equals(0)) {
            given()
                .queryParam("hash", "a3da66460bfb7e62ea1c96cfa0b7a634a346ccbf")
                .queryParam("apiKey", getApiKey())
                .delete(getBaseUrl() + "mealplanner/geekbrains/shopping-list/items/{id}", id)
            .then()
                .spec(getResponseSpecification());
       }
    }
}
