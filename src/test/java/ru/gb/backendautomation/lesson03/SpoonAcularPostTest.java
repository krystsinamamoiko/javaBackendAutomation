package ru.gb.backendautomation.lesson03;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SpoonAcularPostTest extends AbstractTest {

    private static final String UNSUPPORTED_LANGUAGE = "fr";
    private static final String ERROR_MESSAGE = "Error report";

    private enum Cuisines {
        MEDITERRANEAN("Mediterranean"),
        EUROPEAN("European"),
        ITALIAN("Italian"),
        AMERICAN("American");

        public final String title;

        Cuisines(String title) {
            this.title = title;
        }
    }

    @Test
    @DisplayName("Verify case with no parameters")
    void testClassifyCuisineNoParams() {
        Response response =
            given()
                .baseUri(getBaseUrl())
                .queryParam("apiKey", getApiKey())
                .contentType(ContentType.URLENC)
            .when()
                .post("recipes/cuisine")
            .then()
                .statusCode(200)
                .extract()
                .response();

        assertThat(response.header("Content-Type"), is("application/json"));
        assertThat(response.jsonPath().get("cuisine"), is(Cuisines.MEDITERRANEAN.title));
        assertThat(response.jsonPath().get("confidence"), is(0.0f));
        assertThat(response.jsonPath().getList("cuisines", String.class),
            is(new ArrayList<>(Arrays.asList(Cuisines.MEDITERRANEAN.title, Cuisines.EUROPEAN.title, Cuisines.ITALIAN.title))));
    }

    @Test
    @DisplayName("Verify case with a valid title parameter")
    void testClassifyCuisineValidTitleParam() {
        Response response =
            given()
                .baseUri(getBaseUrl())
                .queryParam("apiKey", getApiKey())
                .contentType(ContentType.URLENC)
                .formParam("title","burger")
            .when()
                .post("recipes/cuisine")
            .then()
                .statusCode(200)
                .log()
                .all()
                .extract()
                .response();

        assertThat(response.header("Content-Type"), is("application/json"));
        assertThat(response.jsonPath().get("cuisine"), is(Cuisines.AMERICAN.title));
        assertThat(response.jsonPath().get("confidence"), is(0.85f));
        assertThat(response.jsonPath().getList("cuisines", String.class),
            is(new ArrayList<>(Arrays.asList(Cuisines.AMERICAN.title))));
    }

    @Test
    @DisplayName("Verify case with a non existing title parameter")
    void testClassifyCuisineNonExistingTitleParam() {
        Response response =
            given()
                .baseUri(getBaseUrl())
                .queryParam("apiKey", getApiKey())
                .contentType(ContentType.URLENC)
                .formParam("title","something unexciting")
            .when()
                .post("recipes/cuisine")
            .then()
                .statusCode(200)
                .log()
                .all()
                .extract()
                .response();

        assertThat(response.header("Content-Type"), is("application/json"));
        assertThat(response.jsonPath().get("cuisine"), is(Cuisines.MEDITERRANEAN.title));
        assertThat(response.jsonPath().get("confidence"), is(0.0f));
        assertThat(response.jsonPath().getList("cuisines", String.class),
            is(new ArrayList<>(Arrays.asList(Cuisines.MEDITERRANEAN.title, Cuisines.EUROPEAN.title, Cuisines.ITALIAN.title))));
    }

    @ParameterizedTest
    @ValueSource(strings = {"en", "de"})
    @DisplayName("Verify the given language support")
    void testClassifyCuisineNotValidLanguageParam(String language) {
        Response response = given()
            .baseUri(getBaseUrl())
            .queryParam("apiKey", getApiKey())
            .queryParam("language", language)
            .contentType(ContentType.URLENC)
            .when()
            .post("recipes/cuisine")
            .then()
            .extract()
            .response();

        assertThat(response.header("Content-Type"), is("application/json"));
        assertThat(response.jsonPath().get("cuisine"), is(Cuisines.MEDITERRANEAN.title));
        assertThat(response.jsonPath().get("confidence"), is(0.0f));
        assertThat(response.jsonPath().getList("cuisines", String.class),
            is(new ArrayList<>(Arrays.asList(Cuisines.MEDITERRANEAN.title, Cuisines.EUROPEAN.title, Cuisines.ITALIAN.title))));
    }

    @Test
    @DisplayName("Verify behaviour in case of not supported language")
    void testClassifyCuisineNotValidLanguageParam() {
        Response response =
            given()
                .baseUri(getBaseUrl())
                .queryParam("apiKey", getApiKey())
                .queryParam("language", UNSUPPORTED_LANGUAGE)
                .contentType(ContentType.URLENC)
                .log()
                .all()
            .when()
                .post("recipes/cuisine")
            .then()
                .log()
                .all()
                .extract()
                .response();

        assertThat(response.header("Content-Type"), is("text/html;charset=utf-8"));
        assertThat(response.getBody().asString().contains(ERROR_MESSAGE), is(true));
    }
}
