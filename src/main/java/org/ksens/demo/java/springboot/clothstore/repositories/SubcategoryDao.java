package org.ksens.demo.java.springboot.clothstore.repositories;

import org.ksens.demo.java.springboot.clothstore.entities.Category;
import org.ksens.demo.java.springboot.clothstore.entities.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SubcategoryDao extends JpaRepository<Subcategory, Long> {
    Subcategory findSubcategoryByName(String name);
}
