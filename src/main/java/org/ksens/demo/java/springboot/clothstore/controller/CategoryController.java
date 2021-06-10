package org.ksens.demo.java.springboot.clothstore.controller;

import org.ksens.demo.java.springboot.clothstore.models.CategoryModel;
import org.ksens.demo.java.springboot.clothstore.models.ResponseModel;
import org.ksens.demo.java.springboot.clothstore.models.SubcategoryModel;
import org.ksens.demo.java.springboot.clothstore.services.CategoryService;
import org.ksens.demo.java.springboot.clothstore.services.interfaces.ICategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final ICategoryService service;

    public CategoryController(ICategoryService service) {
        this.service = service;
    }

    @GetMapping("/categories")
    public ResponseEntity<ResponseModel> getCategories() {
        return new ResponseEntity<>(service.getCategories(), HttpStatus.OK);
    }

    @PostMapping("/categories")
    public ResponseEntity<ResponseModel> createCategory(@RequestBody CategoryModel category) {
        return new ResponseEntity<>(service.createCategory(category), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/categories/{id}")
    public ResponseEntity<ResponseModel> updateCategory(@PathVariable Long id, @RequestBody CategoryModel category) {
        category.setId(id);
        return new ResponseEntity<>(service.updateCategory(category), HttpStatus.OK);
    }

    @DeleteMapping(value = "/categories/{id}")
    public ResponseEntity<ResponseModel> deleteCategory(@PathVariable Long id) {
        ResponseModel responseModel = service.deleteCategory(id);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    @GetMapping("/subcategories")
    public ResponseEntity<ResponseModel> getSubcategories() {
        return new ResponseEntity<>(service.getSubcategories(), HttpStatus.OK);
    }

    @PostMapping("/subcategories")
    public ResponseEntity<ResponseModel> createSubcategory(@RequestBody SubcategoryModel subcategory) {
        return new ResponseEntity<>(service.createSubcategory(subcategory), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/subcategories/{id}")
    public ResponseEntity<ResponseModel> updateSubcategory(@PathVariable Long id, @RequestBody SubcategoryModel subcategory) {
        subcategory.setId(id);
        return new ResponseEntity<>(service.updateSubcategory(subcategory), HttpStatus.OK);
    }

    @DeleteMapping(value = "/subcategories/{id}")
    public ResponseEntity<ResponseModel> deleteSubcategory(@PathVariable Long id) {
        ResponseModel responseModel = service.deleteSubcategory(id);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    @GetMapping("/subcategories/category/{id}")
    public ResponseEntity<ResponseModel> getSubcategoryByCategory(@PathVariable Long id) {
        return new ResponseEntity<>(service.getSubcategoryByCategory(id), HttpStatus.OK);
    }

}
