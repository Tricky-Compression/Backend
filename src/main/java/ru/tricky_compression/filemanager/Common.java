package ru.tricky_compression.filemanager;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.String.valueOf;

@RestController
@RequestMapping("/api")
public class Common {
    private static final Gson gson = new Gson();
    private static final String prefix = "/file_storage/";
    private static final Path prefixPath = Path.of(prefix);

    public static Path getPath(String filename) {
        return Path.of(prefix, filename);
    }

    public static Path getPathWithNumber(String filename, int number) {
        return Path.of(getPath(filename).toString(), valueOf(number));
    }

    @GetMapping("/get_list_files")
    public ResponseEntity<String> getListFiles() {
        try (var stream = Files.walk(prefixPath)) {
            String[] files = stream
                    .filter(Files::isRegularFile)
                    .map(path -> path.toFile().getName())
                    .toArray(String[]::new);
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(files));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
