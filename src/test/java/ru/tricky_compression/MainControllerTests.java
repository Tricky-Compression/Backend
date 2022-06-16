/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.tricky_compression;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired private ObjectMapper objectMapper;

	private JacksonTester<Element> jsonTester;

	@BeforeEach
	public void setup() {
		JacksonTester.initFields(this, objectMapper);
	}

	@Test
	public void noParamGreetingShouldReturnDefaultMessage() throws Exception {

		this.mockMvc.perform(get("/greeting")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.content").value("Hello, World!"));
	}

	@Test
	public void paramGreetingShouldReturnTailoredMessage() throws Exception {

		this.mockMvc.perform(get("/greeting").param("name", "Spring Community"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.content").value("Hello, Spring Community!"));
	}

	@Test
	public void dataBaseReturnsExistingId() throws Exception {

		this.mockMvc.perform(get("/api/download").param("id", "2"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.element").value("third"));
	}

	@Test
	public void dataBaseAcceptNewIdAndThenReturnsIt() throws Exception {
		final String nameElement = "test";
		final Element element = new Element(nameElement);
		final String testJSON = jsonTester.write(element).getJson();

		this.mockMvc.perform(post("/api/upload").content(testJSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		/*Thread.sleep(1000);

		this.mockMvc.perform(get("/api/download").param("id", "3"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.element").value(nameElement));*/
	}

	@Test
	public void dataBaseReturnsNothingWhenNotExistingId() throws Exception {

		this.mockMvc.perform(get("/api/download").param("id", "10000"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(""));
	}

}
