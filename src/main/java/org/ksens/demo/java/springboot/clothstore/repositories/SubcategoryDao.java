package org.ksens.demo.java.springboot.clothstore.repositories;

import org.ksens.demo.java.springboot.clothstore.entities.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubcategoryDao extends JpaRepository<Subcategory, Long> {
    List<Subcategory> findSubcategoryByCategoryId(Long categoryId);
}
