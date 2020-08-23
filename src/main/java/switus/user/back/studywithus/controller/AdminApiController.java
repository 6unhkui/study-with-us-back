package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import switus.user.back.studywithus.common.security.CustomUserDetails;

@Api(tags = {"2. Admin"})
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AdminApiController {

    @GetMapping("admin")
    public ResponseEntity admin(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(String.valueOf(userDetails));
    }
}
