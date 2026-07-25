package dgsw.hs.kr.awscrud.domain;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok().body("Success Health Check");

    }
}
