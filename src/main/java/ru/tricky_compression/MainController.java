package ru.tricky_compression;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping("api/upload")
	public DataFrame upload(@RequestParam(value = "data") String data) {
		return new DataFrame(data);
	}

	@GetMapping("api/download")
	public Element download(@RequestParam(value = "id") Integer id) {
		return dataBase.get(id);
	}
}
