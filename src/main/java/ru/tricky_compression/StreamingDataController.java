package ru.tricky_compression;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@RestController
@RequestMapping("/api")
public class StreamingDataController {
    @GetMapping(value = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping(value = "/consume", produces = MediaType.APPLICATION_JSON_VALUE)
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
