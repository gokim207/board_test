package dgsw.hs.kr.awscrud.domain.board.repository;

import dgsw.hs.kr.awscrud.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
