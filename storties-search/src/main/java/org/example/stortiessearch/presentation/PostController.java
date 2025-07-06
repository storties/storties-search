package org.example.stortiessearch.presentation;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.service.AddLikeService;
import org.example.stortiessearch.application.service.CreatePostService;
import org.example.stortiessearch.application.service.DeletePostService;
import org.example.stortiessearch.application.service.QueryPostService;
import org.example.stortiessearch.application.service.QueryPostsService;
import org.example.stortiessearch.application.service.UnLikeService;
import org.example.stortiessearch.application.service.UpdatePostService;
import org.example.stortiessearch.application.service.dto.request.CreatePostRequest;
import org.example.stortiessearch.application.service.dto.request.UpdatePostRequest;
import org.example.stortiessearch.application.service.dto.response.PostResponse;
import org.example.stortiessearch.application.service.dto.response.PostsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final AddLikeService addLikeService;

    private final UnLikeService unLikeService;

    private final CreatePostService createPostService;

    private final DeletePostService deletePostService;

    private final QueryPostService queryPostService;

    private final QueryPostsService queryPostsService;

    private final UpdatePostService updatePostService;

    // 게시물 단건 조회 (조회수 증가 포함)
    @GetMapping("/{postId}")
    @ResponseStatus(code = HttpStatus.OK)
    public PostResponse getPost(@PathVariable Long postId) {
        return queryPostService.execute(postId);
    }

    // 게시물 목록 조회 (페이징)
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public PostsResponse getPosts(@RequestParam(defaultValue = "0") int page) {
        return queryPostsService.execute(page);
    }

    // 게시물 생성
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createPost(@RequestBody CreatePostRequest request) {
        createPostService.execute(request);
    }

    // 게시물 수정
    @PatchMapping("/{postId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest request) {
        updatePostService.execute(postId, request);
    }

    // 게시물 삭제
    @DeleteMapping("/{postId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long postId) {
        deletePostService.execute(postId);
    }

    // 게시물 좋아요 추가
    @PostMapping("/{postId}/like")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void likePost(@PathVariable Long postId) {
        addLikeService.execute(postId);
    }

    // 게시물 좋아요 취소
    @DeleteMapping("/{postId}/like")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void unlikePost(@PathVariable Long postId) {
        unLikeService.execute(postId);
    }
}
