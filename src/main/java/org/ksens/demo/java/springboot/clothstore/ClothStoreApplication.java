package org.ksens.demo.java.springboot.clothstore;

import org.ksens.demo.java.springboot.clothstore.entities.*;
import org.ksens.demo.java.springboot.clothstore.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ClothStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClothStoreApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(
			RoleDao roleDao,
			UserDao userDao,
			CategoryDao categoryDao,
			SubcategoryDao subcategoryDao,
			SizeDao sizeDao,
			PasswordEncoder passwordEncoder
			) {
		return args -> {
			roleDao.save(Role.builder().name("ROLE_ADMIN").build());
			roleDao.save(Role.builder().name("ROLE_USER").build());
			categoryDao.save(Category.builder().name("C1").build());
			categoryDao.save(Category.builder().name("C2").build());
			sizeDao.save(Size.builder().title("S1").build());
			sizeDao.save(Size.builder().title("S2").build());
			Category c1Category = categoryDao.findCategoryByName("C1");
			Category c2Category = categoryDao.findCategoryByName("C2");
			subcategoryDao.save(
					Subcategory.builder()
									.name("sub1")
									.category(c1Category)
									.build()
			);
			subcategoryDao.save(
					Subcategory.builder()
							.name("sub2")
							.category(c2Category)
							.build()
			);
			Role adminRole = roleDao.findRoleByName("ROLE_ADMIN");
			Role userRole = roleDao.findRoleByName("ROLE_USER");
			userDao.save(
					User.builder()
							.name("admin")
							.password(passwordEncoder.encode("admin"))
							.role(adminRole)
							.build()
			);
			userDao.save(
					User.builder()
							.name("one")
							.password(passwordEncoder.encode("user1"))
							.role(userRole)
							.build()
			);
			userDao.save(
					User.builder()
							.name("two")
							.password(passwordEncoder.encode("user2"))
							.role(userRole)
							.build()
			);
			userDao.save(
					User.builder()
							.name("three")
							.password(passwordEncoder.encode("user3"))
							.role(userRole)
							.build()
			);
		};

	}


}
