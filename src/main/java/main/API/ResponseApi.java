package main.API;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseApi<T> {
    private boolean result;
    private T user;
    private T errors;
    private String secret;
    private String image;
    private Integer id;

    public ResponseApi(boolean result) {
        this.result = result;
    }
}
