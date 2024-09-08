package fitpet_be.presentation.controller;

import fitpet_be.application.dto.response.CardnewsListResponse;
import fitpet_be.application.service.CardnewsService;
import fitpet_be.common.ApiResponse;
import fitpet_be.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cardNews")
public class CardNewsController {

    private final CardnewsService cardNewsService;

    @Operation(summary = "카드뉴스 조회", description = "카드 뉴스를 최신순으로 조회합니다")
    @GetMapping("/desc")
    public ApiResponse<PageResponse<CardnewsListResponse>> getCardNewsDesc(
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "9") int size) {

        Pageable pageable = PageRequest.of(page -1, size);

        return ApiResponse.onSuccess(cardNewsService.getCardnewsListDesc(pageable));

    }

    @GetMapping("/asc")
    @Operation(summary = "카드뉴스 조회", description = "카드 뉴스를 오래된 순으로 조회합니다")
    public ApiResponse<PageResponse<CardnewsListResponse>> getCardNewsAsc(
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "9") int size) {

        Pageable pageable = PageRequest.of(page -1, size);

        return ApiResponse.onSuccess(cardNewsService.getCardnewsListAsc(pageable));

    }

}
