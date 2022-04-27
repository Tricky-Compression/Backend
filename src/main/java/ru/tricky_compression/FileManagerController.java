package ru.tricky_compression;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
public class FileManagerController {
	@PostMapping("/upload/single_file")
	public ResponseEntity<String> uploadSingleFile(@RequestBody File file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file.getFilename()));
		writer.write(file.getData());

		writer.close();
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	public String getFileContent(String filename) throws IOException {
		Path path = Paths.get(filename);
		return Files.readAllLines(path).get(0);
	}

	@GetMapping("/download/single_file")
	public ResponseEntity<String> downloadSingleFile(@RequestParam(value = "filename") String filename) {
		try {
			String data = getFileContent(filename);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(data);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

}
