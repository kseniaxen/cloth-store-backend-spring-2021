package org.ksens.demo.java.springboot.clothstore.services.interfaces;

import org.ksens.demo.java.springboot.clothstore.models.ProductFilterModel;
import org.ksens.demo.java.springboot.clothstore.models.ProductModel;
import org.ksens.demo.java.springboot.clothstore.models.ProductSearchModel;
import org.ksens.demo.java.springboot.clothstore.models.ResponseModel;

public interface IProductService {
    ResponseModel create(ProductModel productModel);
    ResponseModel update(ProductModel productModel);
    ResponseModel getAll();
    ResponseModel delete(Long id);
    ResponseModel getFiltered(ProductFilterModel filter);
    ResponseModel search(ProductSearchModel searchModel);
}
