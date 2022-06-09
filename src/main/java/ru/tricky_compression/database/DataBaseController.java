package ru.tricky_compression.database;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tricky_compression.entity.DataFrame;

@RestController
@RequestMapping("/api/database")
public class DataBaseController {
    DataBase dataBase = new DataBase();

    public DataBaseController() {
        dataBase.add(new Element("first"));
        dataBase.add(new Element("second"));
        dataBase.add(new Element("third"));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestBody DataFrame data) {
        dataBase.add(new Element(data.getContent()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/download")
    public ResponseEntity<Element> download(@RequestParam(value = "id") int id) {
        if (id < dataBase.size()) {
            return ResponseEntity.status(HttpStatus.OK).body(dataBase.get(id));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
