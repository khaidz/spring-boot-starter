package net.khaibq.springbootstater.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.khaibq.springbootstater.utils.Constants;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BaseResponse<T> {
    private Integer status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(Constants.SUCCESS_STATUS, data, null);
    }

    public static <T> BaseResponse<T> fail(String message) {
        return new BaseResponse<>(Constants.FAILED_STATUS, null, message);
    }
}
