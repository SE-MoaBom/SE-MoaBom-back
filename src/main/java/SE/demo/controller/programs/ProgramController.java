package SE.demo.controller.programs;

import SE.demo.dto.programs.ProgramDetailResponseDto;
import SE.demo.dto.programs.ProgramPageResponse;
import SE.demo.service.programs.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ProgramController {
    private final ProgramService programService;

    @GetMapping("/programs")
    public ResponseEntity<?> getPrograms(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", defaultValue = "ALL") String status,
            @RequestParam(value = "sort", defaultValue = "ID") String sort,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        ProgramPageResponse response = programService.getProgramInfo(token, keyword, status, sort, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/programs/{programId}")
    public ResponseEntity<?> getDetaiProgram(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable long programId
    ) {
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        ProgramDetailResponseDto programDetail = programService.getProgramDetail(token, programId);
        return ResponseEntity.ok(programDetail);
    }
}
