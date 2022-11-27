package ru.gb.backendautomation.lesson03;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.gb.backendautomation.model.complexSearch.ClassifyCuisineResponse;

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
        ClassifyCuisineResponse classifyCuisineResponse =
            given()
                .spec(getRequestUrlencSpecification())
            .when()
                .post(getBaseUrl() + "recipes/cuisine")
            .then()
                .spec(getResponseSpecification())
                .extract()
                .response()
                .body()
                .as(ClassifyCuisineResponse.class);

        assertThat(classifyCuisineResponse.getCuisine(), is(Cuisines.MEDITERRANEAN.title));
        assertThat(classifyCuisineResponse.getConfidence(), is(0.0));
        assertThat(classifyCuisineResponse.getCuisines().containsAll(new ArrayList<>(Arrays.asList(Cuisines.MEDITERRANEAN.title, Cuisines.EUROPEAN.title, Cuisines.ITALIAN.title))),
            is(true));
    }

    @Test
    @DisplayName("Verify case with a valid title parameter")
    void testClassifyCuisineValidTitleParam() {
        ClassifyCuisineResponse classifyCuisineResponse =
            given()
                .spec(getRequestUrlencSpecification())
                .formParam("title","burger")
            .when()
                .post(getBaseUrl() + "recipes/cuisine")
            .then()
                .spec(getResponseSpecification())
                .extract()
                .response()
                .body()
                .as(ClassifyCuisineResponse.class);

        assertThat(classifyCuisineResponse.getCuisine(), is(Cuisines.AMERICAN.title));
        assertThat(classifyCuisineResponse.getConfidence(), is(0.85));
        assertThat(classifyCuisineResponse.getCuisines().containsAll(new ArrayList<>(Arrays.asList(Cuisines.AMERICAN.title))),
            is(true));
    }

    @Test
    @DisplayName("Verify case with a non existing title parameter")
    void testClassifyCuisineNonExistingTitleParam() {
        ClassifyCuisineResponse classifyCuisineResponse =
            given()
                .spec(getRequestUrlencSpecification())
                .formParam("title","something unexciting")
            .when()
                .post(getBaseUrl() + "recipes/cuisine")
            .then()
                .spec(getResponseSpecification())
                .extract()
                .response()
                .body()
                .as(ClassifyCuisineResponse.class);

        assertThat(classifyCuisineResponse.getCuisine(), is(Cuisines.MEDITERRANEAN.title));
        assertThat(classifyCuisineResponse.getConfidence(), is(0.0));
        assertThat(classifyCuisineResponse.getCuisines().containsAll(new ArrayList<>(Arrays.asList(Cuisines.MEDITERRANEAN.title, Cuisines.EUROPEAN.title, Cuisines.ITALIAN.title))),
            is(true));
    }

    @ParameterizedTest
    @ValueSource(strings = {"en", "de"})
    @DisplayName("Verify the given language support")
    void testClassifyCuisineNotValidLanguageParam(String language) {
        ClassifyCuisineResponse classifyCuisineResponse =
            given()
                .spec(getRequestUrlencSpecification())
                .contentType(ContentType.URLENC)
            .when()
                .post(getBaseUrl() + "recipes/cuisine")
            .then()
                .spec(getResponseSpecification())
                .extract()
                .response()
                .body()
                .as(ClassifyCuisineResponse.class);

        assertThat(classifyCuisineResponse.getCuisine(), is(Cuisines.MEDITERRANEAN.title));
        assertThat(classifyCuisineResponse.getConfidence(), is(0.0));
        assertThat(classifyCuisineResponse.getCuisines().containsAll(new ArrayList<>(Arrays.asList(Cuisines.MEDITERRANEAN.title, Cuisines.EUROPEAN.title, Cuisines.ITALIAN.title))),
            is(true));
    }

    @Test
    @DisplayName("Verify behaviour in case of not supported language")
    void testClassifyCuisineNotValidLanguageParam() {
        Response response =
            given()
                .spec(getRequestUrlencSpecification())
                .queryParam("language", UNSUPPORTED_LANGUAGE)
            .when()
                .post(getBaseUrl() + "recipes/cuisine")
            .then()
                .contentType(ContentType.HTML)
                .extract()
                .response();

        assertThat(response.getBody().asString().contains(ERROR_MESSAGE), is(true));
    }
}
