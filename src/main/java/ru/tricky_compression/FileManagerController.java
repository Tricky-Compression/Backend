package ru.tricky_compression;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tricky_compression.entity.FileData;
import ru.tricky_compression.entity.Timestamps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FileManagerController {
    private static final Gson gson = new Gson();
    private static final String prefix = "/file_storage/";
    private static final Path prefixPath = Path.of(prefix);

    private Path getPath(String filename) {
        return Path.of(prefix, filename);
    }

    @GetMapping("/get_list_files")
    public ResponseEntity<String> getListFiles() {
        try (var stream = Files.walk(prefixPath)) {
            String files = stream
                    .filter(Files::isRegularFile)
                    .map(path -> path.toFile().getName())
                    .collect(Collectors.joining(",", "[", "]"));
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(files));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/upload/single_file")
    public ResponseEntity<String> uploadSingleFile(@RequestBody FileData fileData) {
        try {
            fileData.getTimestamps().setServerStart();
            Path path = getPath(fileData.getFilename());
            Files.createDirectories(path.getParent());
            Files.write(
                    path,
                    fileData.getData(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            fileData.getTimestamps().setServerEnd();
            return ResponseEntity.status(HttpStatus.CREATED).body(gson.toJson(fileData.getTimestamps()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/download/single_file")
    public ResponseEntity<String> downloadSingleFile(@RequestParam String filename, @RequestParam long clientStart) {
        try {
            FileData fileData = new FileData(filename);
            try {
                var field = Timestamps.class.getDeclaredField("clientStart");
                field.setAccessible(true);
                field.setLong(fileData.getTimestamps(), clientStart);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
            fileData.getTimestamps().setServerStart();
            Path path = getPath(fileData.getFilename());
            fileData.setData(Files.readAllBytes(path));
            fileData.getTimestamps().setServerEnd();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(gson.toJson(fileData));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
