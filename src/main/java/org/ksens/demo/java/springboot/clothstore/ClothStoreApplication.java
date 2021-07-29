package org.ksens.demo.java.springboot.clothstore;

import org.apache.catalina.connector.Connector;
import org.ksens.demo.java.springboot.clothstore.entities.*;
import org.ksens.demo.java.springboot.clothstore.repositories.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
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

	@Value("${tests.unit.strings.image-base64-adidas}")
	private String adidasImageString;

	@Value("${tests.unit.strings.image-base64-dress2}")
	private String dress2ImageString;

	@Value("${tests.unit.strings.image-base64-tshirt2}")
	private String tshirt2ImageString;

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
			Size sizeL = sizeDao.save(Size.builder().title("L").build());
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
			Subcategory tshirtSubcategory = subcategoryDao.save(
					Subcategory.builder()
							.name("T-shirt")
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

			productDao.save(
					Product.builder()
							.name("Magnet")
							.description("Composition: Cotton - 100%")
							.price(new BigDecimal(1032.00))
							.quantity(3)
							.image(dress2ImageString)
							.category(womensCategory)
							.subcategory(dressSubcategory)
							.size(sizeM)
							.build()
			);
			/*
			productDao.save(
					Product.builder()
							.name("adidas")
							.description("Composition: Cotton - 100%")
							.price(new BigDecimal(1140.00))
							.quantity(5)
							.image(adidasImageString)
							.category(womensCategory)
							.subcategory(tshirtSubcategory)
							.size(sizeM)
							.build()
			);

			productDao.save(
					Product.builder()
							.name("O'stin")
							.description("Composition: Cotton - 100%")
							.price(new BigDecimal(399.00))
							.quantity(5)
							.image(tshirt2ImageString)
							.category(mensCategory)
							.subcategory(tshirtSubcategory)
							.size(sizeL)
							.build()
			);
			 */
		};

	}
	@Bean
	public ConfigurableServletWebServerFactory webServerFactory() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
			@Override
			public void customize(Connector connector) {
				connector.setProperty("relaxedQueryChars", "|{}[]");
			}
		});
		return factory;
	}


}
