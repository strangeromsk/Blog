package main.API;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.DTO.ModePostDto;
import main.model.Post;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestApi {
    @JsonProperty("post_id")
    private Integer postId;
    private String decision;

    private int offset;
    private int limit;
    private Post.Status status;
    private ModePostDto mode;
    private String query;
    private String date;
    private String tag;

    private long timestamp;
    private byte active;
    private String title;
    private List<String> tags;
    private String text;
}
