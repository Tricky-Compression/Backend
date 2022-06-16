package ru.tricky_compression.database;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/database")
public class DataBaseController {
    DataBase dataBase = new DataBase();

    @GetMapping("/have_chunk")
    public ResponseEntity<String> haveChunk(@RequestParam String hash) {
        if (dataBase.contains(hash)) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
