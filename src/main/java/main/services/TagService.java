package main.services;

import main.DTO.TagDto;
import main.model.Tag;
import main.repositories.PostRepository;
import main.repositories.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {
    private final TagsRepository tagsRepository;
    private final PostRepository postRepository;

    @Autowired
    public TagService(TagsRepository tagsRepository, PostRepository postRepository) {
        this.tagsRepository = tagsRepository;
        this.postRepository = postRepository;
    }

    public List<TagDto> getTags(String query){
        List<Tag> list;
        if(query == null || query.equals("")){
            list = tagsRepository.findAllTags();
        }else{
            list = tagsRepository.findTagsByQuery(query);
        }
        return list.stream().map(e->{
            long postsCountByTag = e.getTagToPost().getPosts().size();
            long allPostsCount = postRepository.count();
            double weight = (double)(postsCountByTag / allPostsCount);

            return new TagDto(e.getName(), weight);
        }).collect(Collectors.toList());
    }
}
