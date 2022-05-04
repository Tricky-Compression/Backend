package ru.tricky_compression;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@RestController
@RequestMapping("/api")
public class FileManagerController {
    private static final String prefix = "/app";

    private Path getPath(File file) {
        return Path.of(prefix, file.getFilename());
    }

    @PostMapping("/upload/single_file")
    public ResponseEntity<String> uploadSingleFile(@RequestBody File file) {
        try {
            Files.write(
                    getPath(file),
                    file.getData(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            System.out.println(e.getMessage());
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
