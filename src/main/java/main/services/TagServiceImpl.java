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
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    private final TagsRepository tagsRepository;
    private final PostRepository postRepository;

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
        double weightInitialValue = 0.3;
        LocalTag localTag = new LocalTag();
        List<Tag> list;
        if(query == null || query.equals("")){
            list = tagsRepository.findTagsOnActivePosts();
        }else{
            list = tagsRepository.findTagsByQuery(query);
        }
        AtomicReference<Double> maxWeight = new AtomicReference<>(weightInitialValue);
        localTag.tags = list.stream().map(e->{
            double postsCountByTag = e.getTagToPosts()
                    .stream()
                    .mapToLong(d->d.getTag().getId())
                    .count();
            double allPostsCount = postRepository.count();
            double weight = postsCountByTag / allPostsCount;
            weight = Double.parseDouble(new DecimalFormat("##.##").format(weight).replace(",", "."));
            //if(e.getTagToPosts().isEmpty()){return new TagDto();}
            return new TagDto(e.getName(), weight);
        }).collect(Collectors.toList())
        .stream().peek(j-> {
            if(j.getWeight() > maxWeight.get()){
                maxWeight.set(j.getWeight());
            }
        }).peek(h->{
            double maxCoeff = 1 / maxWeight.get();
            h.setWeight(h.getWeight() * maxCoeff);
            if (h.getWeight() < weightInitialValue){
                h.setWeight(weightInitialValue);
            }
        }).collect(Collectors.toList());
        return localTag;
    }
}
