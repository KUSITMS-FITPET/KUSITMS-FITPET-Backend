package fitpet_be.presentation.controller;

import fitpet_be.application.dto.request.CardnewsListRequest;
import fitpet_be.application.dto.response.CardnewsListResponse;
import fitpet_be.application.service.CardnewsService;
import fitpet_be.common.ApiResponse;
import fitpet_be.common.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cardnews")
public class CardNewsController {

    private final CardnewsService cardnewsService;

    @GetMapping("/desc")
    public ApiResponse<PageResponse<CardnewsListResponse>> getCardNewsDesc(
            @RequestBody CardnewsListRequest request) {

        return ApiResponse.onSuccess(cardnewsService.getCardnewsListDesc(request));

    }

    @GetMapping("/asc")
    public ApiResponse<PageResponse<CardnewsListResponse>> getCardNewsAsc(
            @RequestBody CardnewsListRequest request) {

        return ApiResponse.onSuccess(cardnewsService.getCardnewsListAsc(request));

    }

}
