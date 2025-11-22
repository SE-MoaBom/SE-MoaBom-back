package SE.demo.repository.wishlist;

import SE.demo.dto.wishlist.WishlistDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcWishlistRepository implements WishlistRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<WishlistDto> getWishlists(long userId) {
        String sql = "select w.wishlist_id, w.program_id, p.title, p.thumbnail_url "
                + "from Wishlist w "
                + "join Program p on p.program_id=w.program_id "
                + "where w.user_id=?";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new WishlistDto(
                        rs.getLong("wishlist_id"),
                        rs.getLong("program_id"),
                        rs.getString("title"),
                        rs.getString("thumbnail_url")
                ),
                userId
        );
    }

    @Override
    public boolean addWishlist(long userId, long programId) {
        String sql = "Insert into Wishlist(user_id,program_id) values(?,?)";
        int update = jdbcTemplate.update(sql, userId, programId);
        if (update == 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteWishlist(long userId, long wishlistId) {
        String sql = "delete from Wishlist where user_id=? and wishlist_id=?";
        int update = jdbcTemplate.update(sql, userId, wishlistId);
        if (update == 0) {
            return false;
        }
        return true;
    }


}
