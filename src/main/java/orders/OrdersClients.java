package orders;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import user.User;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OrdersClients {
    private static final String USER_PATH = "/api/orders";

    @Step
    public static ValidatableResponse create(Orders orders){
        return given()
                .header("Content-type", "application/json")
                .body(orders)
                .when()
                .post(USER_PATH)
                .then();
    }
    @Step
    public static ValidatableResponse createWithAuth(String accessToken, Orders orders){
        return given()
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .body(orders)
                .when()
                .post(USER_PATH)
                .then();
    }

    @Step
    public static ValidatableResponse getListOfOrders(String accessToken){
        return given()
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .when()
                .get(USER_PATH)
                .then();
    }


}
