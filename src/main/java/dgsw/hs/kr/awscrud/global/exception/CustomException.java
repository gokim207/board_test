package dgsw.hs.kr.awscrud.global.exception;

import dgsw.hs.kr.awscrud.global.exception.error.CustomErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
  private final CustomErrorCode error;
}
