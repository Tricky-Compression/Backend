package ru.tricky_compression;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
public class FileManagerController {
    private static final String prefix = "/app";

    @PostMapping("/upload/single_file")
    public ResponseEntity<String> uploadSingleFile(@RequestBody File file) {
        try (FileWriter fileWriter = new FileWriter(prefix + file.getFilename());
             BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.write(file.getData());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/download/single_file")
    public ResponseEntity<String> downloadSingleFile(@RequestParam(value = "filename") String filename) {
        try {
            String data = String.join("\n", Files.readAllLines(Paths.get(filename)));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(data);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
