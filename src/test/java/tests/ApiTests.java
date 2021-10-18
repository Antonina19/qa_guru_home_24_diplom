package tests;

import allure.Layer;
import allure.Lead;
import allure.Microservice;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;

@Layer("rest")
@Lead("qa-lead")
@Owner("puliavinaav")
@Feature("User")
public class ApiTests extends TestBase {
    int productCount;
    int orderCount = 0;
    ArrayList<String> productName;
    ArrayList<String> promoName;
    Map<String, String> authorizationCookie;


    @Test
    @Story("Отображение товара в корзине")
    @Microservice("Billing")
    @Tags({@Tag("api"), @Tag("regress")})
    // @JiraIssues({@JiraIssue("HOMEWORK-238")}) - создать задачу
    @DisplayName("Проверяем, что добавленный товар отображается в корзине")
    void addProductInBasket() {
        step("Определяем адресс и получаем куки", () -> {
            authorizationCookie =
                    given()
                            .contentType("application/json;charset=UTF-8")
                            .body("{\"lat\":55.774487,\"long\":37.682377,\"components\":" +
                                    "[{\"kind\":\"country\",\"name\":\"Россия\"}," +
                                    "{\"kind\":\"province\",\"name\":\"Центральный федеральный округ\"}," +
                                    "{\"kind\":\"province\",\"name\":\"Москва\"}," +
                                    "{\"kind\":\"locality\",\"name\":\"Москва\"}," +
                                    "{\"kind\":\"street\",\"name\":\"Бакунинская улица\"}," +
                                    "{\"kind\":\"house\",\"name\":\"15\"}]}")
                            .when()
                            .put("https://mcdonalds.ru/api/address")
                            .then()
                            .statusCode(200)
                            .extract()
                            .cookies();
        });
        step("Добавляем товар в корзину и сохраняем количество товара", () -> {
            Response responce =
                    given()
                            .contentType("application/json; charset=UTF-8")
                            .cookies(authorizationCookie)
                            .body("{\"offerId\":910,\"quantity\":1,\"ingredientGroups\":[]}")
                            .when()
                            .put("https://mcdonalds.ru/api/basket/add")
                            .then()
                            .statusCode(200)
                            .body("basket.basketItems.offerId", hasItems(910))
                            .body("basket.basketItems.productCode", hasItems("mcflurry-delux-caramel-chocolate"))
                            .extract().response();
            productCount = responce.path("basket.itemCount");
        });
        step("Подтверждаем, что в корзине добавлен наш товар", () -> {
            given()
                    .contentType("application/json; charset=UTF-8")
                    .cookies(authorizationCookie)
                    .when()
                    .post("https://mcdonalds.ru/api/order/dryRun")
                    .then()
                    .statusCode(200)
                    .body("order.basket.items.offerId", hasItems(910))
                    .body("order.basket.items.productCode", hasItems("mcflurry-delux-caramel-chocolate"))
                    .body("order.basket.items.quantity", hasItems(productCount))
                    .extract().response();
        });
    }

    @Test
    @Story("Удаление товаров из корзины")
    @Microservice("Billing")
    @Tags({@Tag("api"), @Tag("regress")})
    // @JiraIssues({@JiraIssue("HOMEWORK-238")}) - создать задачу
    @DisplayName("Проверяем, что добавленный товар удаляется из корзины")
    void deleteAllProductFromBasket() {
        step("Определяем адрес и получаем куки", () -> {
            authorizationCookie =
                    given()
                            .contentType("application/json;charset=UTF-8")
                            .body("{\"lat\":55.774487,\"long\":37.682377,\"components\":" +
                                    "[{\"kind\":\"country\",\"name\":\"Россия\"}," +
                                    "{\"kind\":\"province\",\"name\":\"Центральный федеральный округ\"}," +
                                    "{\"kind\":\"province\",\"name\":\"Москва\"}," +
                                    "{\"kind\":\"locality\",\"name\":\"Москва\"}," +
                                    "{\"kind\":\"street\",\"name\":\"Бакунинская улица\"}," +
                                    "{\"kind\":\"house\",\"name\":\"15\"}]}")
                            .when()
                            .put("https://mcdonalds.ru/api/address")
                            .then()
                            .statusCode(200)
                            .extract()
                            .cookies();
        });
        step("Добавляем товар в корзину и сохраняем количество товара", () -> {
            Response responce =
                    given()
                            .contentType("application/json; charset=UTF-8")
                            .cookies(authorizationCookie)
                            .body("{\"offerId\":998,\"quantity\":1,\"ingredientGroups\":[]}")
                            .when()
                            .put("https://mcdonalds.ru/api/basket/add")
                            .then()
                            .statusCode(200)
                            .body("basket.basketItems.offerId", hasItems(998))
                            .body("basket.basketItems.productCode", hasItems("apple-slices"))
                            .extract().response();
        });
        step("Добавляем товар в корзину и сохраняем количество товара", () -> {
            Response responce =
                    given()
                            .contentType("application/json; charset=UTF-8")
                            .cookies(authorizationCookie)
                            .body("{\"offerId\":1001,\"quantity\":2,\"ingredientGroups\":[]}")
                            .when()
                            .put("https://mcdonalds.ru/api/basket/add")
                            .then()
                            .statusCode(200)
                            .body("basket.basketItems.offerId", hasItems(1001))
                            .body("basket.basketItems.productCode", hasItems("carrot-sticks"))
                            .extract().response();
        });
        step("Добавляем товар в корзину и сохраняем количество товара", () -> {
            Response responce =
                    given()
                            .contentType("application/json; charset=UTF-8")
                            .cookies(authorizationCookie)
                            .body("{\"offerId\":6626,\"quantity\":5,\"ingredientGroups\":[]}")
                            .when()
                            .put("https://mcdonalds.ru/api/basket/add")
                            .then()
                            .statusCode(200)
                            .body("basket.basketItems.offerId", hasItems(6626))
                            .body("basket.basketItems.productCode", hasItems("applesauce-agusha"))
                            .extract().response();
        });
        step("Подтверждаем, что в корзине добавлен наш товар", () -> {
            Response responce =
                    given()
                            .contentType("application/json; charset=UTF-8")
                            .cookies(authorizationCookie)
                            .when()
                            .post("https://mcdonalds.ru/api/order/dryRun")
                            .then()
                            .statusCode(200)
                            .body("order.basket.items.offerId", hasItems(998, 1001))
                            .body("order.basket.items.productCode", hasItems("apple-slices", "carrot-sticks"))
                            .extract().response();
            JsonPath jsonPath = new JsonPath(responce.asString());
            int itemsSize = jsonPath.getInt("order.basket.items.size()");
            System.out.println("Количество позиций = " + itemsSize);
            for (int orderId = 0; orderId < itemsSize; orderId++) {
                int quantity = jsonPath.getInt("order.basket.items[" + orderId + "].quantity");
                orderCount = orderCount + quantity;
            }
            System.out.println("Количество продуктов в корзине = " + orderCount);
        });
        step("Удаляем весь товар из корзины", () -> {
            Response responce =
                    given()
                            .contentType("application/json; charset=UTF-8")
                            .cookies(authorizationCookie)
                            .when()
                            .delete("https://mcdonalds.ru/api/basket/clear")
                            .then()
                            .statusCode(200)
                            .body("message", is("Корзина очищена"))
                            .body("success", is(true))
                            .extract().response();
            System.out.println("Сообщение после удаления всех продуктов: " + (String) responce.path("message"));
        });
    }

