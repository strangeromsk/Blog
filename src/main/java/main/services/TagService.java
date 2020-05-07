package main.services;

import main.model.Tag;
import main.repositories.TagsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TagService {
    private final TagsRepository tagsRepository;

    public TagService(TagsRepository tagsRepository) {
        this.tagsRepository = tagsRepository;
    }

    public List<Tag> getTags(){
        return tagsRepository.findTags();
    }
}
