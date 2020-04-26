package main.model.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostDtoView {
    @Getter
    @Setter
    long count;
    @Getter
    @Setter
    List<PostDto> posts;

//    public Collection<PostDto> getAll() {
//        return posts.stream()
//                .filter(Objects::nonNull)
//                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
//    }

//    List<PostDto> posts = Collections.singletonList((PostDto) postRepository.findAll());

//    public void populateList(){
//        Iterable<Post> postIterable = postRepository.findAll();
//        for(int i = 0; i < count; i++) {
//            posts.add(postIterable.);
//        }
//    }
}
