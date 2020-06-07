package main.API;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.DTO.ModePostDto;
import main.model.Post;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestApi {
    private int offset;
    private int limit;
    private Post.Status status;
    private ModePostDto mode;
    private String query;
    private String date;
    private String tag;
}
