package ru.tricky_compression.filemanager;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tricky_compression.entity.ChunkData;
import ru.tricky_compression.entity.FileData;
import ru.tricky_compression.entity.Timestamps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api")
public class DownloadModule {
    private static final Gson gson = new Gson();

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
            Path path = Common.getPath(fileData.getFilename());
            fileData.setData(Files.readAllBytes(path));
            fileData.getTimestamps().setServerEnd();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(gson.toJson(fileData));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/download/chunk")
    public ResponseEntity<String> downloadChunk(@RequestBody int number, @RequestParam String filename, @RequestParam long clientStart) {
        try {
            ChunkData chunk = new ChunkData(number, filename);
            try {
                var field = Timestamps.class.getDeclaredField("clientStart");
                field.setAccessible(true);
                field.setLong(chunk.getTimestamps(), clientStart);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
            chunk.getTimestamps().setServerStart();
            Path path = Common.getPathWithNumber(chunk.getFilename(), chunk.getChunkNumber());
            chunk.setData(Files.readAllBytes(path));
            chunk.getTimestamps().setServerEnd();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(gson.toJson(chunk));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
