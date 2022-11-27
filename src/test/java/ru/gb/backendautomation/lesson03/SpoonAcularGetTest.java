package ru.gb.backendautomation.lesson03;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class SpoonAcularGetTest extends AbstractTest {

    @Test
    @DisplayName("Verify a case with no parameters")
    void testComplexSearchNoParams() {
        Integer offsetData = given()
            .baseUri(getBaseUrl())
            .param("apiKey", getApiKey())
        .when()
            .get("recipes/complexSearch")
        .then()
            .statusCode(200)
            .extract().jsonPath().get("offset");

        assertThat(offsetData, is(0)); 
    }

    @Test
    @DisplayName("Verify a case with a positive 'offset' parameter value")
    void testComplexSearchPositiveOffset() {
        int randomOffset = new Random().nextInt(5) + 1;
        Integer offsetData =
            given()
                .baseUri(getBaseUrl())
                .param("apiKey", getApiKey())
                .param("offset", randomOffset)
            .when()
                .get("recipes/complexSearch")
            .then()
                .statusCode(200)
                .extract().jsonPath().get("offset");

        assertThat(offsetData, is(randomOffset));
    }

    @Test
    @DisplayName("Verify a case with a positive 'number' parameter value")
    void testComplexSearchPositiveNumber() {
        int randomNumber = new Random().nextInt(10) + 1;
        Integer offsetData =
            given()
                .baseUri(getBaseUrl())
                .param("apiKey", getApiKey())
                .param("number", randomNumber)
            .when()
                .get("recipes/complexSearch")
            .then()
                .statusCode(200)
                .extract().jsonPath().get("number");

        assertThat(offsetData, is(randomNumber));
    }

    @Test
    @DisplayName("Verify a case with a negative 'number' parameter value")
    void testComplexSearchNegativeNumber() {
        int randomNumber = - new Random().nextInt(10);
        Integer offsetData =
            given()
                .baseUri(getBaseUrl())
                .param("apiKey", getApiKey())
                .param("number", randomNumber)
            .when()
                .get("recipes/complexSearch")
            .then()
                .statusCode(200)
                .extract().jsonPath().get("number");

        assertThat(offsetData, is(1));
    }

    @Test
    @DisplayName("Verify filtration by a 'maxProtein' parameter")
    void testComplexSearchMaxProteinFiltration() {
        Integer totalNumberData =
            given()
                .baseUri(getBaseUrl())
                .param("apiKey", getApiKey())
            .when()
                .get("recipes/complexSearch")
            .then()
                .statusCode(200)
                .extract().jsonPath().get("totalResults");

        int randomMaxProteinValue = new Random().nextInt(30);
        Integer filteredNumberData =
            given()
                .baseUri(getBaseUrl())
                .param("apiKey", getApiKey())
                .param("maxProtein", randomMaxProteinValue)
            .when()
                .get("recipes/complexSearch")
            .then()
                .statusCode(200)
                .extract().jsonPath().get("totalResults");

        assertThat(totalNumberData > filteredNumberData, is(true));
    }
}
