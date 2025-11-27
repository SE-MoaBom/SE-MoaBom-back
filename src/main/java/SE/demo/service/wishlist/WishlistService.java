package SE.demo.service.wishlist;

import SE.demo.dto.wishlist.WishlistDto;
import SE.demo.entity.User;
import SE.demo.jwt.JwtTokenProvider;
import SE.demo.repository.wishlist.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final JwtTokenProvider jwtTokenProvider;
    private final WishlistRepository wishlistRepository;

    public ResponseEntity<List<WishlistDto>> getWishlist(User user) {
        List<WishlistDto> wishlists = wishlistRepository.getWishlists(user.getUserId());
        return ResponseEntity.ok(wishlists);
    }

    public ResponseEntity<?> addWishlist(User user, long wishlistId) {
        boolean updated = wishlistRepository.addWishlist(user.getUserId(), wishlistId);
        if (!updated) {
            return ResponseEntity.badRequest().body("DB 반영 실패");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<?> deleteWishlist(User user, long wishlistId) {

        boolean updated = wishlistRepository.deleteWishlist(user.getUserId(), wishlistId);
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "삭제할 위시리스트 정보를 찾을 수 없습니다."));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
