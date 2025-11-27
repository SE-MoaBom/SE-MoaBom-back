package SE.demo.controller.wishlist;

import SE.demo.dto.wishlist.AddWishlistDto;
import SE.demo.dto.wishlist.WishlistDto;
import SE.demo.entity.User;
import SE.demo.service.wishlist.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Wishlist")
public class WishlistController {
    private final WishlistService wishlistService;

    @GetMapping("/wishlists")
    @Operation(summary = "위시리스트 조회", description = "사용자의 위시리스트를 조회합니다.")
    public ResponseEntity<List<WishlistDto>> getWishlists(@AuthenticationPrincipal User user) {
        return wishlistService.getWishlist(user);
    }

    @PostMapping("/wishlists")
    @Operation(summary = "위시리스트 추가", description = "프로그램을 위시리스트에 추가합니다.")
    @ApiResponse(responseCode = "201", content = @Content())
    public ResponseEntity<?> saveWishlist(
            @AuthenticationPrincipal User user,
            @RequestBody AddWishlistDto addWishlistDto
    ) {
        return wishlistService.addWishlist(user, addWishlistDto.getProgramId());
    }

    @DeleteMapping("/wishlists/{wishlistId}")
    @Operation(summary = "위시리스트 삭제", description = "위시리스트에서 프로그램을 삭제합니다.")
    @ApiResponse(responseCode = "204", content = @Content())
    @ApiResponse(responseCode = "404", content = @Content(), description = "Not Found")
    public ResponseEntity<?> deleteWishlist(
            @AuthenticationPrincipal User user,
            @PathVariable long wishlistId
    ) {
        return wishlistService.deleteWishlist(user, wishlistId);
    }
}
