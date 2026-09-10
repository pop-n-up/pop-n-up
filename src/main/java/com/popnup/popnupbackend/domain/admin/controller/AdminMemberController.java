package com.popnup.popnupbackend.domain.admin.controller;

import com.beust.ah.A;
import com.popnup.popnupbackend.domain.admin.dto.request.AdminMemberResponse;
import com.popnup.popnupbackend.domain.admin.dto.request.AdminMemberStatusUpdateRequest;
import com.popnup.popnupbackend.domain.admin.sevice.AdminService;
import com.popnup.popnupbackend.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminMemberController adminMemberController;
    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminMemberResponse>>> getMembers() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getMembers()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<AdminMemberResponse>> getMember(Long memberId) {
        return ResponseEntity.ok(ApiResponse.success(adminService.getMember(memberId)));
    }

    @PatchMapping("/{memberId}/status")
    public ResponseEntity<ApiResponse<Void>> updateMemberStatus(
            @PathVariable Long memberId,
            @Valid @RequestBody AdminMemberStatusUpdateRequest request
            ) {
        adminService.updateMemberStatus(memberId, request);

        return ResponseEntity.ok(ApiResponse.success());
    }
}
