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

public class TestUpdate {

    private String accessToken;

    User user = new User( "a4n3@123.ru", "password","Username");

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
    @DisplayName("update email with auth")
    public void updateUsersEmailWithAuth(){
        User userNewCred = new User( "annan@123.ru", null, null);
        UserClient.update(accessToken,userNewCred)
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("update Name  with auth")
    public void updateUsersNameWithAuth(){
        User userNewCred = new User( null, null, "Niki");
        ValidatableResponse updateResponse = UserClient.update(accessToken, userNewCred)
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("update password  with auth")
    public void updateUsersPassWithAuth(){
        User userNewCred = new User( null, "123456", null);
        UserClient.update(accessToken, userNewCred)
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("update email without auth")
    public void updateUsersEmailWithoutAuth(){
        User userNewCred = new User( "anna@123.ru", null, null);
        ValidatableResponse updateResponse = UserClient.updateWithoutAuth( userNewCred)
                .statusCode(401)
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("update password without auth")
    public void updateUsersPassWithoutAuth(){
        User userNewCred = new User(null, "123456", null);
        ValidatableResponse updateResponse = UserClient.updateWithoutAuth( userNewCred)
                .statusCode(401)
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("update Name without auth")
    public void updateUsersNameWithoutAuth(){
        User userNewCred = new User( null, null, "Niki");
        ValidatableResponse updateResponse = UserClient.updateWithoutAuth(userNewCred)
                .statusCode(401)
                .body("message", equalTo("You should be authorised"));
    }

}
