package org.ksens.demo.java.springboot.clothstore.services;

import lombok.extern.slf4j.Slf4j;
import org.ksens.demo.java.springboot.clothstore.entities.Category;
import org.ksens.demo.java.springboot.clothstore.entities.Subcategory;
import org.ksens.demo.java.springboot.clothstore.models.CategoryModel;
import org.ksens.demo.java.springboot.clothstore.models.ResponseModel;
import org.ksens.demo.java.springboot.clothstore.models.SubcategoryModel;
import org.ksens.demo.java.springboot.clothstore.repositories.CategoryDao;
import org.ksens.demo.java.springboot.clothstore.repositories.SubcategoryDao;
import org.ksens.demo.java.springboot.clothstore.services.interfaces.ICategoryService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService implements ICategoryService {
    private final CategoryDao categoryDao;
    private final SubcategoryDao subcategoryDao;

    public CategoryService(CategoryDao categoryDao, SubcategoryDao subcategoryDao) {
        this.categoryDao = categoryDao;
        this.subcategoryDao = subcategoryDao;
    }

    public ResponseModel createCategory(CategoryModel categoryModel) {
        // преобразование из модели в сущность
        Category category =
                Category.builder().name(categoryModel.getName().trim()).build();
        // вызов метода сохранения сущности экземпляром репозитория
        categoryDao.save(category);
        // Demo Logging
        System.out.println(String.format("Category %s Created", category.getName()));
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Category %s Created", category.getName()))
                .build();
    }

    public ResponseModel updateCategory(CategoryModel categoryModel) {
        Category category =
                Category.builder()
                        .id(categoryModel.getId())
                        .name(categoryModel.getName())
                        .build();
        categoryDao.save(category);
        // Demo Logging
        System.out.println(String.format("Category %s Updated", category.getName()));
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Category %s Updated", category.getName()))
                .build();
    }

    public ResponseModel getCategories() {
        List<Category> categories = categoryDao.findAll(Sort.by("id").descending());
        List<CategoryModel> categoryModels =
                categories.stream()
                        .map(c ->
                                CategoryModel.builder()
                                        .id(c.getId())
                                        .name(c.getName())
                                        .build()
                        )
                        .collect(Collectors.toList());
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .data(categoryModels)
                .build();
    }

    public ResponseModel deleteCategory(Long id) {
        Optional<Category> categoryOptional = categoryDao.findById(id);
        if (categoryOptional.isPresent()){
            Category category = categoryOptional.get();
            categoryDao.delete(category);
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Category #%s Deleted", category.getName()))
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Category #%d Not Found", id))
                    .build();
        }
    }

    public ResponseModel createSubcategory(SubcategoryModel subcategoryModel) {
        System.out.println(subcategoryModel);
        Optional<Category> categoryOptional
                = categoryDao.findById(subcategoryModel.getCategoryId());
        if(categoryOptional.isPresent()){
            Subcategory subcategory =
                    Subcategory.builder()
                            .name(subcategoryModel.getName())
                            .category(categoryOptional.get())
                            .build();
            subcategoryDao.save(subcategory);
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Subcategory %s Created", subcategory.getName()))
                    .build();
        }else{
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Category #%d Not Found", subcategoryModel.getCategoryId()))
                    .build();
        }
    }

    public ResponseModel updateSubcategory(SubcategoryModel subcategoryModel) {
        Optional<Category> categoryOptional
                = categoryDao.findById(subcategoryModel.getCategoryId());
        if(categoryOptional.isPresent()){
            Subcategory subcategory =
                    Subcategory.builder()
                            .id(subcategoryModel.getId())
                            .name(subcategoryModel.getName())
                            .category(categoryOptional.get())
                            .build();
            subcategoryDao.save(subcategory);
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Subcategory %s Updated", subcategory.getName()))
                    .build();
        }else{
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Category #%d Not Found", subcategoryModel.getCategoryId()))
                    .build();
        }
    }

    public ResponseModel getSubcategories() {
        List<Subcategory> subcategories = subcategoryDao.findAll(Sort.by("id").descending());
        List<SubcategoryModel> subcategoryModels =
                subcategories.stream()
                        .map(c ->
                                SubcategoryModel.builder()
                                        .id(c.getId())
                                        .name(c.getName())
                                        .category(CategoryModel.builder()
                                                .id(c.getCategory().getId())
                                                .name(c.getCategory().getName())
                                                .build())
                                        .build()
                        )
                        .collect(Collectors.toList());
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .data(subcategoryModels)
                .build();
    }

    public ResponseModel deleteSubcategory(Long id) {
        Optional<Subcategory> subcategoryOptional = subcategoryDao.findById(id);
        if (subcategoryOptional.isPresent()){
            Subcategory subcategory = subcategoryOptional.get();
            subcategoryDao.delete(subcategory);
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Category #%s Deleted", subcategory.getName()))
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Category #%d Not Found", id))
                    .build();
        }
    }

    public ResponseModel getSubcategoryByCategory(Long id){
        Optional<Category> categoryOptional
                = categoryDao.findById(id);
        if(categoryOptional.isPresent()){
            List<Subcategory> subcategories = subcategoryDao.findSubcategoryByCategoryId(id);
            for (Subcategory s:subcategories) {
                System.out.println(s);
            }
            List<SubcategoryModel> subcategoryModels =
                    subcategories.stream()
                            .map(c ->
                                    SubcategoryModel.builder()
                                            .id(c.getId())
                                            .name(c.getName())
                                            .category(CategoryModel.builder()
                                                    .id(c.getCategory().getId())
                                                    .name(c.getCategory().getName())
                                                    .build())
                                            .build()
                            )
                            .collect(Collectors.toList());
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .data(subcategoryModels)
                    .build();
        }else{
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Category #%d Not Found", id))
                    .build();
        }
    }

}
