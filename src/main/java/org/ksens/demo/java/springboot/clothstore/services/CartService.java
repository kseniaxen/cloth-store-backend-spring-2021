package org.ksens.demo.java.springboot.clothstore.services;

import org.ksens.demo.java.springboot.clothstore.entities.Product;
import org.ksens.demo.java.springboot.clothstore.models.Cart;
import org.ksens.demo.java.springboot.clothstore.models.CartItem;
import org.ksens.demo.java.springboot.clothstore.models.ResponseModel;
import org.ksens.demo.java.springboot.clothstore.repositories.CartMongoDao;
import org.ksens.demo.java.springboot.clothstore.repositories.ProductDao;
import org.ksens.demo.java.springboot.clothstore.repositories.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Служба для добавления, удаления и изменения количества
 * товаров в корзине покупателя
 * */
@Service
public class CartService {

    private final ProductDao productDao;

    private final UserDao userDao;

    private final CartMongoDao cartMongoDao;

    /**
     * @param productDao репозиторий для чтения/записи сущностей "Товар" в РБД
     * @param userDao репозиторий для чтения/записи сущностей "Пользователь" в РБД
     * @param cartMongoDao репозиторий для чтения/записи моделей "Корзина" в хранилище Mongo
     * */
    public CartService(ProductDao productDao, UserDao userDao, CartMongoDao cartMongoDao) {
        this.productDao = productDao;
        this.userDao = userDao;
        this.cartMongoDao = cartMongoDao;
    }

    /**
     * Получает объект корзины покупок из хранилища Mongo по данным текущего пользователя
     * @param authentication стандартный объект SpringSecurity, содержащий имя и название роли текущего пользователя
     * */
    public Cart getCart(Authentication authentication) {
        // есть ли текущий аутентифицированный пользователь
        if (authentication != null && authentication.isAuthenticated()) {
            // из mysql db находим по имени учетную запись пользователя,
            // из нее - идентификатор
            Long userId =
                    userDao.findUserByName(authentication.getName()).getId();
            // из mongo db по идентификатору - объект корзины пользователя
            Cart cart = cartMongoDao.findCartByUserId(userId);
            if (cart == null || cart.getStatus().equals("pay")) {
                cart = new Cart();
                cart.setUserId(userId);
                cart.setStatus("not_pay");
            }
            return cart;
        } else {
            return null;
        }
    }

    // получить все элементы корзины текущего пользователя
    public ResponseModel getCartItems (Authentication authentication) {
        Cart cart = this.getCart(authentication);
        if (cart != null) {
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message("Cart data fetched successfully")
                    .data(cart.getCartItems())
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message("No cart")
                    .build();
        }
    }

    // изменить число определенного товара в объекте корзины
    public ResponseModel changeCartItemCount(Authentication authentication, Long productId, CartItem.Action action) {
        Cart cart = this.getCart(authentication);
        CartItem currentCartItem = null;
        // в БД находим описание товара по его ИД
        Product product = productDao.findById(productId).get();
        // в объекте корзины пытаемся найти элемент списка товаров в корзине,
        // у которого ИД описания товара такой же, как заданный для изменения
        Optional<CartItem> currentCartItemOptional =
                cart.getCartItems()
                        .stream()
                        .filter((item) -> item.getProductId().equals(productId))
                        .findFirst();
        // если в корзине уже был хотя бы один такой товар
        if (currentCartItemOptional.isPresent()) {
            currentCartItem = currentCartItemOptional.get();
        } else {
            // если нет - добавляем товар в корзину с указанием его количества равным 0
            currentCartItem =
                    CartItem.builder()
                            .productId(productId)
                            .name(product.getName())
                            .price(product.getPrice())
                            .quantity(0)
                            .build();
            cart.getCartItems().add(currentCartItem);
        }
        if (action != null) {
            switch (action) {
                case ADD:
                    // увеличение числа товара в корзтине на 1
                    currentCartItem.setQuantity(currentCartItem.getQuantity() + 1);
                    break;
                case SUB:
                    // уменьшение числа товара в корзтине на 1,
                    // но если осталось 0 или меньше - полное удаление товара из корзины
                    currentCartItem.setQuantity(currentCartItem.getQuantity() - 1);
                    if (currentCartItem.getQuantity() <= 0) {
                        cart.getCartItems().remove(currentCartItem);
                    }
                    break;
                case REM:
                    // безусловное полное удаление товара из корзины
                    cart.getCartItems().remove(currentCartItem);
                    break;
                default:
                    break;
            }
        }
        // сохранение объекта корзины в MongoDB -
        // первичное или обновление
        cartMongoDao.save(cart);
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message("Cart data changed successfully")
                .data(cart.getCartItems())
                .build();
    }
    // удалить из корзины все элементы
    public void clearCartItems (Authentication authentication) {
        Cart cart = getCart(authentication);
        if (cart != null) {
            cart.getCartItems().clear();
            cartMongoDao.save(cart);
        }
    }

    public ResponseModel getAllByStatus (String status){
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message("Get all card by status")
                .data(cartMongoDao.findAllByStatus(status))
                .build();
    }

    public ResponseModel updateStatus (String mongoId, String status){
        Optional<Cart> cartOptional = cartMongoDao.findById(mongoId);
        if(cartOptional.isPresent()){
            cartOptional.get().setStatus(status);
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message("Update status on card")
                    .build();
        }else{
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message("No cart")
                    .build();
        }
    }
}
