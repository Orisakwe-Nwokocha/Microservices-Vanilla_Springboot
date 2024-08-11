package dev.orisha.book_service.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@Sql(scripts = "/db/data.sql")
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addBookTest() throws Exception {
        mockMvc.perform(post("/books")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\": \"Test Book\", \"author\": \"Test Author\", \"isbn\": \"12345678901\", \"genre\": \"FICTION\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.successful").value(true))
                .andExpect(jsonPath("$.data.title").value("Test Book"))
                .andExpect(jsonPath("$.data.author").value("Test Author"))
                .andExpect(jsonPath("$.data.isbn").value("12345678901"))
                .andExpect(jsonPath("$.data.genre").value("FICTION"))
                .andDo(print());
    }

    @Test
    public void updateBookTest() throws Exception {
        mockMvc.perform(patch("/books")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\": 100, \"title\": \"Updated Book\", \"author\": \"Updated Author\", \"isbn\": \"0987654321\", \"genre\": \"COMEDY\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.successful").value(true))
                .andExpect(jsonPath("$.data.title").value("Updated Book"))
                .andDo(print());
    }

    @Test
    public void getAllBooksTest() throws Exception {
        mockMvc.perform(get("/books")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.successful").value(true))
                .andExpect(jsonPath("$.data[0].title").value("Test Book"))
                .andExpect(jsonPath("$.data[0].author").value("Test Author"))
                .andExpect(jsonPath("$.data[0].isbn").value("1234567890"))
                .andExpect(jsonPath("$.data[0].genre").value("FICTION"));
    }

    @Test
    public void getBookTest() throws Exception {
        mockMvc.perform(get("/books/100")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.successful").value(true))
                .andExpect(jsonPath("$.data.title").value("Test Book"))
                .andExpect(jsonPath("$.data.author").value("Test Author"))
                .andExpect(jsonPath("$.data.isbn").value("1234567890"))
                .andExpect(jsonPath("$.data.genre").value("FICTION"));
    }

    @Test
    public void deleteBookTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/100")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
