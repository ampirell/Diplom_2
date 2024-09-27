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

public class TestUser {
    private String accessToken;

    User user = new User( "a23n23@123.ru", "password","Username");

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
    @DisplayName("Create uniq user")
    public void createUser(){
        UserClient.create(user)
                .statusCode(200)
                .body("success", equalTo(true));
        UserClient.login(UserCredentials.from(user))
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Create user already exist")
    public void createUserExist(){
        UserClient.create(user)
                .statusCode(200);
        UserClient.login(UserCredentials.from(user))
                .statusCode(200);
        UserClient.create(user)
                .statusCode(403)
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Create user without name")
    public void createUserWithoutName(){
        User user1 = new User( "anp00@123.ru", "123456", null);
        UserClient.create(user1)
                .statusCode(403) .body("message", equalTo("Email, password and name are required fields"));
        UserClient.login(UserCredentials.from(user1))
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Create user without pass")
    public void createUserWithoutPass(){
        User user1 = new User( "anna12345@123.ru", null, "Niki");
        UserClient.create(user1)
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
        UserClient.login(UserCredentials.from(user1))
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));

    }
    @Test
    @DisplayName("Create user without email")
    public void createUserWithoutEmail(){
        User user1 = new User( null, "123456", "Niki");
        UserClient.create(user1)
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
        UserClient.login(UserCredentials.from(user1))
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));

    }

}
