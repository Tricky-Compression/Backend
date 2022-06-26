package ru.tricky_compression.filemanager;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tricky_compression.entity.ChunkData;
import ru.tricky_compression.entity.FileData;

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
            fileData.getTimestamps().setClientStart(clientStart);
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
    public ResponseEntity<String> downloadChunk(@RequestParam int number, @RequestParam String filename, @RequestParam long clientStart) {
        try {
            ChunkData chunkData = new ChunkData(number, filename);
            chunkData.getTimestamps().setClientStart(clientStart);
            chunkData.getTimestamps().setServerStart();
            Path path = Common.getPathWithNumber(chunkData.getFilename(), chunkData.getChunkNumber());
            chunkData.setData(Files.readAllBytes(path));
            chunkData.getTimestamps().setServerEnd();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(gson.toJson(chunkData));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
