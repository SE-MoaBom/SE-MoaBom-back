package SE.demo.service.programs;

import SE.demo.dto.programs.AvailabilityDto;
import SE.demo.dto.programs.ProgramDetailResponseDto;
import SE.demo.dto.programs.ProgramPageResponse;
import SE.demo.entity.User;
import SE.demo.repository.programs.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgramService {
    private final ProgramRepository programRepository;

    public ProgramPageResponse getProgramInfo(
            User user,
            String keyword,
            String status,
            String sort,
            int page,
            int size
    ) {
        List<ProgramDetailResponseDto> programs = programRepository.searchPrograms(keyword, status, sort, page, size);

        // 프로그램 목록이 비어있을 경우, 바로 비어있는 응답 반환
        if (programs.isEmpty()) {
            ProgramPageResponse emptyResponse = new ProgramPageResponse();
            emptyResponse.setPage(page);
            emptyResponse.setSize(0);
            emptyResponse.setTotalpages(0);
            emptyResponse.setResults(Collections.emptyList());
            return emptyResponse;
        }

        List<Long> programIds = programs.stream()
                .map(ProgramDetailResponseDto::getProgramId)
                .collect(Collectors.toList());

        // 위시리스트 정보 일괄 조회 및 설정
        if (user != null) {
            Map<Long, Long> wishlistMap = programRepository.findWishListsForPrograms(user.getUserId(), programIds);
            programs.forEach(p -> p.setWishlistId(wishlistMap.get(p.getProgramId())));
        }

        // Availability 정보 일괄 조회 및 설정
        Map<Long, List<AvailabilityDto>> availabilityMap = programRepository.findAvailabilitiesForPrograms(programIds);
        programs.forEach(p -> p.setAvailability(availabilityMap.getOrDefault(p.getProgramId(), Collections.emptyList())));

        //전체 페이지 계산
        int totalItems = programRepository.countPrograms(keyword, status);
        int totalPages = (int) Math.ceil((double) totalItems / (double) size);
        ProgramPageResponse programPageResponse = new ProgramPageResponse();
        programPageResponse.setPage(page);
        programPageResponse.setSize(totalItems);
        programPageResponse.setTotalpages(totalPages);
        programPageResponse.setResults(programs);
        return programPageResponse;
    }

    public ProgramDetailResponseDto getProgramDetail(User user, long programId) {
        ProgramDetailResponseDto programDetail = programRepository.getProgramDetail(programId);

        // Availability 정보 조회
        List<AvailabilityDto> availability = programRepository.findAvailability(programId);
        programDetail.setAvailability(availability);

        // 사용자 로그인 시 위시리스트 정보 조회
        if (user != null) {
            Long wishlistId = programRepository.findWishList(user.getUserId(), programId);
            programDetail.setWishlistId(wishlistId);
        }

        return programDetail;
    }
}
