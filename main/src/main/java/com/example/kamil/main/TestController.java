package com.example.kamil.main;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping()
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("test works");
    }

//    @GetMapping("/2")
//    public ResponseEntity<String> test2(){
//        return ResponseEntity.ok("test 2 works");
//    }
}
