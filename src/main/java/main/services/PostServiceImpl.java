package main.services;

import lombok.extern.slf4j.Slf4j;
import main.API.RequestApi;
import main.API.ResponseApi;
import main.DTO.CalendarDto;
import main.DTO.ModePostDto;
import main.DTO.PostDto;
import main.DTO.PostDtoById.PostDtoById;
import main.DTO.PostDtoView;
import main.mapper.PostMapper;
import main.model.*;
import main.repositories.PostRepository;
import main.repositories.PostVotesRepository;
import main.repositories.TagToPostRepository;
import main.repositories.TagsRepository;
import main.services.interfaces.PostService;
import org.jsoup.Jsoup;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.toIntExact;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final PostCommentServiceImpl postCommentServiceImpl;
    private final SettingsServiceImpl settingsServiceImpl;
    private final PostVotesRepository postVotesRepository;
    private final TagToPostRepository tagToPostRepository;
    private final TagsRepository tagsRepository;
    private final PostRepository postRepository;
    private final UserServiceImpl userService;

    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper,
                           PostCommentServiceImpl postCommentServiceImpl,
                           SettingsServiceImpl settingsServiceImpl, PostVotesRepository postVotesRepository,
                           TagToPostRepository tagToPostRepository, TagsRepository tagsRepository, UserServiceImpl userService) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.postCommentServiceImpl = postCommentServiceImpl;
        this.settingsServiceImpl = settingsServiceImpl;
        this.postVotesRepository = postVotesRepository;
        this.tagToPostRepository = tagToPostRepository;
        this.tagsRepository = tagsRepository;
        this.userService = userService;
    }

    public PostDto mapPost(Post post) {
        return postMapper.toDto(post);
    }

    public PostDtoById mapPostById(Post post) {
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

    private PostDtoById getPostDtoById(int id, Post post) {
        PostDtoById postDtoById = mapPostById(post);
        List<PostComment> postCommentList = post.getPostComments();
        if(postCommentList.size() == 0){
            postCommentList = new ArrayList<>();
        }
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
                .map(k -> postCommentServiceImpl.mapCommentPostById(k.getId()))
                .collect(Collectors.toList()));
        postDtoById.setTags(tagResultList.stream()
                .map(Tag::getName)
                .collect(Collectors.toList()));
        postDtoById.setAnnounce(Jsoup.parse(post.getText()
                .substring(0, Math.min(post.getText().length(), 200))).text());
        log.info("Populate post by id: id:{}", id);
        return postDtoById;
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
        log.info("Populate all posts: offset:{} limit:{} mode:'{}'", offset, limit, mode);
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
        log.info("Populate posts with search: offset:{} limit:{} query:'{}'", offset, limit, query);
        return populateDtoViewWithStream(postDtoView, list);
    }

    @Transactional
    public PostDtoById populateVarsByPostIdWithUser(int id, String session) {
        Optional<Post> post = Optional.ofNullable(postRepository.getPostById(id));
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        boolean userAuthorized = userId.isPresent();
        if(post.isPresent()){
            boolean postIsActive = post.get().getIsActive() == 1 &&
                    post.get().getStatus().equals(Post.Status.ACCEPTED) &&
                    post.get().getTimestamp() <= new Date().getTime();
            if(userAuthorized){
                boolean sameUser = post.get().getUser().getId() == userId.get();
                boolean isModerator = userService.isModerator(userId.get());
                if(sameUser){
                    return getPostDtoById(id, post.get());
                }
                if(isModerator){
                    return getPostDtoById(id, post.get());
                }else {
                    if(postIsActive){
                        return getPostDtoById(id, post.get());
                    }
                }
            }else{
                if(postIsActive){
                    return getPostDtoById(id, post.get());
                }
            }
        }
        return new PostDtoById();
    }

    public PostDtoView populateVarsWithExactDate(int offset, int limit, String date) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        PostDtoView postDtoView = new PostDtoView();
        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String day = date.substring(8,10);

        List<Post> list = postRepository.findPostWithExactDate(pageable, year, month, day);
        postDtoView.setCount(list.size());
        log.info("Populate posts by date: offset:{} limit:{} date:'{}'", offset, limit, date);
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
        log.info("Populate all posts by tag: offset:{} limit:{} tag:'{}'", offset, limit, tag);
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
        log.info("Populate calendar: year:{}", year);
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
        log.info("Populate my profile: userId:{} offset:{} limit:{} status:'{}'", userId, offset, limit, status);
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
        log.info("Populate vars moderation: offset:{} limit:{} status:'{}'", offset, limit, status);
        return populateDtoViewWithStream(postDtoView, list);
    }
    @Transactional
    public ResponseEntity<ResponseApi> makeNewPost(RequestApi post, User user){
        int mintTitleLength = 3;
        int minTextLength = 50;
        long postDate = post.getTimestamp();
        long currentDate = new Date().getTime();
        boolean preModeration = settingsServiceImpl.getPreModeration();
        if(postDate < currentDate){
            post.setTimestamp(currentDate);
        }
        Map<String, String> errors = new HashMap<>(8);
        if(post.getTitle().length() <= mintTitleLength){
            errors.put("title", "Title is not set");
        }
        if(post.getText().length() <= minTextLength){
            errors.put("text", "Text is too short");
        }
        if(errors.size() == 0){
            Post newPost = new Post();
            newPost.setStatus(Post.Status.NEW);
            if(!preModeration){
                newPost.setStatus(Post.Status.ACCEPTED);
            }
            newPost.setText(post.getText());
            newPost.setTitle(post.getTitle());
            newPost.setUser(user);
            newPost.setViewCount(0);
            List<Tag> tagList = post.getTags().stream().map(Tag::new).collect(Collectors.toList());
            List<TagToPost> tagToPostList = tagList.stream().map(e ->
                    new TagToPost(new TagToPostKey(newPost.getId(), e.getId()))).collect(Collectors.toList());
            if (!tagToPostList.isEmpty() && !tagList.isEmpty()) {
                tagsRepository.saveAll(tagList);
                tagToPostList = tagToPostRepository.saveAll(tagToPostList);
            }
            newPost.setTagToPosts(tagToPostList);
            postRepository.save(newPost);
            log.info("Successfully created new post id: id:{}", newPost.getId());
            return new ResponseEntity<>(ResponseApi.builder().result(true).build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResponseApi.builder().result(false).errors(errors).build(), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseApi> changePost(int id, RequestApi post, User user){
        Post oldPost = postRepository.getOne(id);
        int mintTitleLength = 3;
        int minTextLength = 50;
        long postDate = post.getTimestamp();
        long currentDate = new Date().getTime();
        if(postDate < currentDate){
            post.setTimestamp(currentDate);
        }
        Map<String, String> errors = new HashMap<>(8);
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

            List<Tag> tagList = post.getTags().stream().map(Tag::new).collect(Collectors.toList());
            List<Tag> existsTags = tagsRepository.findTagsByNameIn(post.getTags());
            List<Tag> forSave = tagList.stream().filter(e -> !existsTags.contains(e)).collect(Collectors.toList());
            if (!forSave.isEmpty()) {
                tagsRepository.saveAll(forSave);
            }
            tagList = Stream.concat(existsTags.stream(), forSave.stream()).collect(Collectors.toList());
            List<TagToPost> tagToPostList = tagList.stream().map(e ->
                    new TagToPost(new TagToPostKey(oldPost.getId(), e.getId()))).collect(Collectors.toList());
            List<TagToPost> listForRemove = new ArrayList<>();
            for (TagToPost tagToPost : oldPost.getTagToPosts()) {
                if (!tagToPostList.contains(tagToPost)) {
                    listForRemove.add(tagToPost);
                }
            }
            List<TagToPost> listForSave = new ArrayList<>();
            for (TagToPost tagToPost : tagToPostList) {
                if (!oldPost.getTagToPosts().contains(tagToPost)) {
                    listForSave.add(tagToPost);
                }
            }
            if (!listForRemove.isEmpty()) {
                tagToPostRepository.deleteAll(listForRemove);
            }
            if (!listForSave.isEmpty()) {
                tagToPostList = tagToPostRepository.saveAll(listForSave);
            }

            oldPost.setTagToPosts(tagToPostList);
            oldPost.setText(post.getText());
            postRepository.save(oldPost);
            return new ResponseEntity<>(ResponseApi.builder().result(true).build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResponseApi.builder().result(false).errors(errors).build(), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseApi> makeNewLike(int postId, User user){
        Post post = postRepository.getPostById(postId);
        int userId = user.getId();
        Optional<PostVote> postVote = postVotesRepository.getLikeByUserAndPost(postId, userId);
        if(postVote.isPresent()){
            if(postVote.get().getValue() == 1){
                return new ResponseEntity<>(ResponseApi.builder().result(false).build(), HttpStatus.OK);
            }else {
                postVotesRepository.save(postVote.get());
                return new ResponseEntity<>(ResponseApi.builder().result(true).build(), HttpStatus.OK);
            }
        }else {
            PostVote postVoteNew = new PostVote();
            postVoteNew.setPost(post);
            postVoteNew.setTimestamp(new Date().getTime());
            postVoteNew.setUser(user);
            postVoteNew.setValue(1);
            postVotesRepository.save(postVoteNew);
            return new ResponseEntity<>(ResponseApi.builder().result(true).build(), HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity<ResponseApi> makeNewDisLike(int postId, User user){
        Post post = postRepository.getPostById(postId);
        int userId = user.getId();
        Optional<PostVote> postVote = postVotesRepository.getLikeByUserAndPost(postId, userId);
        if(postVote.isPresent()){
            if(postVote.get().getValue() == -1){
                return new ResponseEntity<>(ResponseApi.builder().result(false).build(), HttpStatus.OK);
            }else {
                postVotesRepository.save(postVote.get());
                return new ResponseEntity<>(ResponseApi.builder().result(true).build(), HttpStatus.OK);
            }
        }else {
            PostVote postVoteNew = new PostVote();
            postVoteNew.setPost(post);
            postVoteNew.setTimestamp(new Date().getTime());
            postVoteNew.setUser(user);
            postVoteNew.setValue(-1);
            postVotesRepository.save(postVoteNew);
            return new ResponseEntity<>(ResponseApi.builder().result(true).build(), HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseApi postModeration(RequestApi requestApi, User user){
        Integer postId = requestApi.getPostId();
        Post post = postRepository.getPostById(postId);
        int userId = user.getId();

        post.setModeratorId(userId);
        if(requestApi.getDecision().equals("accept")){
            post.setStatus(Post.Status.ACCEPTED);
        }else {
            post.setStatus(Post.Status.DECLINED);
        }
        postRepository.save(post);
        return ResponseApi.builder().result(true).build();
    }
}
