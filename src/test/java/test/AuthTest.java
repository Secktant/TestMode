package test;

import data.DataGenerator;
import data.Login;
import data.UserApi;
import data.UserInfo;
import org.junit.jupiter.api.AfterEach;;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class AuthTest {
    private static UserInfo activeUser;
    private static UserInfo blockedUser;

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        activeUser = DataGenerator
                .RegistrationDataGenerator
                .generateData(UserInfo.STATUS_ACTIVE);

        blockedUser = DataGenerator.
                RegistrationDataGenerator.
                generateData(UserInfo.STATUS_BLOCKED);
    }

    @AfterEach
    void tearDown() {
        activeUser = null;
        blockedUser = null;
    }

    @Test
    void authUserActiveStatus() {
        UserApi.postUser(activeUser);
        Login.login(activeUser);
        $x("//h2[contains(text(), \"Личный кабинет\")]").should(visible);
    }

    @Test
    void authUserBlockedStatus() {
        UserApi.postUser(blockedUser);
        Login.login(blockedUser);
        $(".notification__title").shouldHave(text("Ошибка"));
        $(".notification__content").shouldHave(text("Пользователь заблокирован"));
    }

    @Test
    void authUnregisteredUser() {
        activeUser = DataGenerator.RegistrationDataGenerator.generateData(UserInfo.STATUS_ACTIVE);
        Login.login(activeUser);
        $(".notification__title").shouldHave(text("Ошибка"));
        $(".notification__content").shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void authWithEmptyFields() {
        activeUser = new UserInfo(" ", " ", UserInfo.STATUS_ACTIVE);
        Login.login(activeUser);
        $x("//*[@data-test-id = \"login\"]//span[@class = \"input__sub\"]").
                shouldHave(text("Поле обязательно для заполнения"));
        $x("//*[@data-test-id = \"password\"]//span[@class = \"input__sub\"]").
                shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void authUserWithIncorrectPassword() {
        UserApi.postUser(activeUser);
        activeUser = new UserInfo(activeUser.getLogin(), DataGenerator.RegistrationDataGenerator.generatePassword(), UserInfo.STATUS_ACTIVE);
        Login.login(activeUser);
        $(".notification__title").shouldHave(text("Ошибка"));
        $(".notification__content").shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void authUserWithIncorrectLogin() {
        UserApi.postUser(activeUser);
        activeUser = new UserInfo(DataGenerator.RegistrationDataGenerator.generateLogin(), activeUser.getPassword(), UserInfo.STATUS_ACTIVE);
        Login.login(activeUser);
        $(".notification__title").shouldHave(text("Ошибка"));
        $(".notification__content").shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void authUserWithIncorrectPasswordBlockedStatus() {
        UserApi.postUser(blockedUser);
        blockedUser = new UserInfo(blockedUser.getLogin(), DataGenerator.RegistrationDataGenerator.generatePassword(), UserInfo.STATUS_BLOCKED);
        Login.login(blockedUser);
        $(".notification__title").shouldHave(text("Ошибка"));
        $(".notification__content").shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void authUserWithIncorrectLoginBlockedStatus() {
        UserApi.postUser(blockedUser);
        blockedUser = new UserInfo(DataGenerator.RegistrationDataGenerator.generateLogin(), blockedUser.getPassword(), UserInfo.STATUS_BLOCKED);
        Login.login(blockedUser);
        $(".notification__title").shouldHave(text("Ошибка"));
        $(".notification__content").shouldHave(text("Неверно указан логин или пароль"));
    }
}