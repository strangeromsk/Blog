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
    private T id;
    private T result;
    private T user;
    private T errors;
    private T secret;
    private T image;

    public ResponseApi(T result) {
        this.result = result;
    }
}
