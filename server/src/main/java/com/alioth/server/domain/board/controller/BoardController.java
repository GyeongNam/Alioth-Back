package com.alioth.server.domain.board.controller;

import com.alioth.server.common.aws.S3Service;
import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.board.dto.req.BoardCreateDto;
import com.alioth.server.domain.board.dto.req.BoardUpdateDto;
import com.alioth.server.domain.board.dto.req.SugBoardImageReqDto;
import com.alioth.server.domain.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/server/api/board")
public class BoardController {

    private final BoardService boardService;
    private final S3Service s3Service;

    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createBoard(
            @RequestBody @Valid BoardCreateDto boardCreateDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "추가되었습니다.",
                boardService.save(boardCreateDto,Long.parseLong(userDetails.getUsername()))
        );
    }

    @GetMapping("/list")
    public ResponseEntity<CommonResponse> listBoard(@AuthenticationPrincipal UserDetails userDetails){
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "게시글 리스트",
                boardService.list()
        );
    }

    @GetMapping("/suggestions-list")
    public ResponseEntity<CommonResponse> suggestionsListBoard(@AuthenticationPrincipal UserDetails userDetails){
        Long sm_code = Long.parseLong(userDetails.getUsername());
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "건의사항 리스트",
                boardService.suggestionsList(sm_code)
        );
    }

    @PatchMapping("/update/{boardId}")
    public ResponseEntity<CommonResponse> updateBoard(
            @RequestBody @Valid BoardUpdateDto boardUpdateDto,
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "글이 수정되었습니다.",
                boardService.update(boardUpdateDto,boardId,Long.parseLong(userDetails.getUsername()))
        );
    }

    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<CommonResponse> deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "글이 삭제되었습니다.",
                boardService.delete(boardId,Long.parseLong(userDetails.getUsername()))
        );
    }

    @GetMapping("/detail/{boardId}")
    public ResponseEntity<CommonResponse> detailBoard(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long boardId){
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "게시글 상세정보",
                boardService.detail(Long.parseLong(userDetails.getUsername()), boardId)
        );
    }

    @PostMapping("/image/upload")
    public ResponseEntity<CommonResponse> uploadImage(@ModelAttribute("file") SugBoardImageReqDto file) throws IOException {
        String imageUrl = s3Service.saveFile(file.boardImage(), "/suggestionboard");

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "이미지 S3 url 반환",
                imageUrl
        );
    }


}
