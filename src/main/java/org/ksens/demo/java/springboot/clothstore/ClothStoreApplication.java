package org.ksens.demo.java.springboot.clothstore;

import org.ksens.demo.java.springboot.clothstore.entities.*;
import org.ksens.demo.java.springboot.clothstore.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@SpringBootApplication
public class ClothStoreApplication {

	@Value("${tests.unit.strings.image-base64-dress}")
	private String dressImageString;

	@Value("${tests.unit.strings.image-base64-shirt}")
	private String shirtImageString;

	@Value("${tests.unit.strings.image-base64-jumper}")
	private String jumperImageString;

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
			ProductDao productDao,
			PasswordEncoder passwordEncoder
			) {
		return args -> {
			roleDao.save(Role.builder().name("ROLE_ADMIN").build());
			roleDao.save(Role.builder().name("ROLE_USER").build());
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
			Category womensCategory = categoryDao.save(Category.builder().name("womens").build());
			Category mensCategory = categoryDao.save(Category.builder().name("mens").build());
			Size size38 = sizeDao.save(Size.builder().title("38").build());
			Size sizeM = sizeDao.save(Size.builder().title("M").build());
			Subcategory dressSubcategory = subcategoryDao.save(
					Subcategory.builder()
							.name("dress")
							.build()
			);
			Subcategory shirtSubcategory = subcategoryDao.save(
					Subcategory.builder()
							.name("shirt")
							.build()
			);
			Subcategory jumperSubcategory = subcategoryDao.save(
					Subcategory.builder()
							.name("jumper")
							.build()
			);

			productDao.save(
					Product.builder()
						.name("Pimkie")
						.description("Composition: Viscose - 100%")
						.price(new BigDecimal(1450.00))
						.quantity(3)
						.image(dressImageString)
							.category(womensCategory)
						.subcategory(dressSubcategory)
						.size(size38)
					.build()
			);
			productDao.save(
					Product.builder()
							.name("Zubrytskaya")
							.description("Composition: Linen - 70%, Cotton - 30%")
							.price(new BigDecimal(1990.00))
							.quantity(4)
							.image(shirtImageString)
							.category(womensCategory)
							.subcategory(shirtSubcategory)
							.size(size38)
							.build()
			);
			productDao.save(
					Product.builder()
							.name("United Colors of Benetton")
							.description("Composition: Cotton - 100%")
							.price(new BigDecimal(1270.00))
							.quantity(2)
							.image(jumperImageString)
							.category(mensCategory)
							.subcategory(jumperSubcategory)
							.size(sizeM)
							.build()
			);
		};

	}


}
