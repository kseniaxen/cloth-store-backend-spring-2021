package org.ksens.demo.java.springboot.clothstore.repositories;

import org.ksens.demo.java.springboot.clothstore.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDao extends JpaRepository<Category, Long> {
    Category findCategoryByName(String name);
}
