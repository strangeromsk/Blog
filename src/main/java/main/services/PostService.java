package main.services;

import main.DTO.PostDTOById.CommentDtoById;
import main.DTO.PostDTOById.PostDtoById;
import main.DTO.PostDTOById.UserDtoById;
import main.DTO.moderation.PostDtoViewModeration;
import main.mapper.CommentMapper;
import main.mapper.PostMapper;
import main.mapper.UserMapper;
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

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostCommentsRepository postCommentsRepository;
    private final TagsRepository tagsRepository;

    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public PostService(PostRepository postRepository, PostMapper postMapper,
                       UserRepository userRepository, PostCommentsRepository postCommentsRepository,
                       TagsRepository tagsRepository, UserMapper userMapper, CommentMapper commentMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userRepository = userRepository;
        this.postCommentsRepository = postCommentsRepository;
        this.tagsRepository = tagsRepository;
        this.userMapper = userMapper;
        this.commentMapper = commentMapper;
    }

    public PostDto save(PostDto postDto){
        return postMapper.toDto(postRepository.save(postMapper.toEntity(postDto)));
    }

    public PostDto get(Long id){
        return postMapper.toDto(postRepository.getOne(id));
    }

    public UserDtoById getUserById(Long id){
        return userMapper.toDtoById(userRepository.getOne(id));
    }

    public PostDtoById getPostById(Long id){
        return postMapper.toDtoById(postRepository.getOne(id));
    }

    public CommentDtoById getCommentPostById(Long id){
        return commentMapper.toDto(postCommentsRepository.getOne(id));
    }

    public PostDtoView populateVars(int offset, int limit, ModePostDto mode){
        Pageable pageable = PageRequest.of(offset / limit, limit);
        PostDtoView postDtoView = new PostDtoView();
        postDtoView.setCount(postRepository.findPostByDateAsc(pageable).size());
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
                .map(e->get(e.getId()))
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
                .map(e->get(e.getId()))
                .collect(Collectors.toList()));
        return postDtoView;
    }

    public PostDtoById populateVarsByPostId(int id){
        List<PostComment> postCommentList = postCommentsRepository.findPostCommentByPostId(id);
        List<Tag> tagList = tagsRepository.findTags();
        PostDtoById postDtoById = getPostById((long) id);

        postDtoById.setComments(postCommentList.stream()
                    .map(e->getCommentPostById((long) e.getId()))
                    .collect(Collectors.toList()));
        postDtoById.setTags(tagList.stream()
                    .map(Tag::getName)
                    .collect(Collectors.toList()));
        return postDtoById;
    }

    public PostDtoView populateVarsWithExactDate(int offset, int limit, Date date){
        Pageable pageable = PageRequest.of(offset / limit, limit);
        PostDtoView postDtoView = new PostDtoView();
        postDtoView.setCount(postRepository.findPostByDateAsc(pageable).size());
        java.sql.Date sDate = new java.sql.Date(date.getTime());
        List<Post> list = postRepository.findPostWithExactDate(pageable, sDate);

        postDtoView.setPosts(list.stream()
                .map(e->get(e.getId()))
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
                .map(e->get(e.getId()))
                .collect(Collectors.toList()));
        return postDtoView;
    }

//    public PostDtoViewModeration populateVarsModeraton(int offset, int limit, Post.Status status){
//        Pageable pageable = PageRequest.of(offset / limit, limit);
//        PostDtoViewModeration postDtoViewModeration = new PostDtoViewModeration();
//        postDtoViewModeration.setCount(postRepository.findPostByDateAsc(pageable).size());
//        List<Post> list;
//        if(status.equals(Post.Status.NEW)){
//            list = postRepository.findPostByDateAsc(pageable);
//        }
//        postDtoViewModeration.setPosts(list.stream()
//                .map(e->get(e.getId()))
//                .collect(Collectors.toList()));
//        return postDtoViewModeration;
//    }
}
