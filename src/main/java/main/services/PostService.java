package main.services;

import main.DTO.CalendarDto.CalendarDto;
import main.DTO.PostDTOById.PostDtoById;
import main.mapper.PostMapper;
import main.DTO.ModePostDto;
import main.DTO.PostDto;
import main.DTO.PostDtoView;
import main.model.Post;
import main.model.PostComment;
import main.model.Tag;
import main.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static java.lang.Math.toIntExact;

import javax.persistence.*;
import javax.transaction.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final PostCommentService postCommentService;
    private final UserService userService;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public PostService(PostRepository postRepository, PostMapper postMapper,
                       PostCommentService postCommentService, UserService userService) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.postCommentService = postCommentService;
        this.userService = userService;
    }
    public PostDto mapPost(Post post){
        PostDto.disableMapper();
        return postMapper.toDto(post);
    }

    public PostDtoById mapPostById(Post post){
        PostDto.disableMapper();
        return postMapper.toDtoById(post);
    }

    private PostDtoView populateDtoViewWithStream(PostDtoView postDtoView, List<Post> list){
        postDtoView.setPosts(list.stream()
                .map(k->{
                    int commentCount = k.getPostComments().size();
                    long likes = k.getPostVotes().stream().filter(l->l.getValue() == 1).count();
                    long dislikes = k.getPostVotes().stream().filter(l->l.getValue() == -1).count();
                    PostDto postDto = mapPost(k);
                    postDto.setLikeCount(toIntExact(likes));
                    postDto.setDislikeCount(toIntExact(dislikes));
                    postDto.setCommentCount(commentCount);
                    postDto.setAnnounce(k.getText().substring(0, Math.min(k.getText().length(), 200)));
                    return postDto;
                })
                .collect(Collectors.toList()));
        return postDtoView;
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
        return populateDtoViewWithStream(postDtoView, list);
    }

    public PostDtoView populateSearchVars(int offset, int limit, String query){
        Pageable pageable = PageRequest.of(offset / limit, limit);
        PostDtoView postDtoView = new PostDtoView();
        postDtoView.setCount(postRepository.countPostWithSearchQuery(query));
        List<Post> list;
        if(query == null || query.equals("")){
            list = postRepository.findPostByDateAsc(pageable);
        }else {
            list = postRepository.findPostBySearchQuery(pageable,query);
        }
        return populateDtoViewWithStream(postDtoView, list);
    }
    @Transactional
    public PostDtoById populateVarsByPostId(int id) {
        int count = entityManager.createQuery("UPDATE Post p SET viewCount = viewCount + 1 WHERE p.id =:id")
                .setParameter("id", id).executeUpdate();

        Post post = postRepository.getPostById(id);
        PostDtoById postDtoById = mapPostById(post);
        List<PostComment> postCommentList = post.getPostComments();
        List<Tag> tagResultList = post.getTagToPost().getTags();
        postDtoById.setCommentCount(postCommentList.size());
        postDtoById.setLikeCount(toIntExact(post.getPostVotes().stream()
                    .filter(e->e.getValue() == 1).count()));
        postDtoById.setDislikeCount(toIntExact(post.getPostVotes().stream()
                    .filter(e->e.getValue() == -1).count()));
        postDtoById.setComments(postCommentList.stream()
                    .map(k->postCommentService.mapCommentPostById(k.getId()))
                    .collect(Collectors.toList()));
        postDtoById.setTags(tagResultList.stream()
                    .map(Tag::getName)
                    .collect(Collectors.toList()));
        postDtoById.setAnnounce(post.getText()
                .substring(0, Math.min(post.getText().length(), 200)));
        return postDtoById;
    }

    public PostDtoView populateVarsWithExactDate(int offset, int limit, String date){
        Pageable pageable = PageRequest.of(offset / limit, limit);
        PostDtoView postDtoView = new PostDtoView();
        postDtoView.setCount(postRepository.countPostWithExactDate(date));
        List<Post> list = postRepository.findPostWithExactDate(pageable, date);
        return populateDtoViewWithStream(postDtoView, list);
    }

    public PostDtoView populateTagVars(int offset, int limit, String tag){
        Pageable pageable = PageRequest.of(offset / limit, limit);
        PostDtoView postDtoView = new PostDtoView();
        postDtoView.setCount(postRepository.countPostWithTag(tag));
        List<Post> list;
        if(tag == null || tag.equals("")){
            list = postRepository.findPostByDateAsc(pageable);
        }else {
           list = postRepository.findPostsByTag(pageable,tag);
        }
        return populateDtoViewWithStream(postDtoView, list);
    }

//    public CalendarDto populateCalendarVars(int year){
//        Date date = null; // your date
//        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
//        cal.setTime(date);
//        int year = cal.get(Calendar.YEAR);
//        int month = cal.get(Calendar.MONTH);
//        int day = cal.get(Calendar.DAY_OF_MONTH);
//        return CalendarDto;
//    }
}
