package ru.tricky_compression;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class MainController {
	DataBase dataBase = new DataBase();

	public MainController() {
		dataBase.add(new Element("first"));
		dataBase.add(new Element("second"));
		dataBase.add(new Element("third"));
	}

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@PostMapping("api/upload")
	public ResponseEntity<String> upload(@RequestBody DataFrame data) {
		dataBase.add(new Element(data.getContent()));
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("api/upload/single_file")
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

	@GetMapping("api/download/single_file")
	public ResponseEntity<String> downloadSingleFile(@RequestParam(value = "filename") String filename) {
		try {
			String data = getFileContent(filename);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(data);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@GetMapping("api/download")
	public Element download(@RequestParam(value = "id") int id) {
		return dataBase.get(id);
	}

	@GetMapping (value = "api/data", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StreamingResponseBody> data(final HttpServletResponse response) {
		response.setContentType("application/json");
		StreamingResponseBody stream = out -> {
			try {
				for (int i = 0; i < 10; i++) {
					String content = "{\"counter\": " + i + "}\n";
					out.write(content.getBytes());
					out.flush();
				}
				out.close();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		};
		return new ResponseEntity(stream, HttpStatus.OK);
	}

	@GetMapping(value = "api/consume", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> consume() throws IOException {
		URL url = new URL("http://localhost:8080/api/data");
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);

		BufferedReader in = new BufferedReader(
				new InputStreamReader(connection.getInputStream())
		);

		String decodedString;
		while ((decodedString = in.readLine()) != null) {
			System.out.println(decodedString);
		}

		in.close();

		return new ResponseEntity("ok", HttpStatus.OK);
	}
}
