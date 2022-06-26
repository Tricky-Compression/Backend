package ru.tricky_compression.filemanager;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tricky_compression.database.DataBase;
import ru.tricky_compression.entity.ChunkData;
import ru.tricky_compression.entity.FileData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@RestController
@RequestMapping("/api")
public class UploadModule {
    private static final Gson gson = new Gson();

    @PostMapping("/upload/single_file")
    public ResponseEntity<String> uploadSingleFile(@RequestBody FileData fileData) {
        try {
            fileData.getTimestamps().setServerStart();
            Path path = Common.getPath(fileData.getFilename());
            DataBase.addFilename(fileData.getFilename());
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

    @PostMapping("/upload/chunk")
    public ResponseEntity<String> uploadChunk(@RequestBody ChunkData chunk) {
        try {
            chunk.getTimestamps().setServerStart();
            Path path = Common.getPathWithNumber(chunk.getFilename(), chunk.getChunkNumber());
            DataBase.addFilename(chunk.getFilename());
            Files.createDirectories(path.getParent());
            Files.write(
                    path,
                    chunk.getData(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            chunk.getTimestamps().setServerEnd();
            return ResponseEntity.status(HttpStatus.CREATED).body(gson.toJson(chunk.getTimestamps()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
