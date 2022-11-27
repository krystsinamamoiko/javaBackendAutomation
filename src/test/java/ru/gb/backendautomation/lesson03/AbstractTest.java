package ru.gb.backendautomation.lesson03;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AbstractTest {
    static Properties prop = new Properties();
    private static InputStream configFile;
    private static String apiKey;
    private static String baseUrl;
    protected static ResponseSpecification responseSpecification;
    protected static RequestSpecification requestJsonSpecification;
    protected static RequestSpecification requestUrlencSpecification;

    @BeforeAll
    static void initTest() throws IOException {
        configFile = new FileInputStream("src/main/resources/my.properties");
        prop.load(configFile);

        apiKey =  prop.getProperty("apiKey");
        baseUrl= prop.getProperty("base_url");

        //for logging request and responses in Allure reporting
        // mvn clean test allure:serve
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured()
            .setRequestTemplate("http-request.ftl")
            .setResponseTemplate("http-response.ftl"));

        requestJsonSpecification = new RequestSpecBuilder()
            .addQueryParam("apiKey", apiKey)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

        requestUrlencSpecification = new RequestSpecBuilder()
            .addQueryParam("apiKey", apiKey)
            .setContentType(ContentType.URLENC)
            .log(LogDetail.ALL)
            .build();

        responseSpecification = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectStatusLine("HTTP/1.1 200 OK")
            .expectContentType(ContentType.JSON)
            .expectResponseTime(Matchers.lessThan(5000L))
            .log(LogDetail.ALL)
            //.expectHeader("Access-Control-Allow-Credentials", "true")
            .build();
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public RequestSpecification getRequestJsonSpecification(){
        return requestJsonSpecification;
    }

    public RequestSpecification getRequestUrlencSpecification(){
        return requestUrlencSpecification;
    }

    public static ResponseSpecification getResponseSpecification() {
        return responseSpecification;
    }
}
