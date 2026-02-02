package elementary.Rutix.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ResponseDto {
    private boolean status;
    private String message;
    private Map<String, String> listValidationErrors = new HashMap<>();
    private String error;
    private Object data;
    private Date timestamp;
}
