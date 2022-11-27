package ru.gb.backendautomation.model.complexSearch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.gb.backendautomation.model.complexSearch.Nutrition;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {
    private Integer id;
    private String title;
    private String image;
    private String imageType;
    private Nutrition nutrition;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/*    @Test
    void testComplexSearchQuery() {
        String searchTitle = "pasta";
        int randomOffset = new Random().nextInt(5);
        List<Recipe> resultList = given()
            .baseUri(getBaseUrl())
            .param("apiKey", getApiKey())
            .param("query", searchTitle)
            .when()
            .get("recipes/complexSearch")
            .then()
            .log().all()
            .statusCode(200)
            .extract().jsonPath().getList("results", Recipe.class);

            Recipe[] recipes = given().when().get("person/").as(Recipe[].class);

        for(Recipe recipe : resultList) {
            assertThat(recipe.getTitle().toLowerCase().contains(searchTitle), is(true));
        }
    }*/
}
