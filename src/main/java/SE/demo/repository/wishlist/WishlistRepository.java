package SE.demo.repository.wishlist;

import SE.demo.dto.wishlist.WishlistDto;
import java.util.List;

public interface WishlistRepository {
    public List<WishlistDto> getWishlists(long userId);

    public boolean addWishlist(long userId, long programId);

    public boolean deleteWishlist(long userId, long wishlistId);
}
