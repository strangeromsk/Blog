package main.services;

import main.API.RequestApi;
import main.DTO.CalendarDto;
import main.DTO.PostDtoById.PostDtoById;
import main.API.ResponseApi;
import main.mapper.PostMapper;
import main.DTO.ModePostDto;
import main.DTO.PostDto;
import main.DTO.PostDtoView;
import main.model.*;
import main.repositories.PostRepository;
import main.repositories.PostVotesRepository;
import main.repositories.TagsRepository;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static java.lang.Math.toIntExact;

import javax.transaction.*;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final PostCommentService postCommentService;
    private final UserService userService;
    private final PostVotesRepository postVotesRepository;
    private final TagsRepository tagsRepository;

    @Autowired
    public PostService(PostRepository postRepository, PostMapper postMapper,
                       PostCommentService postCommentService, UserService userService, PostVotesRepository postVotesRepository, TagsRepository tagsRepository) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.postCommentService = postCommentService;
        this.userService = userService;
        this.postVotesRepository = postVotesRepository;
        this.tagsRepository = tagsRepository;
    }

    public PostDto mapPost(Post post) {
        PostDto.disableMapper();
        return postMapper.toDto(post);
    }

    public PostDtoById mapPostById(Post post) {
        PostDto.disableMapper();
        return postMapper.toDtoById(post);
    }

    private PostDtoView populateDtoViewWithStream(PostDtoView postDtoView, List<Post> list) {
        postDtoView.setPosts(list.stream()
                .map(k -> {
                    int commentCount = k.getPostComments().size();
                    long likes = k.getPostVotes().stream().filter(l -> l.getValue() == 1).count();
                    long dislikes = k.getPostVotes().stream().filter(l -> l.getValue() == -1).count();
                    PostDto postDto = mapPost(k);
                    postDto.setLikeCount(toIntExact(likes));
                    postDto.setDislikeCount(toIntExact(dislikes));
                    postDto.setCommentCount(commentCount);
                    postDto.setAnnounce(Jsoup.parse(k.getText().substring(0, Math.min(k.getText().length(), 200))).text());
                    return postDto;
                })
                .collect(Collectors.toList()));
        return postDtoView;
    }

    public PostDtoView populateVars(int offset, int limit, ModePostDto mode) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        PostDtoView postDtoView = new PostDtoView();
        postDtoView.setCount(postRepository.countPost());
        List<Post> list;
        if (mode.equals(ModePostDto.early)) {
            list = postRepository.findPostByDateAsc(pageable);
        } else if (mode.equals(ModePostDto.recent)) {
            list = postRepository.findPostByDateDesc(pageable);
        } else if (mode.equals(ModePostDto.popular)) {
            list = postRepository.findPostByCommentCount(pageable);
        } else {
            list = postRepository.findPostByLikeCount(pageable);
        }
        return populateDtoViewWithStream(postDtoView, list);
    }

    public PostDtoView populateSearchVars(int offset, int limit, String query) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        PostDtoView postDtoView = new PostDtoView();
        postDtoView.setCount(postRepository.countPostWithSearchQuery(query));
        List<Post> list;
        if (query == null || query.equals("")) {
            list = postRepository.findPostByDateAsc(pageable);
        } else {
            list = postRepository.findPostBySearchQuery(pageable, query);
        }
        return populateDtoViewWithStream(postDtoView, list);
    }

    @Transactional
    public PostDtoById populateVarsByPostId(int id) {
        postRepository.updateViewCount(id);

        Post post = postRepository.getPostById(id);
        PostDtoById postDtoById = mapPostById(post);
        List<PostComment> postCommentList = post.getPostComments();
        List<TagToPost> tagToPost = post.getTagToPosts();
        List<Tag> tagResultList = new ArrayList<>();
        if (tagToPost != null) {
            tagResultList = post.getTagToPosts()
                    .stream()
                    .map(TagToPost::getTag)
                    .collect(Collectors.toList());
        }
        postDtoById.setCommentCount(postCommentList.size());
        postDtoById.setLikeCount(toIntExact(post.getPostVotes().stream()
                .filter(e -> e.getValue() == 1).count()));
        postDtoById.setDislikeCount(toIntExact(post.getPostVotes().stream()
                .filter(e -> e.getValue() == -1).count()));
        postDtoById.setComments(postCommentList.stream()
                .map(k -> postCommentService.mapCommentPostById(k.getId()))
                .collect(Collectors.toList()));
        postDtoById.setTags(tagResultList.stream()
                .map(Tag::getName)
                .collect(Collectors.toList()));
        postDtoById.setAnnounce(Jsoup.parse(post.getText()
                .substring(0, Math.min(post.getText().length(), 200))).text());
        return postDtoById;
    }

    public PostDtoView populateVarsWithExactDate(int offset, int limit, String date) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        PostDtoView postDtoView = new PostDtoView();
        postDtoView.setCount(postRepository.countPostWithExactDate(date));
        List<Post> list = postRepository.findPostWithExactDate(pageable, date);
        return populateDtoViewWithStream(postDtoView, list);
    }

    public PostDtoView populateTagVars(int offset, int limit, String tag) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        PostDtoView postDtoView = new PostDtoView();
        postDtoView.setCount(postRepository.countPostWithTag(tag));
        List<Post> list;
        if (tag == null || tag.equals("")) {
            list = postRepository.findPostByDateAsc(pageable);
        } else {
            list = postRepository.findPostsByTag(pageable, tag);
        }
        return populateDtoViewWithStream(postDtoView, list);
    }

    public CalendarDto populateCalendarVars(Integer year) {
        CalendarDto calendarDto = new CalendarDto();
        Map<String, Integer> postsMap = new HashMap<>();
        if(year == null || year == 0){
            year = Calendar.getInstance().get(Calendar.YEAR);
        }
        postRepository.getPostsByYears(year)
                .forEach(e-> postsMap.put((String) e.get(0), ((BigInteger) e.get(1)).intValue()));
        List<String> postsYearsList = postsMap.keySet()
                .stream()
                .map(e->e.substring(0,4))
                .distinct()
                .collect(Collectors.toList());
        calendarDto.setPosts(postsMap);
        calendarDto.setYears(postsYearsList);
        return calendarDto;
    }

    public PostDtoView populateMyVars(int userId, int offset, int limit, Post.Status status) {
        List<Post> list;
        PostDtoView postDtoView = new PostDtoView();
        Pageable pageable = PageRequest.of(offset / limit, limit);
        postDtoView.setCount(postRepository.countMyPosts(userId));
        if (status.equals(Post.Status.inactive)) {
            list = postRepository.findInactivePosts(pageable, userId);
        } else if (status.equals(Post.Status.pending)) {
            list = postRepository.findPendingPosts(pageable, userId);
        } else if (status.equals(Post.Status.declined)) {
            list = postRepository.findDeclinedPosts(pageable, userId);
        } else {
            list = postRepository.findAcceptedPosts(pageable, userId);
        }
        return populateDtoViewWithStream(postDtoView, list);
    }

    public PostDtoView populateVarsModeration(int offset, int limit, String status) {
        List<Post> list;
        PostDtoView postDtoView = new PostDtoView();
        Pageable pageable = PageRequest.of(offset / limit, limit);
        postDtoView.setCount(postRepository.countPost());
        if (status.equals("new")) {
            list = postRepository.findPendingPostsModeration(pageable);
        } else if (status.equals("accepted")) {
            list = postRepository.findAcceptedPostsModeration(pageable);
        } else {
            list = postRepository.findDeclinedPostsModeration(pageable);
        }
        return populateDtoViewWithStream(postDtoView, list);
    }
    @Transactional
    public ResponseEntity<ResponseApi> makeNewPost(Post post, User user){
        int mintTitleLength = 3;
        int minTextLength = 50;
        Date postDate = post.getTime();
        Date currentDate = new Date();
        if(postDate.before(currentDate)){
            post.setTime(currentDate);
        }
        HashMap<String, String> errors = new HashMap<>(4);
        if(post.getTitle().length() <= mintTitleLength){
            errors.put("title", "Title is not set");
        }
        if(post.getText().length() <= minTextLength){
            errors.put("text", "Text is too short");
        }
        if(errors.size() == 0){
            post.setStatus(Post.Status.NEW);
            post.setUser(user);
            post.setViewCount(0);
            postRepository.save(post);
            return new ResponseEntity<>(ResponseApi.builder().result("true").build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResponseApi.builder().result("false").errors(errors).build(), HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<ResponseApi> changePost(int id, RequestApi post, User user){
        Post oldPost = postRepository.getOne(id);
        int mintTitleLength = 3;
        int minTextLength = 50;
        Date postDate = post.getTime();
        Date currentDate = new Date();
        if(postDate.before(currentDate)){
            post.setTime(currentDate);
        }
        HashMap<String, String> errors = new HashMap<>(4);
        if(post.getTitle().length() <= mintTitleLength){
            errors.put("title", "Title is not set");
        }
        if(post.getText().length() <= minTextLength){
            errors.put("text", "Text is too short");
        }
        if(errors.size() == 0){
            if(user.getIsModerator() == 0){
                post.setStatus(Post.Status.NEW);
            }
            oldPost.setIsActive(post.getActive());
            oldPost.setTitle(post.getTitle());

            //List<Tag> tagList = post.getTags().stream().map(Tag::new).collect(Collectors.toList());
            //List<Tag> tagList = new ArrayList<>();

            List<TagToPost> tagToPostList = oldPost.getTagToPosts()
                    .stream()
                    .peek(e-> post.getTags().forEach(k->{
                        Tag tag = new Tag(k);
                        tagsRepository.save(tag);
                        e.setTag(tag);
                    }))
                    .collect(Collectors.toList());

            oldPost.setTagToPosts(tagToPostList);
            oldPost.setText(post.getText());
            //tagsRepository.saveAll(tagList);
            postRepository.save(oldPost);
            return new ResponseEntity<>(ResponseApi.builder().result("true").build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResponseApi.builder().result("false").errors(errors).build(), HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<ResponseApi> makeNewLike(int postId, User user){
        Post post = postRepository.getPostById(postId);
        int userId = user.getId();
        Optional<PostVote> postVote = postVotesRepository.getLikeByUserAndPost(postId, userId);
        if(postVote.isPresent()){
            if(postVote.get().getValue() == 1){
                return new ResponseEntity<>(ResponseApi.builder().result("false").build(), HttpStatus.OK);
            }else {
                postVotesRepository.save(postVote.get());
                return new ResponseEntity<>(ResponseApi.builder().result("true").build(), HttpStatus.OK);
            }
        }else {
            PostVote postVoteNew = new PostVote();
            postVoteNew.setPost(post);
            postVoteNew.setTime(new Date());
            postVoteNew.setUser(user);
            postVoteNew.setValue(1);
            postVotesRepository.save(postVoteNew);
            return new ResponseEntity<>(ResponseApi.builder().result("true").build(), HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity<ResponseApi> makeNewDisLike(int postId, User user){
        Post post = postRepository.getPostById(postId);
        int userId = user.getId();
        Optional<PostVote> postVote = postVotesRepository.getLikeByUserAndPost(postId, userId);
        if(postVote.isPresent()){
            if(postVote.get().getValue() == -1){
                return new ResponseEntity<>(ResponseApi.builder().result("false").build(), HttpStatus.OK);
            }else {
                postVotesRepository.save(postVote.get());
                return new ResponseEntity<>(ResponseApi.builder().result("true").build(), HttpStatus.OK);
            }
        }else {
            PostVote postVoteNew = new PostVote();
            postVoteNew.setPost(post);
            postVoteNew.setTime(new Date());
            postVoteNew.setUser(user);
            postVoteNew.setValue(-1);
            postVotesRepository.save(postVoteNew);
            return new ResponseEntity<>(ResponseApi.builder().result("true").build(), HttpStatus.OK);
        }
    }
}
