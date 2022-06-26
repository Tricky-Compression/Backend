package ru.tricky_compression.filemanager;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tricky_compression.database.DataBase;

import java.nio.file.Path;
import java.sql.SQLException;

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
        try {
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(DataBase.getFilenames()));
        } catch (SQLException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
