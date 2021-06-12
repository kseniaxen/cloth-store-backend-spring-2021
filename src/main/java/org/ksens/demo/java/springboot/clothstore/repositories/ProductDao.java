package org.ksens.demo.java.springboot.clothstore.repositories;

import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.ksens.demo.java.springboot.clothstore.entities.Product;
import org.ksens.demo.java.springboot.clothstore.entities.QProduct;
import org.ksens.demo.java.springboot.clothstore.entities.Subcategory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductDao extends JpaRepository<Product, Long>,
        QuerydslPredicateExecutor<Product>, QuerydslBinderCustomizer<QProduct> {
    // пользовательский метод получения списка товаров,
    // идентификаторы категорий которых входят в множество,
    // заданное параметром :ids,
    // который получает список идентификаторов подкатегорий из объекта списка
    // @Param("ids") List<Long> subcategoryIds,
    // передаваемого при вызове метода в качестве аргумента
    // (явно задается JPQL-запрос, который должен выполнить модуль Spring Data)
    @Query( "SELECT p FROM Product p WHERE p.subcategory.id IN :idsub AND p.size.id IN :idsize" )
    List<Product> findBySubcategoryIdsAndSizeIds(
            @Param("idsub") List<Long> subcategoryIds,
            @Param("idsize") List<Long> sizeIds,
            Sort sort
    );

    @Query( "SELECT p FROM Product p WHERE p.subcategory.category.id IN :ids" )
    List<Product> findByCategoryIds(
            @Param("ids") List<Long> categoryIds,
            Sort sort
    );

    @Query( "SELECT MIN(p.price) FROM Product p" )
    BigDecimal findMinimum ();

    Product findTop1ByOrderByPriceDesc ();

    Product findTop1ByOrderByQuantityDesc ();
    Product findTop1ByOrderByQuantityAsc ();

    Integer countProductsBySubcategory(Subcategory subcategory);

    // добавление поддержки запросов query dsl
    // (предварительно нужно сгенерировать тип QProduct командой
    // mvn apt:process или запустить lifecycle task package)
    @Override
    default public void customize(
            QuerydslBindings bindings, QProduct root) {
        bindings.bind(String.class)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
        bindings.excluding(root.image);
    }
}
