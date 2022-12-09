package ru.gb.backendautomation.lesson03;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.gb.backendautomation.model.complexSearch.ComplexSearchResponse;
import ru.gb.backendautomation.model.complexSearch.Nutrient;
import ru.gb.backendautomation.model.complexSearch.Result;

import java.util.Random;

public class SpoonAcularGetTest extends AbstractTest {

    @Test
    @DisplayName("Verify a case with no parameters")
    void testComplexSearchNoParams() {
        ComplexSearchResponse complexSearchItem =
            given()
                .spec(getRequestJsonSpecification())
            .when()
                .get(getBaseUrl() + "recipes/complexSearch")
            .then()
                .spec(getResponseSpecification())
                .extract()
                .response()
                .body()
                .as(ComplexSearchResponse.class);

        assertThat(complexSearchItem.getOffset(), is(0));
    }

    @Test
    @DisplayName("Verify a case with a positive 'offset' parameter value")
    void testComplexSearchPositiveOffset() {
        int randomOffset = new Random().nextInt(5) + 1;
        ComplexSearchResponse complexSearchItem =
            given()
                .spec(getRequestJsonSpecification())
                .param("offset", randomOffset)
            .when()
                .get(getBaseUrl() + "recipes/complexSearch")
            .then()
                .spec(getResponseSpecification())
                .extract()
                .response()
                .body()
                .as(ComplexSearchResponse.class);

        assertThat(complexSearchItem.getOffset(), is(randomOffset));
    }

    @Test
    @DisplayName("Verify a case with a positive 'number' parameter value")
    void testComplexSearchPositiveNumber() {
        int randomNumber = new Random().nextInt(10) + 1;
        ComplexSearchResponse complexSearchItem =
            given()
                .spec(getRequestJsonSpecification())
                .param("number", randomNumber)
            .when()
                .get(getBaseUrl() + "recipes/complexSearch")
            .then()
                .spec(getResponseSpecification())
                .extract()
                .response()
                .body()
                .as(ComplexSearchResponse.class);

        assertThat(complexSearchItem.getNumber(), is(randomNumber));
    }

    @Test
    @DisplayName("Verify a case with a negative 'number' parameter value")
    void testComplexSearchNegativeNumber() {
        int randomNumber = - new Random().nextInt(10);
        ComplexSearchResponse complexSearchItem =
            given()
                .spec(getRequestJsonSpecification())
                .param("number", randomNumber)
            .when()
                .get(getBaseUrl() + "recipes/complexSearch")
            .then()
                .spec(getResponseSpecification())
                .extract()
                .response()
                .body()
                .as(ComplexSearchResponse.class);

        assertThat(complexSearchItem.getNumber(), is(1));
    }

    @Test
    @DisplayName("Verify filtration by a 'maxProtein' parameter")
    void testComplexSearchMaxProteinFiltration() {
        Integer totalNumberData =
            given()
                .spec(getRequestJsonSpecification())
            .when()
                .get(getBaseUrl() + "recipes/complexSearch")
            .then()
                .spec(getResponseSpecification())
                .extract()
                .response()
                .body()
                .as(ComplexSearchResponse.class)
                .getTotalResults();

        int randomMaxProteinValue = new Random().nextInt(3);
        ComplexSearchResponse filteredComplexSearchItem =
            given()
                .spec(getRequestJsonSpecification())
                .param("maxProtein", randomMaxProteinValue)
            .when()
                .get(getBaseUrl() + "recipes/complexSearch")
            .then()
                .spec(getResponseSpecification())
                .extract()
                .response()
                .body()
                .as(ComplexSearchResponse.class);

        assertThat(totalNumberData > filteredComplexSearchItem.getTotalResults(), is(true));

        for(Result result : filteredComplexSearchItem.getResults()) {
            for(Nutrient nutrient : result.getNutrition().getNutrients()) {
                assertThat(nutrient.getName(), is("Protein"));
                assertThat(nutrient.getAmount() < randomMaxProteinValue, is(true));
            }
        }
    }
}
