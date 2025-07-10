package org.example.stortiessearch.presentation;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.usecase.AddLikeUseCase;
import org.example.stortiessearch.application.usecase.CreatePostUseCase;
import org.example.stortiessearch.application.usecase.DeletePostUseCase;
import org.example.stortiessearch.application.usecase.QueryPostUseCase;
import org.example.stortiessearch.application.usecase.QueryPostsUseCase;
import org.example.stortiessearch.application.usecase.UnLikeUseCase;
import org.example.stortiessearch.application.usecase.UpdatePostUseCase;
import org.example.stortiessearch.application.usecase.dto.request.CreatePostRequest;
import org.example.stortiessearch.application.usecase.dto.request.UpdatePostRequest;
import org.example.stortiessearch.application.usecase.dto.response.PostDetailResponse;
import org.example.stortiessearch.application.usecase.dto.response.PostsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final AddLikeUseCase addLikeUsecase;

    private final UnLikeUseCase unLikeUseCase;

    private final CreatePostUseCase createPostUsecase;

    private final DeletePostUseCase deletePostUseCase;

    private final QueryPostUseCase queryPostUseCase;

    private final QueryPostsUseCase queryPostsUseCase;

    private final UpdatePostUseCase updatePostUseCase;

    // 게시물 단건 조회 (조회수 증가 포함)
    @GetMapping("/{post_id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PostDetailResponse getPost(@PathVariable("post_id") Long postId) {
        return queryPostUseCase.execute(postId);
    }

    // 게시물 목록 조회 (페이징)
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public PostsResponse getPosts(@RequestParam(defaultValue = "0") int page) {
        return queryPostsUseCase.execute(page);
    }

    // 게시물 생성
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createPost(@RequestBody CreatePostRequest request) {
        createPostUsecase.execute(request);
    }

    // 게시물 수정
    @PatchMapping("/{postId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest request) {
        updatePostUseCase.execute(postId, request);
    }

    // 게시물 삭제
    @DeleteMapping("/{postId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long postId) {
        deletePostUseCase.execute(postId);
    }

    // 게시물 좋아요 추가
    @PostMapping("/{postId}/like")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void likePost(@PathVariable Long postId) {
        addLikeUsecase.execute(postId);
    }

    // 게시물 좋아요 취소
    @DeleteMapping("/{postId}/like")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void unlikePost(@PathVariable Long postId) {
        unLikeUseCase.execute(postId);
    }
}
