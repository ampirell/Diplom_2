import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.BaseURL;
import user.User;
import user.UserClient;
import user.UserCredentials;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertTrue;

public class TestLogin {
    private String accessToken;

    User user = new User( "a2n2@123.ru", "password","Username");

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseURL.BASE_URL;
    }

    @After
    public void deleteUser(){
        ValidatableResponse loginResponse = UserClient.login(UserCredentials.from(user))
                .statusCode(200);
        String accessTokenFromResponse = loginResponse.extract().path("accessToken");
        accessToken= accessTokenFromResponse.substring(7);
        UserClient.delete(accessToken);
    }

    @Test
    @DisplayName("login exist user")
    public void loginUser(){
        UserClient.create(user)
                .statusCode(200);
        UserClient.login(UserCredentials.from(user))
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("login with incorrect param")
    public void loginUserNotExist(){
        UserClient.create(user)
                .statusCode(200);
        UserClient.login(UserCredentials.from(user))
                .statusCode(200);
        UserCredentials newCredWithWrongPass = new UserCredentials("anna12345@123.ru", "knwe");
        UserClient.login(newCredWithWrongPass)
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

}
