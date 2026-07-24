package dgsw.hs.kr.awscrud.domain.auth.repository;

import dgsw.hs.kr.awscrud.domain.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByUsername(String username);

    Optional<Member> findByUsername(String username);
}
