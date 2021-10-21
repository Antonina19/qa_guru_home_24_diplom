package tests;

import annotations.JiraIssue;
import annotations.JiraIssues;
import annotations.Layer;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
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

@Layer("UI tests")
public class WebTests extends TestBase {
    @Test
    @Owner("antonina")
    @Story("Проверка корзины")
    @Tags({@Tag("web"), @Tag("regress")})
    @JiraIssues({@JiraIssue("HOMEWORK-256")})
    @DisplayName("Проверка, что по-умолчанию корзина пустая")
    void emptyBasket() {
        step("Открытие страницы 'mcdonalds.ru'", () -> {
            open("https://mcdonalds.ru/");
        });
        step("Наведение на кнопку корзины", () -> {
            $(".mini-cart_empty").hover();
        });
        step("Подтверждаем, что корзина пустая", () -> {
            $(".cart-popup__title").shouldHave(text("Ваша корзина пока пуста"));
        });
    }

    @ParameterizedTest
    @CsvSource({
            "Меню Макдоналдс,Меню,Меню Макдоналдс и МакКафе в России",
            "Меню МакКафе,Меню,Меню МакКафе в России"
    })
//    @ParameterizedTest
    @Owner("antonina")
    @Story("Отрытие страниц меню")
    @Tags({@Tag("web"), @Tag("regress")})
    @JiraIssues({@JiraIssue("HOMEWORK-256")})
    @DisplayName("Проверка, что открываются страницы меню")
    void openPageMenu(String textLink, String textHead, String expectedTitle) {
        step("Открытие страницы 'mcdonalds.ru'", () -> {
            open("https://mcdonalds.ru/");
        });
        step("Кликаем по пункту меню" + textLink, () -> {
            $(".delivery").scrollTo();
            $(By.linkText(textLink)).click();
        });
        step("Проверяем, что открытая страница имеет title - " + expectedTitle + " , и head - " + textHead, () -> {
            $(".page-heading__title").shouldHave(text(textHead));
            String actualTitle = title();
            assertThat(actualTitle).isEqualTo(expectedTitle);
        });
    }

    @Test
    @Owner("antonina")
    @Story("Открытие информации о продукте")
    @Tags({@Tag("web"), @Tag("regress")})
    @JiraIssues({@JiraIssue("HOMEWORK-256")})
    @DisplayName("Открытие окна информации о продукте")
    void openInfoAboutProduct() {
        step("Открытие страницы 'mcdonalds.ru'", () -> {
            open("https://mcdonalds.ru/");
        });
        step("Переключение на вкладку 'Картофель и стартеры'", () -> {
            $(".menu-categories-outer").scrollTo();
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

    @Test
    @Owner("antonina")
    @Story("Страница 'Франчайзинг'")
    @Tags({@Tag("web"), @Tag("regress")})
    @JiraIssues({@JiraIssue("HOMEWORK-256")})
    @DisplayName("Проверка, что открывается страница 'Франчайзинг'")
    void openPageFranchising() {
        step("Открытие страницы 'mcdonalds.ru'", () -> {
            open("https://mcdonalds.ru/");
        });
        step("Кликаем по ссылке 'Франчайзинг'", () -> {
            $(By.linkText("Франчайзинг")).click();
        });
        step("Подтверждаем, что есть заголовок 'Франчайзинг'", () -> {
            $(".contacts__heading-title").shouldHave(text("Франчайзинг"));
        });
    }

    @ParameterizedTest
    @CsvSource({
            "0,Марк Карена,Генеральный директор «Макдоналдс» в России",
            "1,Радомир Глащак,Вице-президент по доставке «Макдоналдс» в России",
            "2,Дарья Назаркина,Вице-президент по маркетингу",
            "3,Олег Пароев,Вице-президент по трансформации «Макдоналдс» в России",
            "4,Анна Патрунина,Вице-президент по производству «Макдоналдс» в России",
            "5,Карина Погосова,Старший вице-президент по развитию и франчайзингу «Макдоналдс» в России",
            "6,Екатерина Серебреникова,Вице-президент по развитию людских ресурсов и обучению «Макдоналдс» в России",
            "7,Антон Чванов,Руководитель Финансового Департамента «Макдоналдс» в России"
    })
//    @ParameterizedTest
    @Owner("antonina")
    @Story("Страница 'Информация о компании'")
    @Tags({@Tag("web"), @Tag("regress")})
    @JiraIssues({@JiraIssue("HOMEWORK-256")})
    @DisplayName("Проверка, информации о руководстве компании")
    void openPageAboutCompanyManagement(int n, String name, String position) {
        step("Открытие страницы 'mcdonalds.ru'", () -> {
            open("https://mcdonalds.ru/");
        });
        step("Кликаем по ссылке 'Макдоналдс в России'", () -> {
            $(By.linkText("Макдоналдс в России")).click();
        });
        step("Подтверждаем, что есть заголовок 'Информация о компании'", () -> {
            $(".contacts__heading-title").shouldHave(text("Информация о компании"));
        });
        step("Кликаем по имени участника руководства", () -> {
            $$(".mcd__russia-item-wrapper").get(n).click();
        });
        step("Подтверждаем, информацию о руководителе", () -> {
            $(".mcd__russia-item-name").shouldHave(text(name));
            $(".mcd__russia-item-position").shouldHave(text(position));
        });
    }
}

