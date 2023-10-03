package org.project.shop.repository;

import org.project.shop.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< HEAD
import java.util.List;

public interface CartItemRepository {
=======
public interface CartItemRepository{
>>>>>>> 5045eca287e3ad1d06c9c6b68101e6e126cf919a
    public CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    public CartItem findById(Long id);

    public void save(CartItem cartItem);
<<<<<<< HEAD

    public void clear();

    public List<CartItem> findAllCartItem();


    public List<CartItem> findByCartMemberId(Long cartId);

    public List<CartItem> findByCartId(Long memberId);


}
=======
}
>>>>>>> 5045eca287e3ad1d06c9c6b68101e6e126cf919a
