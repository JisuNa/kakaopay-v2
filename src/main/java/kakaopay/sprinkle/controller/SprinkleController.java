package kakaopay.sprinkle.controller;

import kakaopay.sprinkle.dto.ApiResponse;
import kakaopay.sprinkle.dto.SprinkleRequest;
import kakaopay.sprinkle.service.SprinkleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/sprinkle")
@RequiredArgsConstructor
public class SprinkleController {
    private final SprinkleService sprinkleService;

    @PostMapping
    public ResponseEntity<ApiResponse> sprinkle(@RequestHeader(value = "X-USER-ID") Long userId,
                                                @RequestHeader(value = "X-ROOM-ID") Long roomId,
                                                @RequestBody SprinkleRequest sprinkleRequest) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(sprinkleService.newSprinkle(userId, roomId, sprinkleRequest))
        );
    }

    @PatchMapping
    public ResponseEntity<ApiResponse> receive(@RequestHeader(value = "X-USER-ID") Long userId,
                                               @RequestHeader(value = "X-ROOM-ID") Long roomId,
                                               @RequestHeader(value = "X-TOKEN") String token) {

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(sprinkleService.receive(userId, roomId, token))
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> inquiry(@RequestHeader(value = "X-USER-ID") Long userId,
                                               @RequestHeader(value = "X-ROOM-ID") Long roomId,
                                               @RequestHeader(value = "X-TOKEN") String token) {

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(sprinkleService.inquiry(userId, roomId, token))
        );
    }

}
