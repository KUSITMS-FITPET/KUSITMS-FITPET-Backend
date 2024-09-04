package fitpet_be.presentation.controller;

import fitpet_be.application.service.ContactService;
import fitpet_be.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contacts")
public class ContactController {

    private final ContactService contactService;

    @Operation(summary = "전화 문의", description = "전화 문의 버튼 클릭시 Count 증가")
    @PostMapping("/phones")
    public ApiResponse<String> contactCalls(){

        return ApiResponse.onSuccess(contactService.addCount());
    }

    @Operation(summary = "오늘의 문의 횟수", description = "전화 문의와 카카오톡 문의 횟수 가져오기")
    @GetMapping("/mainpages")
    public ApiResponse<Integer> getContacts(){

        return ApiResponse.onSuccess(contactService.getCounts());
    }




}
