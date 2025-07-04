package org.example.stortiessearch.global.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.stortiessearch.global.exception.StortiesException;
import org.springframework.boot.web.error.Error;

@Getter
@AllArgsConstructor
public enum ErrorCodes {

    PASSWORD_MISMATCH(ErrorStatus.UNAUTHORIZED, "비밀번호가 일치되지 않습니다.", 1),
    TOKEN_EXPIRED(ErrorStatus.UNAUTHORIZED, "토큰이 만료되었습니다.", 2),

    POST_DELETE_FORBIDDEN(ErrorStatus.FORBIDDEN, "삭제 권한이 없습니다.", 1),

    USER_NOT_FOUND(ErrorStatus.NOT_FOUND, "유저를 찾지 못했습니다.", 1),
    POST_NOT_FOUND(ErrorStatus.NOT_FOUND, "게시물을 찾지 못했습니다.", 2),

    EMAIL_ALREADY_EXIST(ErrorStatus.CONFLICT, "이메일이 중복됩니다.", 1),
    ALREADY_LIKED(ErrorStatus.CONFLICT, "이미 좋아요를 눌렀습니다.", 2),

    INTERNAL_SERVER_ERROR(ErrorStatus.INTERNAL_SERVER_ERROR, "서버 에러", 1),

    CLIENT_CONNECT_FAILED(ErrorStatus.SERVICE_UNAVAILABLE, "서비스 통신 오류", 1);

    private final int status;

    private final String message;

    private final int sequence;

    public StortiesException throwException() {
        return new StortiesException(this);
    }
}
