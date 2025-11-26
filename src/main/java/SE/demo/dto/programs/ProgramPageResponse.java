package SE.demo.dto.programs;

import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProgramPageResponse {
    private int page;
    private int size;
    private int totalpages;
    private List<ProgramDetailResponseDto> results;
}
