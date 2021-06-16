package org.ksens.demo.java.springboot.clothstore.services;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.ksens.demo.java.springboot.clothstore.entities.Category;
import org.ksens.demo.java.springboot.clothstore.entities.Product;
import org.ksens.demo.java.springboot.clothstore.entities.Size;
import org.ksens.demo.java.springboot.clothstore.entities.Subcategory;
import org.ksens.demo.java.springboot.clothstore.models.*;
import org.ksens.demo.java.springboot.clothstore.repositories.CategoryDao;
import org.ksens.demo.java.springboot.clothstore.repositories.ProductDao;
import org.ksens.demo.java.springboot.clothstore.repositories.SizeDao;
import org.ksens.demo.java.springboot.clothstore.repositories.SubcategoryDao;
import org.ksens.demo.java.springboot.clothstore.repositories.predicate.ProductPredicatesBuilder;
import org.ksens.demo.java.springboot.clothstore.services.interfaces.IProductService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService implements IProductService {

    private final ProductDao productDao;

    private final CategoryDao categoryDao;

    private final SubcategoryDao subcategoryDao;

    private final SizeDao sizeDao;

    public ProductService(ProductDao productDao, CategoryDao categoryDao, SubcategoryDao subcategoryDao, SizeDao sizeDao) {
        this.productDao = productDao;
        this.categoryDao = categoryDao;
        this.subcategoryDao = subcategoryDao;
        this.sizeDao = sizeDao;
    }

    public ResponseModel create(ProductModel productModel) {
        Optional<Category> categoryOptional
                = categoryDao.findById(productModel.getCategoryId());
        Optional<Subcategory> subcategoryOptional
                = subcategoryDao.findById(productModel.getSubcategoryId());
        Optional<Size> sizeOptional
                = sizeDao.findById(productModel.getSizeId());
        if(subcategoryOptional.isPresent() && sizeOptional.isPresent() && categoryOptional.isPresent()){
            Product product =
                    Product.builder()
                            .name(productModel.getName())
                            .description(productModel.getDescription())
                            .price(productModel.getPrice())
                            .quantity(productModel.getQuantity())
                            .image(productModel.getImage())
                            .category(categoryOptional.get())
                            .subcategory(subcategoryOptional.get())
                            .size(sizeOptional.get())
                            .build();
            productDao.save(product);
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Product %s Created", product.getName()))
                    .build();
        }else if(subcategoryOptional.isEmpty()){
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Subcategory #%d Not Found", productModel.getSubcategoryId()))
                    .build();
        }else if(sizeOptional.isEmpty()){
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Size #%d Not Found", productModel.getSizeId()))
                    .build();
        }else{
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Category #%d Not Found", productModel.getCategoryId()))
                    .build();
        }
    }

    public ResponseModel update(ProductModel productModel) {
        Optional<Category> categoryOptional
                = categoryDao.findById(productModel.getCategoryId());
        Optional<Subcategory> subcategoryOptional
                = subcategoryDao.findById(productModel.getSubcategoryId());
        Optional<Size> sizeOptional
                = sizeDao.findById(productModel.getSizeId());
        if(subcategoryOptional.isPresent() && sizeOptional.isPresent() && categoryOptional.isPresent()){
            Product product =
                    Product.builder()
                            .id(productModel.getId())
                            .name(productModel.getName())
                            .description(productModel.getDescription())
                            .price(productModel.getPrice())
                            .quantity(productModel.getQuantity())
                            .category(categoryOptional.get())
                            .subcategory(subcategoryOptional.get())
                            .size(sizeOptional.get())
                            .image(productModel.getImage())
                            .build();
            productDao.save(product);
            // Demo Logging
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Product %s Updated", product.getName()))
                    .build();
        } else if(subcategoryOptional.isEmpty()){
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Subcategory #%d Not Found", productModel.getSubcategoryId()))
                    .build();
        }else if(sizeOptional.isEmpty()){
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Size #%d Not Found", productModel.getSizeId()))
                    .build();
        }else{
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Category #%d Not Found", productModel.getCategoryId()))
                    .build();
        }
    }

    public ResponseModel getAll() {
        List<Product> products = productDao.findAll(Sort.by("id").descending());
        List<ProductModel> productModels =
                products.stream()
                        .map(p ->
                                ProductModel.builder()
                                        .id(p.getId())
                                        .name(p.getName())
                                        .description(p.getDescription())
                                        .price(p.getPrice())
                                        .quantity(p.getQuantity())
                                        .image(p.getImage())
                                        .category(
                                                CategoryModel.builder()
                                                        .id(p.getCategory().getId())
                                                        .name(p.getCategory().getName())
                                                        .build()
                                        )
                                        .subcategory(
                                                SubcategoryModel.builder()
                                                        .id(p.getSubcategory().getId())
                                                        .name(p.getSubcategory().getName())
                                                        .build()
                                        )
                                        .size(
                                                SizeModel.builder()
                                                        .id(p.getSize().getId())
                                                        .title(p.getSize().getTitle())
                                                        .build()
                                        )
                                        .build()
                        )
                        .collect(Collectors.toList());
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .data(productModels)
                .build();
    }

    public ResponseModel delete(Long id) {
        Optional<Product> productOptional = productDao.findById(id);
        if (productOptional.isPresent()){
            Product product = productOptional.get();
            productDao.delete(product);
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Product #%s Deleted", product.getName()))
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Product #%d Not Found", id))
                    .build();
        }
    }

    public ResponseModel getProductsPriceBounds() {
        Map<String, Integer> maxAndMin = new LinkedHashMap<>();
        maxAndMin.put("min", productDao.findMinimum().intValue());
        maxAndMin.put("max", productDao.findTop1ByOrderByPriceDesc().getPrice().intValue());
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .data(maxAndMin)
                .build();
    }

    public ResponseModel getProductsQuantityBounds() {
        Map<String, Integer> maxAndMin = new LinkedHashMap<>();
        maxAndMin.put("min", productDao.findTop1ByOrderByQuantityAsc().getQuantity());
        maxAndMin.put("max", productDao.findTop1ByOrderByQuantityDesc().getQuantity());
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .data(maxAndMin)
                .build();
    }

    public ResponseModel getFiltered(ProductFilterModel filter) {
        List<Product> products =
                productDao.findBySubcategoryIdsAndSizeIds(
                        filter.subcategories,
                        filter.sizes,
                        Sort.by(filter.sortingDirection, filter.orderBy)
                );

        return getResponseModelFromEntities(products);
    }

    // поиск отфильтрованного и отсортированного списка товаров
    // на основе запросов query dsl
    public ResponseModel search(ProductSearchModel searchModel) {
        List<Product> products = null;
        if (searchModel.searchString != null && !searchModel.searchString.isEmpty()) {
            ProductPredicatesBuilder builder = new ProductPredicatesBuilder();
            // разбиение значения http-параметра search
            // на отдельные выражения условий фильтрации
            Pattern pattern = Pattern.compile("([\\w]+?)(:|<|>|<:|>:)([\\w\\]\\[\\,]+?);");
            Matcher matcher = pattern.matcher(searchModel.searchString + ";");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
            BooleanExpression expression = builder.build();
            // выполнение sql-запроса к БД
            // с набором условий фильтрации
            // и с указанием имени поля и направления сортировки
            products =
                    (List<Product>) productDao.findAll(
                            expression,
                            Sort.by(
                                    searchModel.sortingDirection,
                                    searchModel.orderBy
                            )
                    );
        } else {
            products =
                    productDao.findAll(
                            Sort.by(
                                    searchModel.sortingDirection,
                                    searchModel.orderBy
                            )
                    );
        }
        return getResponseModelFromEntities(products);
    }

    private ResponseModel getResponseModelFromEntities(List<Product> products) {
        List<ProductModel> productModels =
                products.stream()
                        .map(p ->
                                ProductModel.builder()
                                        .id(p.getId())
                                        .name(p.getName())
                                        .description(p.getDescription())
                                        .price(p.getPrice())
                                        .quantity(p.getQuantity())
                                        .image(p.getImage())
                                        .category(
                                                CategoryModel.builder()
                                                        .id(p.getCategory().getId())
                                                        .name(p.getCategory().getName())
                                                        .build()
                                        )
                                        .subcategory(
                                                SubcategoryModel.builder()
                                                        .id(p.getSubcategory().getId())
                                                        .name(p.getSubcategory().getName())
                                                        .build()
                                        )
                                        .size(
                                                SizeModel.builder()
                                                        .id(p.getSize().getId())
                                                        .title(p.getSize().getTitle())
                                                        .build()
                                        )
                                        .build()
                        )
                        .collect(Collectors.toList());
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .data(productModels)
                .build();
    }

}
