package SE.demo.service.programs;

import SE.demo.dto.programs.AvailabilityDto;
import SE.demo.dto.programs.ProgramDetailResponseDto;
import SE.demo.dto.programs.ProgramPageResponse;
import SE.demo.dto.programs.ProgramResponseDto;
import SE.demo.entity.User;
import SE.demo.jwt.JwtTokenProvider;
import SE.demo.repository.programs.ProgramRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgramService {
    private final JwtTokenProvider tokenProvider;
    private final ProgramRepository programRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public ProgramPageResponse getProgramInfo(
            String token,
            String keyword,
            String status,
            String sort,
            int page,
            int size
    ) {
        Integer userId = null;
        if (token != null) {
            User userFromToken = tokenProvider.getUserFromToken(token);
            userId = userFromToken.getUserId();
        }
        List<ProgramResponseDto> programResponseDtos = programRepository.searchPrograms(keyword, status, sort, page,
                size);

        //각 프로그램에 wishlist 채워넣음
        if (userId != null) {
            for (ProgramResponseDto programResponseDto : programResponseDtos) {
                Long wishlist = programRepository.findWishList(userId, programResponseDto.getProgramId());
                programResponseDto.setWishlistId(wishlist);
            }
        }
        //전체 페이지 계산
        int totalItems = programRepository.countPrograms(keyword, status);
        int totalPages = (int) Math.ceil((double) totalItems / (double) size);
        ProgramPageResponse programPageResponse = new ProgramPageResponse();
        programPageResponse.setPage(page);
        programPageResponse.setSize(totalItems);
        programPageResponse.setTotalpages(totalPages);
        programPageResponse.setResults(programResponseDtos);
        return programPageResponse;
    }

    public ProgramDetailResponseDto getProgramDetail(String token, long programId) {
        ProgramDetailResponseDto programDetail = programRepository.getProgramDetail(programId);
        List<AvailabilityDto> availability = programRepository.findAvailability(programId);
        if (token != null) {
            User userFromToken = jwtTokenProvider.getUserFromToken(token);
            programDetail.setWishlistId(programRepository.findWishList(userFromToken.getUserId(), programId));
        }
        programDetail.setAvailability(availability);
        return programDetail;
    }
}
