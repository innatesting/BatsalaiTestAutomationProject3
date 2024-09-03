package com.epam.automation.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PetsStoreTests {

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @Test
    public void verifyCreateUser() {
        given()
            .contentType("application/json")
            .body("{ \"id\": 1, \"username\": \"batsalai\", \"firstName\": \"Inna\", \"lastName\": \"Batsalai\", \"email\": \"inna@example.com\", \"password\": \"password\", \"phone\": \"1234567890\", \"userStatus\": 1 }")
        .when()
            .post("/user")
        .then()
            .statusCode(200)
            .body("code", equalTo(200));
    }
    
    @Test
    public void verifyUserLogin() {
        given()
            .queryParam("username", "batsalai")
            .queryParam("password", "password")
        .when()
            .get("/user/login")
        .then()
            .statusCode(200)
            .body("message", containsString("logged in user session:"));
    }

    @Test
    public void verifyCreatingListOfUsers() {
        List<Map<String, Object>> users = Arrays.asList(
            new HashMap<String, Object>() {{
                put("id", 2);
                put("username", "user1");
                put("firstName", "Example");
                put("lastName", "User");
                put("email", "exampleuser1@example.com");
                put("password", "password1");
                put("phone", "1111111111");
                put("userStatus", 1);
            }},
            new HashMap<String, Object>() {{
                put("id", 3);
                put("username", "user2");
                put("firstName", "Example");
                put("lastName", "User");
                put("email", "exampleuser2@example.com");
                put("password", "password2");
                put("phone", "2222222222");
                put("userStatus", 1);
            }}
        );

        given()
            .contentType("application/json")
            .body(users)
        .when()
            .post("/user/createWithArray")
        .then()
            .statusCode(200)
            .body("message", equalTo("ok"));
    }

    @Test
    public void verifyUserLogout() {
        given()
        .when()
            .get("/user/logout")
        .then()
            .statusCode(200)
            .body("message", containsString("ok"));
    }

    @Test
    public void verifyAddingNewPet() {
        given()
            .contentType("application/json")
            .body("{ \"id\": 1, \"category\": { \"id\": 1, \"name\": \"Dogs\" }, \"name\": \"Rex\", \"photoUrls\": [\"url1\"], \"tags\": [{ \"id\": 1, \"name\": \"tag1\" }], \"status\": \"available\" }")
        .when()
            .post("/pet")
        .then()
            .statusCode(200)
            .body("name", equalTo("Rex"));
    }

    @Test
    public void verifyUpdatingPetsImage() {
        given()
            .contentType("application/json")
            .body("{ \"photoUrls\": [\"newUrl1\"] }")
        .when()
            .put("/pet/1")
        .then()
            .statusCode(200);
    }

    @Test
    public void verifyUpdatingPetsNameAndStatus() {
        given()
            .contentType("application/json")
            .body("{ \"name\": \"Max\", \"status\": \"sold\" }")
        .when()
            .put("/pet/1")
        .then()
            .statusCode(200)
            .body("name", equalTo("Max"))
            .body("status", equalTo("sold"));
    }

    @Test
    public void verifyDeletingPet() {
        given()
            .contentType("application/json")
        .when()
            .delete("/pet/1")
        .then()
            .statusCode(200);
    }
}