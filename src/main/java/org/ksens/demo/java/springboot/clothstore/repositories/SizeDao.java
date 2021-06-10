package org.ksens.demo.java.springboot.clothstore.repositories;

import org.ksens.demo.java.springboot.clothstore.entities.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeDao extends JpaRepository<Size, Long> {

}