    @Test
    @Story("Получаем информацию о продукте")
    @Microservice("Billing")
    @Tags({@Tag("api"), @Tag("regress")})
    // @JiraIssues({@JiraIssue("HOMEWORK-238")}) - создать задачу
    @DisplayName("Получаем информацию о продукте 'Цезарь-ролл' и выводим в консоль его состав")
    void getInformationAboutCaesarRoll() {
        step("Получение информации о продукте Цезарь-ролл", () -> {
            Response responce =
                    given()
                            .contentType("application/json; charset=UTF-8")
                            .when()
                            .get("https://mcdonalds.ru/api/product/caesar-roll")
                            .then()
                            .statusCode(200)
                            .body("product.code", is("caesar-roll"))
                            .body("product.category.alias", is("rolls"))
                            .extract().response();
            System.out.println("Название: " + (String) responce.path("product.name"));
            System.out.println("Категория: " + (String) responce.path("product.category.alias"));
            System.out.println("Состав: ");
            productName = responce.path("product.composition.name");
            for (int i = 0; i < productName.size(); i++) {
                System.out.println("- " + productName.get(i) + " ");
            }
        });
    }

    @Test
    @Story("Получаем информацию о промо")
    @Microservice("Billing")
    @Tags({@Tag("api"), @Tag("regress")})
    // @JiraIssues({@JiraIssue("HOMEWORK-238")}) - создать задачу
    @DisplayName("Получаем информацию и выводим в консоль названия промо-акций")
    void getInformationAboutPromo() {
        step("Получение информации о промо", () -> {
            Response responce =
                    given()
                            .contentType("application/json; charset=UTF-8")
                            .when()
                            .get("https://mcdonalds.ru/api/promos/0000073738")
                            .then()
                            .statusCode(200)
                            .extract().response();
            promoName = responce.path("items.name");
            for (int i = 0; i < promoName.size(); i++) {
                System.out.println("Название: " + promoName.get(i) + " ");
            }
        });
    }

    @Test
    @Story("Получаем информацию о бургерах")
    @Microservice("Billing")
    @Tags({@Tag("api"), @Tag("regress")})
    // @JiraIssues({@JiraIssue("HOMEWORK-238")}) - создать задачу
    @DisplayName("Получаем информацию о бургерах и их цене")
    void getInformationAboutSandwiches() {
        step("Получение информации о бургерах", () -> {
            Response responce =
                    given()
                            .contentType("application/json; charset=UTF-8")
                            .when()
                            .get("https://mcdonalds.ru/api/menu/category/sandwiches")
                            .then()
                            .statusCode(200)
                            .extract().response();
            JsonPath jsonPath = new JsonPath(responce.asString());
            int productsSize = jsonPath.getInt("products.size()");

            for (int productId = 0; productId < productsSize; productId++) {
                String name = jsonPath.getString("products[" + productId + "].name"); // products[0].name
                int offersSize = jsonPath.getInt("products[" + productId + "].offers.size()"); // products[0].offers.size()

                System.out.println("Для товара '" + name + "' найдено " + offersSize + " предложение");

                for (int offerId = 0; offerId < offersSize; offerId++) {
                    int price = jsonPath.getInt("products[" + productId + "].offers[" + offerId + "].price");
                    // products[0].offers[0].price

                    System.out.println("цена - " + price + "");
                }
            }
        });
    }
}





