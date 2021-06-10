package org.ksens.demo.java.springboot.clothstore.junit.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ksens.demo.java.springboot.clothstore.entities.Category;
import org.ksens.demo.java.springboot.clothstore.entities.Subcategory;
import org.ksens.demo.java.springboot.clothstore.models.CategoryModel;
import org.ksens.demo.java.springboot.clothstore.models.ResponseModel;
import org.ksens.demo.java.springboot.clothstore.models.SubcategoryModel;
import org.ksens.demo.java.springboot.clothstore.repositories.CategoryDao;
import org.ksens.demo.java.springboot.clothstore.services.CategoryService;
import org.ksens.demo.java.springboot.clothstore.services.interfaces.ICategoryService;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/*
 * Набор модульных тестов для класса CategoryService
 * */
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    // Внедрение экземпляра CategoryDao
    // для дальнейшего использования службой CategoryService
    @Mock
    private CategoryDao categoryDao;

    // Внедрение экземпляра интерфейсного типа ICategoryService
    // для использования в модульных тестах
    // до разработки реализаций (подход "Разработка через тестирование" -
    // Test Driven Development, TDD)
    @Mock
    private ICategoryService categoryServiceMock;

    // Внедрение экземпляра CategoryService для его дальнейшего тестирования -
    // так, что при создании в него внедрятся все необходимые зависимости,
    // помеченные в классе тестов аннтацией @Mock
    @InjectMocks
    private CategoryService categoryService;

    // подготовка объекта-заглушки, который может получать на входе
    // метод save репозитория категорий
    ArgumentCaptor<Category> categoryArgument =
            ArgumentCaptor.forClass(Category.class);

    ArgumentCaptor<Subcategory> subcategoryArgument =
            ArgumentCaptor.forClass(Subcategory.class);

    @Test
    void givenCategoryModelWhenCallCreateMethodThenReturnSuccessResponseModel () {
        final CategoryModel categoryModel =
                CategoryModel.builder()
                        .name("test category 1")
                        .build();
        ResponseModel responseModel =
                categoryService.createCategory(categoryModel);
        // Проверка, что результат не равен null
        assertNotNull(responseModel);
        // Проверка, что результат содержит положительный статус-код
        assertEquals(ResponseModel.SUCCESS_STATUS, responseModel.getStatus());
        // Проверка, что в результате вызванного выше метода тестируемой службы (create)
        // ровно один раз был вызван метод save репозитория categoryDao.
        // При этом на уровне тест-кейса неизвестно, какой именно аргумент
        // получил метод save репозитория при его каскадном вызове методом create службы,
        // поэтому в область аргумента передается объект-заглушка -
        // заменитель реального определенного аргумента
        verify(categoryDao, atLeastOnce())
                .save(categoryArgument.capture());
        verify(categoryDao, atMostOnce())
                .save(categoryArgument.capture());
    }

    @Test
    void givenСategoryServiceMockWhenCallGetAllMethodThenReturnSuccessResponseModel () {
        // Обучаем макет:
        // вернуть что? - результат, равный ...
        doReturn(
                ResponseModel.builder()
                        .status(ResponseModel.SUCCESS_STATUS)
                        .data(Arrays.asList(
                                new CategoryModel(1L, "c1"),
                                new CategoryModel(2L, "c2"),
                                new CategoryModel(3L, "c3"))
                        ).build()
        ).when(categoryServiceMock) // откуда? - из объекта categoryServiceMock - макета службы
                .getCategories(); // как результат вызова какого метода? - getAll
        // вызов настроенного выше метода макета, созданного по интерфейсу
        ResponseModel responseModel =
                categoryServiceMock.getCategories();
        assertNotNull(responseModel);
        assertNotNull(responseModel.getData());
        assertEquals(((List)responseModel.getData()).size(), 3);
    }

    @Test
    void givenСategoryServiceMockWhenCallGetAllSubcategoryMethodThenReturnSuccessResponseModel () {
        // Обучаем макет:
        // вернуть что? - результат, равный ...
        doReturn(
                ResponseModel.builder()
                        .status(ResponseModel.SUCCESS_STATUS)
                        .data(Arrays.asList(
                                new SubcategoryModel(1L, "sc1", 1L, new CategoryModel(1L,"c1")),
                                new SubcategoryModel(2L, "sc1", 1L, new CategoryModel(1L,"c1")),
                                new SubcategoryModel(3L, "sc1", 1L, new CategoryModel(1L,"c1")))
                        ).build()
        ).when(categoryServiceMock) // откуда? - из объекта categoryServiceMock - макета службы
        // вызов настроенного выше метода макета, созданного по интерфейсу
        ResponseModel responseModel =
                categoryServiceMock.getSubcategories();
        assertNotNull(responseModel);
        assertNotNull(responseModel.getData());
        assertEquals(((List)responseModel.getData()).size(), 3);
    }

}
