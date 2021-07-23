package org.ksens.demo.java.springboot.clothstore.repositories;

import org.ksens.demo.java.springboot.clothstore.models.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartMongoDao extends MongoRepository<Cart, String> {
    // расширяем стандартный набор методов работы с хранилищем
    // методом получения одного объекта Cart по ИД пользователя,
    // которому пренадлежит эта корзина
    Cart findCartByUserId(Long userId);
}