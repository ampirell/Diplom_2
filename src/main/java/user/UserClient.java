package user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String USER_PATH = "/api/auth/";

    @Step
    public static ValidatableResponse create(User user){
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(USER_PATH +"register")
                .then();
    }
    @Step
    public static ValidatableResponse login(UserCredentials credentials) {
        return given()
                .header("Content-type", "application/json")
                .body(credentials)
                .when()
                .post(USER_PATH +"login")
                .then();
    }
    @Step
    public static ValidatableResponse delete(String accessToken) {
        return given()
                .auth().oauth2(accessToken)
                .and()
                .header("Content-type", "application/json")
                .when()
                .delete(USER_PATH +"user")
                .then();
    }

    @Step
    public static ValidatableResponse update(String accessToken,User user) {
        return given()
                .auth().oauth2(accessToken)
                .and()
                .body(user)
                .header("Content-type", "application/json")
                .when()
                .patch(USER_PATH +"user")
                .then();
    }
    @Step
    public static ValidatableResponse updateWithoutAuth(User user) {
        return given()
                .body(user)
                .header("Content-type", "application/json")
                .when()
                .patch(USER_PATH +"user")
                .then();
    }
}
