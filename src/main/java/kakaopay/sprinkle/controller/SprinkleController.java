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

    @PostMapping("")
    public ResponseEntity<?> sprinkle(@RequestHeader(value = "X-USER-ID") Long userId,
                                      @RequestHeader(value = "X-ROOM-ID") Long roomId,
                                      @RequestBody SprinkleRequest sprinkleRequest) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(sprinkleService.newSprinkle(userId, roomId, sprinkleRequest))
        );
    }

    @PatchMapping("")
    public ResponseEntity<?> receive(@RequestHeader(value = "X-USER-ID") Long userId,
                                     @RequestHeader(value = "X-ROOM-ID") Long roomId,
                                     @RequestHeader(value = "TOKEN") String token) {

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(sprinkleService.receive(userId, roomId, token))
        );
    }

    @GetMapping("")
    public ResponseEntity<?> check(@RequestHeader(value = "TOKEN") String token) {


        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(sprinkleService.check(token))
        );
    }

}
