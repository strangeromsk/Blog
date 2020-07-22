package main.services.interfaces;

import main.services.TagServiceImpl;

public interface TagService {
    TagServiceImpl.LocalTag getTags(String query);
}
