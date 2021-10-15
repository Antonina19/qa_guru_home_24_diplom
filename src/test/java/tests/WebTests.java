package tests;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class WebTests {
    @Test
    void emptyBasket() {
        step("Открытие страницы 'mcdonalds.ru'", () -> {
            open("https://mcdonalds.ru/");
        });
        step("Отклонение уведомнений и выбор города по-умолчанию", () -> {
            $(".confirm-push-notifications__reject").click();
            $(".el-button").shouldHave(text("Да, верно")).click();
        });
        step("Наведение на кнопку корзины", () -> {
            $(".mini-cart_empty").hover();
        });
        step("Подтверждаем, что корзина пустая", () -> {
            $(".cart-popup__title").shouldHave(text("Ваша корзина пока пуста"));
        });
    }

    @CsvSource({
            "Меню Макдоналдс,Меню,Меню Макдоналдс и МакКафе в России",
            "Меню МакКафе,Меню,Меню МакКафе в России"
    })
    @ParameterizedTest
    void openPageMenu(String textLink, String textHead, String expectedTitle) {
        step("Открытие страницы 'mcdonalds.ru'", () -> {
            open("https://mcdonalds.ru/");
        });
//        $(".confirm-push-notifications__reject").click();
//        $(".el-button").shouldHave(Condition.text("Да, верно")).click();
        step("Кликаем по пункту меню" + textLink, () -> {
            $(By.linkText(textLink)).click();
        });
        step("Проверяем, что открытая страница имеет title - " + expectedTitle + " , и head - " + textHead, () -> {
            $(".page-heading__title").shouldHave(text(textHead));
            String actualTitle = title();
            assertThat(actualTitle).isEqualTo(expectedTitle);
        });
    }

    @Test
    void openInfoAboutProduct() {
        step("Открытие страницы 'mcdonalds.ru'", () -> {
            open("https://mcdonalds.ru/");
        });
        step("Отклонение уведомнений и выбор города по-умолчанию", () -> {
            $(".confirm-push-notifications__reject").click();
            $(".el-button").shouldHave(Condition.text("Да, верно")).click();
        });
        $(".menu-categories-outer").scrollTo();
        step("Переключение на вкладку 'Картофель и стартеры'", () -> {
            $(by("data-section-selector", "#section_34")).click();
        });
        step("Открываем продукт 'Большой картофель Фри' ", () -> {
            $(byId("product-french-fries-big")).click();
        });
        step("Подтверждаем, что открылось окно 'Картофель Фри Большой'", () -> {
            $(".product-settings__name").shouldHave(text("Картофель Фри Большой"));
        });
        step("Кликаем на 'О продукте' ", () -> {
            $(".product-settings__info-button").click();
        });
        step("Подтверждаем, что есть заголовок 'Пищевая ценность'", () -> {
            $(".product-information__title").shouldHave(text("Пищевая ценность"));
        });
    }


}

