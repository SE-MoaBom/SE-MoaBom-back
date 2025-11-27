package SE.demo.controller.programs;

import SE.demo.dto.programs.ProgramDetailResponseDto;
import SE.demo.dto.programs.ProgramPageResponse;
import SE.demo.entity.User;
import SE.demo.exception.program.CannotFindProgramInfo;
import SE.demo.service.programs.ProgramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Program")
public class ProgramController {
    private final ProgramService programService;

    @GetMapping("/programs")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "프로그램 목록 조회", description = "프로그램 목록을 조회합니다.")
    public ResponseEntity<ProgramPageResponse> getPrograms(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", defaultValue = "ALL") String status,
            @RequestParam(value = "sort", defaultValue = "ID") String sort,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        ProgramPageResponse response = programService.getProgramInfo(user, keyword, status, sort, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/programs/{programId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "프로그램 상세 조회", description = "프로그램 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ProgramDetailResponseDto.class)))
    @ApiResponse(responseCode = "404", content = @Content(), description = "Not Found")
    public ResponseEntity<?> getDetailProgram(
            @AuthenticationPrincipal User user,
            @PathVariable long programId
    ) {
        try {
            ProgramDetailResponseDto programDetail = programService.getProgramDetail(user, programId);
            return ResponseEntity.ok(programDetail);
        } catch (CannotFindProgramInfo e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
