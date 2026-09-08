package com.popnup.popnupbackend.domain.popup.controller;

import com.popnup.popnupbackend.domain.auth.dto.request.AuthUser;
import com.popnup.popnupbackend.domain.popup.dto.reponse.PopupListResponse;
import com.popnup.popnupbackend.domain.popup.dto.reponse.PopupResponse;
import com.popnup.popnupbackend.domain.popup.dto.request.PopupCreateRequest;
import com.popnup.popnupbackend.domain.popup.dto.request.PopupSearchCondition;
import com.popnup.popnupbackend.domain.popup.dto.request.PopupUpdateRequest;
import com.popnup.popnupbackend.domain.popup.service.PopupService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/popups")
@RequiredArgsConstructor
public class PopupController {

  private final PopupService popupService;

  @PostMapping
  public ResponseEntity<PopupResponse> createPopup(
      @AuthenticationPrincipal AuthUser authUser, // 추가
      @RequestBody @Valid PopupCreateRequest request) {
    PopupResponse response = popupService.createPopup(authUser.getId(), request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  public ResponseEntity<List<PopupListResponse>> getPopupList(PopupSearchCondition condition) {
    List<PopupListResponse> popups = popupService.getPopupList(condition);
    return ResponseEntity.ok(popups);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PopupResponse> getPopupDetail(@PathVariable("id") Long id) {
    PopupResponse popup = popupService.getPopupDetail(id);
    return ResponseEntity.ok(popup);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PopupResponse> updatePopup(
      @AuthenticationPrincipal AuthUser authUser, // 추가
      @PathVariable("id") Long id,
      @RequestBody @Valid PopupUpdateRequest request) {
    PopupResponse updatedPopup = popupService.updatePopup(id, authUser.getId(), request);
    return ResponseEntity.ok(updatedPopup);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePopup(
      @AuthenticationPrincipal AuthUser authUser, // 추가
      @PathVariable("id") Long id) {
    popupService.deletePopup(id, authUser.getId());
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/map")
  public ResponseEntity<List<PopupResponse>> getPopupsOnMap(
      @RequestParam("minLat") Double minLat,
      @RequestParam("maxLat") Double maxLat,
      @RequestParam("minLng") Double minLng,
      @RequestParam("maxLng") Double maxLng) {

    List<PopupResponse> result =
        popupService.getPopupsInBoundingBox(minLat, maxLat, minLng, maxLng);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/nearby")
  public ResponseEntity<List<PopupResponse>> getNearbyPopups(
      @RequestParam("latitude") Double latitude,
      @RequestParam("longitude") Double longitude,
      @RequestParam(value = "radius", defaultValue = "3.0") Double radius) {

    List<PopupResponse> responses = popupService.getNearbyPopups(latitude, longitude, radius);
    return ResponseEntity.ok(responses);
  }
}
