package main.services;

import lombok.Data;
import lombok.NoArgsConstructor;
import main.DTO.TagDto;
import main.model.Tag;
import main.repositories.PostRepository;
import main.repositories.TagsRepository;
import main.services.interfaces.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    private final TagsRepository tagsRepository;
    private final PostRepository postRepository;

    @Autowired
    public TagServiceImpl(TagsRepository tagsRepository, PostRepository postRepository) {
        this.tagsRepository = tagsRepository;
        this.postRepository = postRepository;
    }
    @Data
    @NoArgsConstructor
    public static class LocalTag{
        List<TagDto> tags = new ArrayList<>();
    }

    public LocalTag getTags(String query){
        LocalTag localTag = new LocalTag();
        List<Tag> list;
        if(query == null || query.equals("")){
            list = tagsRepository.findAllTags();
        }else{
            list = tagsRepository.findTagsByQuery(query);
        }
        double maxWeight = 0.3;
        localTag.tags = list.stream().map(e->{
            double postsCountByTag = e.getTagToPosts()
                    .stream()
                    .mapToLong(d->d.getTag().getId())
                    .count();
            double allPostsCount = postRepository.count();
            double weight = postsCountByTag / allPostsCount;
            weight = Double.parseDouble(new DecimalFormat("##.##").format(weight).replace(",", "."));

            return new TagDto(e.getName(), weight);
        }).collect(Collectors.toList())
        .stream().peek(j-> {
            if(j.getWeight() > maxWeight){
                j.setWeight(maxWeight);
            }
        }).peek(h->{
            double maxCoeff = 1 / maxWeight;
            h.setWeight(h.getWeight() * maxCoeff);
            if (h.getWeight() < 0.3){
                h.setWeight(0.3);
            }
                }).collect(Collectors.toList());
        return localTag;
    }
}
