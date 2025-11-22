package SE.demo.dto.wishlist;

import lombok.Data;

@Data
public class WishlistDto {
    public WishlistDto(long wishlistId, long programId, String title, String thumbnailUrl) {
        this.wishlistId = wishlistId;
        this.programId = programId;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
    }

    long wishlistId;
    long programId;
    String title;
    String thumbnailUrl;
}
