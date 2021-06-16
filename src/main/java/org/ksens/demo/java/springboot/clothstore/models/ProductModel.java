package org.ksens.demo.java.springboot.clothstore.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@Data
@ToString(exclude = "image")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductModel {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String image;
    private Long categoryId;
    private CategoryModel category;
    private Long subcategoryId;
    private SubcategoryModel subcategory;
    private Long sizeId;
    private SizeModel size;
}
