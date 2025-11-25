package SE.demo.controller.wishlist;

import SE.demo.dto.wishlist.AddWishlistDto;
import SE.demo.entity.User;
import SE.demo.service.wishlist.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    @GetMapping("/wishlists")
    public ResponseEntity<?> getWishlists(@AuthenticationPrincipal User user) {
        return wishlistService.getWishlist(user);
    }

    @PostMapping("/wishlists")
    public ResponseEntity<?> saveWishlist(
            @AuthenticationPrincipal User user,
            @RequestBody AddWishlistDto addWishlistDto
    ) {
        return wishlistService.addWishlist(user, addWishlistDto.getProgramId());
    }

    @DeleteMapping("/wishlists/{wishlistId}")
    public ResponseEntity<?> deleteWishlist(
            @AuthenticationPrincipal User user,
            @PathVariable long wishlistId
    ) {
        return wishlistService.deleteWishlist(user, wishlistId);
    }
}
