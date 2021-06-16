package org.ksens.demo.java.springboot.clothstore.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Categories")
@Data
@EqualsAndHashCode(exclude = "products")
@ToString(exclude = "products")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;
    @OneToMany(mappedBy="category", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Product> products;
}
