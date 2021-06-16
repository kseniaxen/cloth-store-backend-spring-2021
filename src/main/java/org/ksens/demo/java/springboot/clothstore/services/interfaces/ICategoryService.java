package org.ksens.demo.java.springboot.clothstore.services.interfaces;

import org.ksens.demo.java.springboot.clothstore.models.CategoryModel;
import org.ksens.demo.java.springboot.clothstore.models.ResponseModel;
import org.ksens.demo.java.springboot.clothstore.models.SubcategoryModel;

public interface ICategoryService {
    ResponseModel createCategory(CategoryModel categoryModel);
    ResponseModel updateCategory(CategoryModel categoryModel);
    ResponseModel getCategories();
    ResponseModel deleteCategory(Long id);
    ResponseModel createSubcategory(SubcategoryModel subcategoryModel);
    ResponseModel updateSubcategory(SubcategoryModel subcategoryModel);
    ResponseModel getSubcategories();
    ResponseModel deleteSubcategory(Long id);
}
