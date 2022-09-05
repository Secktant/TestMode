package data;

import static com.codeborne.selenide.Selenide.$x;

public class Login {
    public static void login(UserInfo user) {
        $x("//input[@name = \"login\"]").setValue(user.getLogin());
        $x("//input[@name = \"password\"]").setValue(user.getPassword());
        $x("//button[@data-test-id = \"action-login\"]").click();
    }
}