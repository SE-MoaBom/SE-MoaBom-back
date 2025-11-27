package SE.demo.dto.wishlist;

import SE.demo.dto.programs.AvailabilityDto;
import lombok.Data;

import java.util.List;

@Data
public class WishlistProgramDto {
    private Long programId;
    private String title;
    private List<AvailabilityDto> availabilities;
}
