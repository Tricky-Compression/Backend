package ru.tricky_compression;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	@RequestMapping(value ="api/upload", method = RequestMethod.POST)
	public ResponseEntity<String> upload(@RequestBody DataFrame data) {
		dataBase.add(new Element(data.getDataFrame()));
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("api/download")
	public Element download(@RequestParam(value = "id") int id) {
		return dataBase.get(id);
	}
}
