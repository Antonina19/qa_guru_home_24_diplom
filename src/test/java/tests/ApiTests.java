package tests;

import annotations.JiraIssue;
import annotations.JiraIssues;
import annotations.Layer;
import annotations.Lead;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;

@Layer("API tests")
@Lead("qa-lead")
@Owner("puliavinaav")
public class ApiTests {
    int productCount;
    int orderCount = 0;
    ArrayList<String> productName;
    ArrayList<String> promoName;
    Map<String, String> authorizationCookie;

    @Test
    @Story("Отображение товара в корзине")
    @Tags({@Tag("api"), @Tag("regress")})
    @JiraIssues({@JiraIssue("HOMEWORK-256")})
    @DisplayName("Проверяем, что добавленный товар отображается в корзине")
    void addProductInBasket() {
        Map<String, Object> map = getJson();
        Map<String, Object> product = getJsonProduct(910, 1);

        step("Определяем адрес и получаем куки", () -> {
            authorizationCookie =
                    given()
                            .contentType("application/json;charset=UTF-8")
                            .body(map)
                            .when()
                            .put("https://mcdonalds.ru/api/address")
                            .then()
                            .statusCode(200)
                            .extract()
                            .cookies();
        });
        step("Добавляем товар в корзину и сохраняем количество товара", () -> {
            Response response =
                    given()
                            .contentType("application/json; charset=UTF-8")
                            .cookies(authorizationCookie)
                            .body(product)
                            .when()
                            .put("https://mcdonalds.ru/api/basket/add")
                            .then()
                            .statusCode(200)
                            .body("basket.basketItems.offerId", hasItems(910))
                            .body("basket.basketItems.productCode", hasItems("mcflurry-delux-caramel-chocolate"))
                            .extract().response();
            productCount = response.path("basket.itemCount");
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
    @Tags({@Tag("api"), @Tag("regress")})
    @JiraIssues({@JiraIssue("HOMEWORK-256")})
    @DisplayName("Проверяем, что добавленный товар удаляется из корзины")
    void deleteAllProductFromBasket() {
        Map<String, Object> map = getJson();
        Map<String, Object> product1 = getJsonProduct(998, 1);
        Map<String, Object> product2 = getJsonProduct(1001, 2);
        Map<String, Object> product3 = getJsonProduct(6626, 5);

        step("Определяем адрес и получаем куки", () -> {
            authorizationCookie =
                    given()
                            .contentType("application/json;charset=UTF-8")
                            .body(map)
                            .when()
                            .put("https://mcdonalds.ru/api/address")
                            .then()
                            .statusCode(200)
                            .extract()
                            .cookies();
        });
        step("Добавляем товар в корзину и сохраняем количество товара", () -> {
            Response response =
                    given()
                            .contentType("application/json; charset=UTF-8")
                            .cookies(authorizationCookie)
                            .body(product1)
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
                            .body(product2)
                            .when()
                            .put("https://mcdonalds.ru/api/basket/add")
                            .then()
                            .statusCode(200)
                            .body("basket.basketItems.offerId", hasItems(1001))
                            .body("basket.basketItems.productCode", hasItems("carrot-sticks"))
                            .extract().response();
        });
        step("Добавляем товар в корзину и сохраняем количество товара", () -> {
            Response response =
                    given()
                            .contentType("application/json; charset=UTF-8")
                            .cookies(authorizationCookie)
                            .body(product3)
                            .when()
                            .put("https://mcdonalds.ru/api/basket/add")
                            .then()
                            .statusCode(200)
                            .body("basket.basketItems.offerId", hasItems(6626))
                            .body("basket.basketItems.productCode", hasItems("applesauce-agusha"))
                            .extract().response();
        });
        step("Подтверждаем, что в корзине добавлен наш товар", () -> {
            Response response =
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
            JsonPath jsonPath = new JsonPath(response.asString());
            int itemsSize = jsonPath.getInt("order.basket.items.size()");
            System.out.println("Количество позиций = " + itemsSize);
            for (int orderId = 0; orderId < itemsSize; orderId++) {
                int quantity = jsonPath.getInt("order.basket.items[" + orderId + "].quantity");
                orderCount = orderCount + quantity;
            }
            System.out.println("Количество продуктов в корзине = " + orderCount);
        });
        step("Удаляем весь товар из корзины", () -> {
            Response response =
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
            System.out.println("Сообщение после удаления всех продуктов: " + (String) response.path("message"));
        });
    }

    @Test
    @Story("Получаем информацию о продукте")
    @Tags({@Tag("api"), @Tag("regress")})
    @JiraIssues({@JiraIssue("HOMEWORK-256")})
    @DisplayName("Получаем информацию о продукте 'Цезарь-ролл' и выводим в консоль его состав")
    void getInformationAboutCaesarRoll() {
        step("Получение информации о продукте Цезарь-ролл", () -> {
            Response response =
                    given()
                            .contentType("application/json; charset=UTF-8")
                            .when()
                            .get("https://mcdonalds.ru/api/product/caesar-roll")
                            .then()
                            .statusCode(200)
                            .body("product.code", is("caesar-roll"))
                            .body("product.category.alias", is("rolls"))
                            .extract().response();
            System.out.println("Название: " + (String) response.path("product.name"));
            System.out.println("Категория: " + (String) response.path("product.category.alias"));
            System.out.println("Состав: ");
            productName = response.path("product.composition.name");
            for (int i = 0; i < productName.size(); i++) {
                System.out.println("- " + productName.get(i) + " ");
            }
        });
    }

    @Test
    @Story("Получаем информацию о промо")
    @Tags({@Tag("api"), @Tag("regress")})
    @JiraIssues({@JiraIssue("HOMEWORK-256")})
    @DisplayName("Получаем информацию и выводим в консоль названия промо-акций")
    void getInformationAboutPromo() {
        step("Получение информации о промо", () -> {
            Response response =
                    given()
                            .contentType("application/json; charset=UTF-8")
                            .when()
                            .get("https://mcdonalds.ru/api/promos/0000073738")
                            .then()
                            .statusCode(200)
                            .body("items.code", hasSize(9))
                            .body("items.code", hasItems("alpine-delicious", "mcchicken-premier-gift", "glass-gift-for-lunch",
                                    "mcdonalds-mastercard-cashback", "profitable-couples", "coffee-mccafe", "mchappy-day",
                                    "happy-meal-jurassic-world", "tell-cheese"))
                            .extract().response();
            promoName = response.path("items.name");
            for (int i = 0; i < promoName.size(); i++) {
                System.out.println("Название: " + promoName.get(i) + " ");
            }
        });
    }

    @Test
    @Story("Получаем информацию о бургерах")
    @Tags({@Tag("api"), @Tag("regress")})
    @JiraIssues({@JiraIssue("HOMEWORK-256")})
    @DisplayName("Получаем информацию о бургерах и их цене")
    void getInformationAboutSandwiches() {
        step("Получение информации о бургерах", () -> {
            Response response =
                    given()
                            .contentType("application/json; charset=UTF-8")
                            .when()
                            .get("https://mcdonalds.ru/api/menu/category/sandwiches")
                            .then()
                            .statusCode(200)
                            .body("products.id", hasSize(23))
                            .body("category.alias", is("sandwiches"))
                            .extract().response();
            JsonPath jsonPath = new JsonPath(response.asString());
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

    private Map<String, Object> getJson() {
        LinkedList<HashMap<String, String>> list = new LinkedList<>();
        list.add(new HashMap<String, String>() {{
            put("kind", "country");
            put("name", "Россия");
        }});
        list.add(new HashMap<String, String>() {{
            put("kind", "province");
            put("name", "Центральный федеральный округ");
        }});
        list.add(new HashMap<String, String>() {{
            put("kind", "province");
            put("name", "Москва");
        }});
        list.add(new HashMap<String, String>() {{
            put("kind", "locality");
            put("name", "Москва");
        }});
        list.add(new HashMap<String, String>() {{
            put("kind", "street");
            put("name", "Бакунинская улица");
        }});
        list.add(new HashMap<String, String>() {{
            put("kind", "house");
            put("name", "15");
        }});

        Map<String, Object> map = new HashMap<>();
        map.put("lat", 55.774487);
        map.put("long", 37.682377);
        map.put("components", list);

        return map;
    }

    private Map<String, Object> getJsonProduct(int id, int count) {
        LinkedList<HashMap<String, String>> list = new LinkedList<>();

        Map<String, Object> product = new HashMap<>();
        product.put("offerId", id);
        product.put("quantity", count);
        product.put("ingredientGroups", list);

        return product;
    }
}





