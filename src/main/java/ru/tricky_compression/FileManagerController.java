package ru.tricky_compression;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tricky_compression.entity.FileData;
import ru.tricky_compression.entity.FileTimestamps;
import ru.tricky_compression.entity.Timestamps;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FileManagerController {
    private static final Gson gson = new Gson();
    private static final String prefix = "file_storage/";
    private static final File prefixFile = new File(prefix);

    private Path getPath(String filename) {
        return Path.of(prefix, filename);
    }

    @GetMapping("/get_list_files")
    public ResponseEntity<String> getListFiles() {
        String files = Arrays.stream(Objects.requireNonNull(prefixFile.listFiles()))
                .filter(file -> !file.isDirectory())
                .map(java.io.File::getName)
                .collect(Collectors.joining(",", "[", "]"));
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(files));
    }

    @PostMapping("/upload/single_file")
    public ResponseEntity<String> uploadSingleFile(@RequestBody FileData file) {
        try {
            file.getTimestamps().setServerStart();
            Path path = getPath(file.getFilename());
            Files.createDirectories(path.getParent());
            Files.write(
                    path,
                    file.getData(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            file.getTimestamps().setServerEnd();
            return ResponseEntity.status(HttpStatus.CREATED).body(gson.toJson(file.getTimestamps()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/download/single_file")
    public ResponseEntity<String> downloadSingleFile(@RequestBody FileTimestamps fileTimestamps) {
        try {
            String filename = fileTimestamps.getFilename();
            FileData file = new FileData(filename);
            file.getTimestamps().setServerStart();
            file.setData(Files.readAllBytes(getPath(filename)));
            file.getTimestamps().setServerEnd();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(gson.toJson(file));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
