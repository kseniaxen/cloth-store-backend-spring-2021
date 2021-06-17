package org.ksens.demo.java.springboot.clothstore.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Size")
@Data
@EqualsAndHashCode(exclude = "products")
@ToString(exclude = "products")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Size{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false, unique = true, length = 50)
    private String title;
    @OneToMany(mappedBy="size", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Product> products;
}
