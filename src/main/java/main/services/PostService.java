package main.services;

import main.DTO.PostDTOById.PostDtoById;
import main.mapper.PostMapper;
import main.DTO.ModePostDto;
import main.DTO.PostDto;
import main.DTO.PostDtoView;
import main.model.Post;
import main.model.PostComment;
import main.model.Tag;
import main.repositories.PostCommentsRepository;
import main.repositories.PostRepository;
import main.repositories.TagsRepository;
import main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final TagsRepository tagsRepository;

    private final PostMapper postMapper;

    private final PostCommentService postCommentService;
    private final UserService userService;
    private final PostCommentsRepository postCommentsRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, PostMapper postMapper,
                       TagsRepository tagsRepository, PostCommentService postCommentService,
                       UserService userService, PostCommentsRepository postCommentsRepository,
                       UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.postCommentService = postCommentService;
        this.tagsRepository = tagsRepository;
        this.userService = userService;
        this.postCommentsRepository = postCommentsRepository;
        this.userRepository = userRepository;
    }
//    public PostDto save(PostDto postDto){
//        return postMapper.toDto(postRepository.save(postMapper.toEntity(postDto)));
//    }
    public PostDto get(Post post){
        return postMapper.toDto(post);
    }

    public PostDtoById getPostById(int id){
        return postMapper.toDtoById(postRepository.getPostById(id));
    }

    public PostDtoView populateVars(int offset, int limit, ModePostDto mode){
        Pageable pageable = PageRequest.of(offset / limit, limit);
        PostDtoView postDtoView = new PostDtoView();
        postDtoView.setCount(postRepository.countPost());
        List<Post> list;
        if(mode.equals(ModePostDto.recent)){
            list = postRepository.findPostByDateAsc(pageable);
        }else if(mode.equals(ModePostDto.early)){
            list = postRepository.findPostByDateDesc(pageable);
        }else if (mode.equals(ModePostDto.popular)){
            list = postRepository.findPostByCommentCount(pageable);
        }else {
            list = postRepository.findPostByLikeCount(pageable);
        }
        postDtoView.setPosts(list.stream()
                .map(this::get)
                .collect(Collectors.toList()));
        return postDtoView;
    }

    public PostDtoView populateSearchVars(int offset, int limit, String query){
        Pageable pageable = PageRequest.of(offset / limit, limit);
        PostDtoView postDtoView = new PostDtoView();
        postDtoView.setCount(postRepository.findPostByDateAsc(pageable).size());
        List<Post> list;
        if(query == null || query.equals("")){
            list = postRepository.findPostByDateAsc(pageable);
        }else {
            list = postRepository.findPostBySearchQuery(pageable,query);
        }
        postDtoView.setPosts(list.stream()
                .map(this::get)
                .collect(Collectors.toList()));
        return postDtoView;
    }

    public PostDtoById populateVarsByPostId(int id){
        List<PostComment> postCommentList = postCommentsRepository.findPostCommentByPostId(id);
        List<Tag> tagList = tagsRepository.findTags();
        PostDtoById postDtoById = getPostById(id);

        postDtoById.setComments(postCommentList.stream()
                    .map(k->postCommentService.getCommentPostById(k.getId()))
                    .collect(Collectors.toList()));
        postDtoById.setTags(tagList.stream()
                    .map(Tag::getName)
                    .collect(Collectors.toList()));
        return postDtoById;
    }

    public PostDtoView populateVarsWithExactDate(int offset, int limit, String date){
        Pageable pageable = PageRequest.of(offset / limit, limit);
        PostDtoView postDtoView = new PostDtoView();
        postDtoView.setCount(postRepository.findPostByDateAsc(pageable).size());
        List<Post> list = postRepository.findPostWithExactDate(pageable, date);

        postDtoView.setPosts(list.stream()
                .map(this::get)
                .collect(Collectors.toList()));
        return postDtoView;
    }

    public PostDtoView populateTagVars(int offset, int limit, String tag){
        Pageable pageable = PageRequest.of(offset / limit, limit);
        PostDtoView postDtoView = new PostDtoView();
        postDtoView.setCount(postRepository.findPostByDateAsc(pageable).size());
        List<Post> list;
        if(tag == null || tag.equals("")){
            list = postRepository.findPostByDateAsc(pageable);
        }else {
           list = postRepository.findPostsByTag(pageable,tag);
        }
        postDtoView.setPosts(list.stream()
                .map(this::get)
                .collect(Collectors.toList()));
        return postDtoView;
    }
}
