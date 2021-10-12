package tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;

public class ApiTests {
    int productCount;
    ArrayList<String> orderCount;
    ArrayList<String> productName;
    ArrayList<String> promoName;
    Map<String, String> authorizationCookie;


    @Test
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
            System.out.println(responce.asString());
            productCount = responce.path("basket.itemCount");
            System.out.println("Количество заказанного продукта = " + productCount);
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
            System.out.println(responce.asString());
            productCount = responce.path("basket.itemCount");
            System.out.println("Количество заказанного продукта = " + productCount);
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
            System.out.println(responce.asString());
            orderCount = responce.path("order.basket.items.quantity");
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
            System.out.println((String) responce.path("message"));
        });
    }


    @Test
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
            System.out.println(responce.asString());
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
            System.out.println(responce.asString());
            promoName = responce.path("items.name");
            for (int i = 0; i < promoName.size(); i++) {
                System.out.println("Название: " + promoName.get(i) + " ");
            }
        });
    }

    @Test
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





