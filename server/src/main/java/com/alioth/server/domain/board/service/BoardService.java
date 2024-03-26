package com.alioth.server.domain.board.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.domain.board.domain.Board;
import com.alioth.server.domain.board.domain.BoardType;
import com.alioth.server.domain.board.dto.req.BoardCreateDto;
import com.alioth.server.domain.board.dto.req.BoardUpdateDto;
import com.alioth.server.domain.board.dto.res.BoardResDto;
import com.alioth.server.domain.board.repository.BoardRepository;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.repository.SalesMemberRepository;
import com.alioth.server.domain.schedule.domain.Schedule;
import com.alioth.server.domain.schedule.dto.res.ScheduleResDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final TypeChange typeChange;
    private final SalesMemberRepository salesMemberRepository;

    public BoardService(
            BoardRepository boardRepository,
            TypeChange typeChange,
            SalesMemberRepository salesMemberRepository
    ) {
        this.boardRepository = boardRepository;
        this.typeChange = typeChange;
        this.salesMemberRepository = salesMemberRepository;
    }

    public Board findById(Long BoardId){
        return boardRepository.findById(BoardId).orElseThrow(()->new EntityNotFoundException("존재하지 않는 글입니다."));
    }

    public BoardResDto save(BoardCreateDto boardCreateDto) {
        SalesMembers salesMembers = salesMemberRepository.findById(1L).orElseThrow(()->new EntityNotFoundException("존재하지 않는 사원입니다."));
        return typeChange.BoardToBoardResDto(
                boardRepository.save(
                        typeChange.BoardCreateDtoToBoard(boardCreateDto, salesMembers)
                )
        );
    }

    public BoardResDto update(BoardUpdateDto boardUpdateDto) {
        Board board = this.findById(boardUpdateDto.boardId());
        board.update(boardUpdateDto);
        return typeChange.BoardToBoardResDto(board);
    }

    public BoardResDto delete(Long boardId) {
        Board board  =  this.findById(boardId);
        board.delete();
        return typeChange.BoardToBoardResDto(board);
    }

    public List<BoardResDto> list() {
        SalesMembers salesMembers = salesMemberRepository.findById(1L).orElseThrow(()->new EntityNotFoundException("존재하지 않는 사원입니다."));
        return boardRepository.findByBoardList(BoardType.SUGGESTION, "Y")
                .stream()
                .map(typeChange::BoardToBoardResDto)
                .collect(Collectors.toList());
    }

//    public List<BoardResDto> suggestionsList() {
//        SalesMembers salesMembers = salesMemberRepository.findById(1L).orElseThrow(()->new EntityNotFoundException("존재하지 않는 사원입니다."));
//        return boardRepository.
//    }
}
