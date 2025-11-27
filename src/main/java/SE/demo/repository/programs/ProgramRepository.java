package SE.demo.repository.programs;

import SE.demo.dto.programs.AvailabilityDto;
import SE.demo.dto.programs.ProgramDetailResponseDto;
import SE.demo.dto.wishlist.WishlistProgramDto;

import java.util.List;
import java.util.Map;

public interface ProgramRepository {
    List<ProgramDetailResponseDto> searchPrograms(String keyword, String status, String sort, int page, int size);

    Long findWishList(int userId, Long programId);

    Map<Long, Long> findWishListsForPrograms(int userId, List<Long> programIds);

    List<WishlistProgramDto> findWishlistProgramsByUserId(Integer userId);

    int countPrograms(String keyword, String status);

    ProgramDetailResponseDto getProgramDetail(long programId);

    List<AvailabilityDto> findAvailability(long programId);

    Map<Long, List<AvailabilityDto>> findAvailabilitiesForPrograms(List<Long> programIds);
}
