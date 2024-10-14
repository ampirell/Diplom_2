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

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;

public class TestGetUsersOrders {

    User user = new User( "anna12345@12345678.ru", "password","Username");
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseURL.BASE_URL;
        UserClient.create(user)
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
    public void getUsersOrders(){
        OrdersClients.getListOfOrders(accessToken)
                .statusCode(200)
                .and()
                .body("orders._id", notNullValue());
    }
}
