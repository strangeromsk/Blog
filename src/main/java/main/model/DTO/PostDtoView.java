package main.model.DTO;

import lombok.Data;
import main.model.Post;
import main.model.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class PostDtoView {
    @Autowired
    private PostRepository postRepository;
    long count = postRepository.count();

    List<PostDto> posts = Collections.singletonList((PostDto) postRepository.findAll());

//    public void populateList(){
//        Iterable<Post> postIterable = postRepository.findAll();
//        for(int i = 0; i < count; i++) {
//            posts.add(postIterable.);
//        }
//    }
}
