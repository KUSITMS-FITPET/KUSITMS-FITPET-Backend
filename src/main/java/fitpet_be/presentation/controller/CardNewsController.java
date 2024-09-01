package fitpet_be.presentation.controller;

import fitpet_be.application.dto.response.CardnewsListResponse;
import fitpet_be.application.service.CardnewsService;
import fitpet_be.common.ApiResponse;
import fitpet_be.common.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cardnews")
public class CardNewsController {

    private final CardnewsService cardnewsService;

    @GetMapping("/desc")
    public ApiResponse<PageResponse<CardnewsListResponse>> getCardNewsDesc(
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "9") int size) {

        Pageable pageable = PageRequest.of(page -1, size);

        return ApiResponse.onSuccess(cardnewsService.getCardnewsListDesc(pageable));

    }

    @GetMapping("/asc")
    public ApiResponse<PageResponse<CardnewsListResponse>> getCardNewsAsc(
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "9") int size) {

        Pageable pageable = PageRequest.of(page -1, size);

        return ApiResponse.onSuccess(cardnewsService.getCardnewsListAsc(pageable));

    }

}
