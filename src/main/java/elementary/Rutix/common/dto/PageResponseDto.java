package elementary.Rutix.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponseDto<T> {
    private List<T> data;
    private int page;
    private int size;
    private long total;
    private boolean hasNext;
}