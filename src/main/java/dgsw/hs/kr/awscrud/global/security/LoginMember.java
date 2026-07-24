package dgsw.hs.kr.awscrud.global.security;

import java.io.Serializable;

public record LoginMember(
        Long id,
        String username
) implements Serializable {
}
