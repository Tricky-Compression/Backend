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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class FileManagerController {
    private static final String prefix = "/file_storage/";

    private Path getPath(String filename) {
        return Path.of(prefix, filename);
    }

    @PostMapping("/get_list_files")
    public ResponseEntity<String> getListFiles() {
        Set<String> files = Stream.of(Objects.requireNonNull(new java.io.File(prefix).listFiles()))
                .filter(file -> !file.isDirectory())
                .map(java.io.File::getName)
                .collect(Collectors.toSet());
        StringBuilder json = new StringBuilder("{" +
                "\"files = [\"");
        for (String filename : files) {
            json.append(filename).append(",");
        }
        json.append("]}");
        return ResponseEntity.status(HttpStatus.OK).body(json.toString());
    }

    @PostMapping("/upload/single_file")
    public ResponseEntity<String> uploadSingleFile(@RequestBody File file) {
        file.setServerStart();
        try {
            Path path = getPath(file.getFilename());
            Files.createDirectories(path.getParent());
            Files.write(
                    path,
                    file.getData(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        file.setServerEnd();
        String json = String.format("{" +
                "\"clientStart\"=%d, " +
                "\"clientEnd\"=%d, " +
                "\"serverStart\"=%d, " +
                "\"serverEnd\"=%d" +
                "}",
                file.getClientStart(),
                file.getClientEnd(),
                file.getServerStart(),
                file.getServerEnd());
        return ResponseEntity.status(HttpStatus.CREATED).body(json);
    }

    @GetMapping("/download/single_file")
    public ResponseEntity<String> downloadSingleFile(@RequestParam(value = "filename") String filename) {
        File file = new File();
        file.setFilename(filename);
        file.setServerStart();
        try {
            Path path = getPath(filename);
            file.setData(Files.readAllBytes(Paths.get(path.toString())));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        file.setServerEnd();
        String json = String.format("{" +
                "\"clientStart\"=%d, " +
                "\"clientEnd\"=%d, " +
                "\"serverStart\"=%d, " +
                "\"serverEnd\"=%d," +
                "\"filename\"=%s," +
                "\"data\"=%s" +
                "}",
                file.getClientStart(),
                file.getClientEnd(),
                file.getServerStart(),
                file.getServerEnd(),
                file.getFilename(),
                Arrays.toString(file.getData()));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(json);
    }

}
