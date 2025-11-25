package SE.demo.service.wishlist;

import SE.demo.dto.wishlist.WishlistDto;
import SE.demo.entity.User;
import SE.demo.jwt.JwtTokenProvider;
import SE.demo.repository.wishlist.WishlistRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final JwtTokenProvider jwtTokenProvider;
    private final WishlistRepository wishlistRepository;

    public ResponseEntity<?> getWishlist(User user) {
        List<WishlistDto> wishlists = wishlistRepository.getWishlists(user.getUserId());
        return ResponseEntity.ok(wishlists);
    }

    public ResponseEntity<?> addWishlist(User user, long wishlistId) {
        boolean updated = wishlistRepository.addWishlist(user.getUserId(), wishlistId);
        if (!updated) {
            return ResponseEntity.badRequest().body("DB 반영 실패");
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteWishlist(User user, long wishlistId) {

        boolean updated = wishlistRepository.deleteWishlist(user.getUserId(), wishlistId);
        if (!updated) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
