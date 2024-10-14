import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import orders.Orders;
import orders.OrdersClients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.BaseURL;
import user.User;
import user.UserClient;
import user.UserCredentials;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class TestCreateOrders {
    private List<String> ingredients = List.of("61c0c5a71d1f82001bdaaa6d");

    Orders order = new Orders(ingredients);
    User user = new User( "a1n1@1234567.ru", "password","Username");
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseURL.BASE_URL;
        ValidatableResponse createResponse = UserClient.create(user)
                .statusCode(200);
        ValidatableResponse loginResponse = UserClient.login(UserCredentials.from(user))
                .statusCode(200);
        String accessTokenFromResponse = loginResponse.extract()
                .path("accessToken");
        accessToken= accessTokenFromResponse.substring(7);
    }

    @After
    public void deleteUser(){
        UserClient.delete(accessToken);
    }

    @Test
    @DisplayName("Create order without authorisation and correct ingredients")

    public void createOrderWithIngredientsAndWithoutAuth(){
        List<String> ingredients = List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70");
        Orders order = new Orders(ingredients);
        OrdersClients.create(order)
            .statusCode(401)
            .body("message", equalTo("You should be authorised"));

}

    @Test
    @DisplayName("Create order with authorisation and correct ingredients")
    public void createOrderWithIngredientsAndAuth(){
        List<String> ingredients = List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70");
        Orders order = new Orders(ingredients);
        OrdersClients.createWithAuth(accessToken, order)
                .statusCode(200)
                .body("success", equalTo(true))
                .and()
                .body("order.number", notNullValue())
                .and()
                .body("name", notNullValue());;
    }
    @Test
    @DisplayName("Create order with authorisation and without ingredients")

    public void createOrderWithoutIngredientsAndAuth(){
        Orders order = new Orders(null);
        OrdersClients.createWithAuth(accessToken, order)
                .statusCode(400)
                .body("message", equalTo("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("Create order without authorisation and without ingredients")

    public void createOrderWithoutIngredientsAndWithoutAuth(){
        Orders order = new Orders(null);
        OrdersClients.create( order)
                .statusCode(401)
                .body("message", equalTo("You should be authorised"));

    }
    @Test
    @DisplayName("Create order with authorisation and incorrect ingredients")

    public void createOrderWithWrongIngredientsAndAuth(){
        List<String> ingredients = List.of("wrongHash");
        Orders order = new Orders(ingredients);
        OrdersClients.createWithAuth(accessToken, order)
                .statusCode(500);
    }
    @Test
    @DisplayName("Create order without authorisation and incorrect ingredients")

    public void createOrderWithWrongIngredientsAndWithoutAuth(){
        List<String> ingredients = List.of("wrongHash");
        Orders order = new Orders(ingredients);
        OrdersClients.create( order)
                .statusCode(500);
    }

}
