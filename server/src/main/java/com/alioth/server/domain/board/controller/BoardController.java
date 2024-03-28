package com.alioth.server.domain.board.controller;

import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.board.dto.req.BoardCreateDto;
import com.alioth.server.domain.board.dto.req.BoardUpdateDto;
import com.alioth.server.domain.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createBoard(@RequestBody @Valid BoardCreateDto boardCreateDto){
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "추가되었습니다.",
                boardService.save(boardCreateDto)
        );
    }

    @GetMapping("/list")
    public ResponseEntity<CommonResponse> listBoard(){
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "게시글 리스트",
                boardService.list()
        );
    }

//    @GetMapping("/suggestions-list")
//    public ResponseEntity<CommonResponse> suggestionsListBoard(){
//        return CommonResponse.responseMessage(
//                HttpStatus.OK,
//                "건의사항 리스트",
//                boardService.suggestionsList()
//        );
//    }

    @PatchMapping("/update")
    public ResponseEntity<CommonResponse> updateBoard(@RequestBody @Valid BoardUpdateDto boardUpdateDto){
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "글이 수정되었습니다.",
                boardService.update(boardUpdateDto)
        );
    }

    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<CommonResponse> deleteBoard(@PathVariable Long boardId){
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "글이 삭제되었습니다.",
                boardService.delete(boardId)
        );
    }
}
