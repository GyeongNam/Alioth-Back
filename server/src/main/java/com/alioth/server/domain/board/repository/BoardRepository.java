package com.alioth.server.domain.board.repository;

import com.alioth.server.domain.board.domain.Board;
import com.alioth.server.domain.board.domain.BoardType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query(value = "SELECT b FROM Board b WHERE b.boardType != :boardType AND b.boardDel_YN != :delYN")
    List<Board> findByBoardList(@Param("boardType") BoardType boardType, @Param("delYN") String delYN);

    @Query(value = "SELECT b FROM Board b " +
            "LEFT JOIN b.salesMembers sm " +
            "LEFT JOIN sm.team t " +
            "WHERE t.id = :teamId " +
            "AND b.boardType = :boardType " +
            "AND b.boardDel_YN = :delYN")
    List<Board> findSuggestionsByTeam(@Param("teamId") Long teamId, @Param("boardType") BoardType boardType, @Param("delYN") String delYN);

    @Query(value = "SELECT b FROM Board b WHERE b.boardId = :boardId AND b.boardDel_YN = :delYN")
    Optional<Board> findByBoardIdAndBoardDel_YN(@Param("boardId") Long boardId, @Param("delYN") String delYN);

    @Query("SELECT b FROM Board b WHERE b.salesMembers.salesMemberCode = :smCode AND b.boardType = :boardType AND b.boardDel_YN = 'N'")
    List<Board> findMyBoards(@Param("smCode") Long smCode, @Param("boardType") BoardType boardType);

    @Query("SELECT b FROM Board b WHERE b.boardType = :boardType AND b.boardDel_YN = 'N'")
    List<Board> findByBoardType(@Param("boardType") BoardType boardType);


}
