package kr.co.fitzcode.product.service;

import kr.co.fitzcode.common.dto.CartDTO;

import java.util.List;

public interface CartService {
    public void addProductToCart(CartDTO cartDTO);
    public List<CartDTO> getCartProductsByUserId(int userId);
}
