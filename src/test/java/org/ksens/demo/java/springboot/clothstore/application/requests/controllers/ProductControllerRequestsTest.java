package org.ksens.demo.java.springboot.clothstore.application.requests.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.*;
import org.ksens.demo.java.springboot.clothstore.ClothStoreApplication;
import org.ksens.demo.java.springboot.clothstore.models.ProductModel;
import org.ksens.demo.java.springboot.clothstore.models.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;

// включение режима теста приложения с запуском на реальном веб-сервере
// и с доступом к контексту приложения
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ClothStoreApplication.class
)
// режим создания одиночного экземпляра класса тестов для всех кейсов
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// активация контейнера выполнения кейсов согласно пользовательской нумерации
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerRequestsTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    final String baseUrl = "/api";

    @Test
    @Order(1)
    public void givenNameAndQuantityGreaterThan2WhenRequestsListOfPimkieProductsThenCorrect() throws Exception {
        // получение от REST API списка товаров, у которых название Pimkie
        // и количество выше 2
        ResponseEntity<ResponseModel> response =
                testRestTemplate.getForEntity(
                        baseUrl + "/products/filtered::orderBy:id::sortingDirection:DESC/?search=name:Pimkie;quantity>2",
                        ResponseModel.class
                );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ArrayList products =
                (ArrayList) response.getBody().getData();
        assertNotNull(products);
        // такой товар должен быть найден только один
        assertEquals(1, products.size());
        // сложный тестовый веб-клиент testRestTemplate десериализует множество данных моделей
        // как список словарей, поэтому нужно явное преоразование в список моделей ProductModel
        List<ProductModel> productModels =
                (new ObjectMapper())
                        .convertValue(products, new TypeReference<>() { });
        // у каждого найденного товара должны быть значения полей,
        // соотетствующие параметрам поискового запроса
        productModels.forEach(product -> {
            assertEquals("Pimkie", product.getName());
            assertTrue(product.getQuantity() > 2);
        });
    }

    @Test
    @Order(2)
    public void givenSubcategoryIdAndPriceLess2000ThanWhenRequestsListOfProductsThenCorrect () throws Exception {
        // получение от REST API списка товаров, у которых id подкатегории равен 1
        // и цена ниже 2000
        ResponseEntity<ResponseModel> response =
                testRestTemplate.getForEntity(
                        baseUrl + "/products/filtered::orderBy:id::sortingDirection:DESC/?search=subcategory:[1,2];price<2000",
                        ResponseModel.class
                );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ArrayList products =
                (ArrayList) response.getBody().getData();
        assertNotNull(products);
        assertEquals(2, products.size());
        // сложный тестовый веб-клиент testRestTemplate десериализует множество данных моделей
        // как список словарей, поэтому нужно явное преоразование в список моделей ProductModel
        List<ProductModel> productModels =
                (new ObjectMapper())
                        .convertValue(products, new TypeReference<>() { });
        // у каждого найденного товара должны быть значения полей,
        // соотетствующие параметрам поискового запроса
        productModels.forEach(product -> {
            List<Long> subcategoryIds = Lists.newArrayList(1L,2L);
            Matcher<Iterable<? super Long>> matcher = hasItem(product.getSubcategory().getId());
            assertThat(subcategoryIds, matcher);
            assertTrue(product.getPrice().compareTo(new BigDecimal(2000))<0);
            System.out.println(product);
        });
    }

    @Test
    @Order(2)
    public void givenSubcategoryIdAndSizeIdThanWhenRequestsListOfProductsThenCorrect () throws Exception {
        // получение от REST API списка товаров, у которых id подкатегории равен 1,2
        // и размер 1
        ResponseEntity<ResponseModel> response =
                testRestTemplate.getForEntity(
                        baseUrl + "/products/filtered::orderBy:id::sortingDirection:DESC/?search=subcategory:[1,2];size:[1]",
                        ResponseModel.class
                );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ArrayList products =
                (ArrayList) response.getBody().getData();
        assertNotNull(products);
        assertEquals(2, products.size());
        // сложный тестовый веб-клиент testRestTemplate десериализует множество данных моделей
        // как список словарей, поэтому нужно явное преоразование в список моделей ProductModel
        List<ProductModel> productModels =
                (new ObjectMapper())
                        .convertValue(products, new TypeReference<>() { });
        // у каждого найденного товара должны быть значения полей,
        // соотетствующие параметрам поискового запроса
        productModels.forEach(product -> {
            List<Long> subcategoryIds = Lists.newArrayList(1L,2L);
            Matcher<Iterable<? super Long>> matcherSub = hasItem(product.getSubcategory().getId());
            assertThat(subcategoryIds, matcherSub);
            List<Long> sizeId = Lists.newArrayList(1L);
            Matcher<Iterable<? super Long>> matcherSize = hasItem(product.getSize().getId());
            assertThat(sizeId, matcherSize);
            System.out.println(product);
        });
    }

    @Test
    @Order(3)
    public void givenCategoryIdThanWhenRequestsListOfProductsThenCorrect () throws Exception {
        // получение от REST API списка товаров, у которых id подкатегории равен 1,2
        // и размер 1
        ResponseEntity<ResponseModel> response =
                testRestTemplate.getForEntity(
                        baseUrl + "/products/filtered::orderBy:id::sortingDirection:DESC/?search=category:[1]",
                        ResponseModel.class
                );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ArrayList products =
                (ArrayList) response.getBody().getData();
        assertNotNull(products);
        assertEquals(2, products.size());
        // сложный тестовый веб-клиент testRestTemplate десериализует множество данных моделей
        // как список словарей, поэтому нужно явное преоразование в список моделей ProductModel
        List<ProductModel> productModels =
                (new ObjectMapper())
                        .convertValue(products, new TypeReference<>() { });
        // у каждого найденного товара должны быть значения полей,
        // соотетствующие параметрам поискового запроса
        productModels.forEach(product -> {
            List<Long> categoryIds = Lists.newArrayList(1L);
            Matcher<Iterable<? super Long>> matcherSub = hasItem(product.getCategory().getId());
            assertThat(categoryIds, matcherSub);
            System.out.println(product);
        });
    }

}
