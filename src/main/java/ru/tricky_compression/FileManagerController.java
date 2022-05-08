package ru.tricky_compression;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

@RestController
@RequestMapping("/api")
public class FileManagerController {
    private static final String prefix = "/file_storage/";

    private Path getPath(String filename) {
        return Path.of(prefix, filename);
    }

    @PostMapping("/upload/single_file")
    public ResponseEntity<String> uploadSingleFile(@RequestBody File file) {
        try {
            Path path = getPath(file.getFilename());
            Files.createDirectories(path.getParent());
            Files.write(
                    path,
                    file.getData(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/download/single_file")
    public ResponseEntity<String> downloadSingleFile(@RequestParam(value = "filename") String filename) {
        try {
            Path path = getPath(filename);
            byte[] data = Files.readAllBytes(Paths.get(path.toString()));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(Arrays.toString(data));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
