package main.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostServiceImplTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void makeNewLikeTest() throws Exception{
        String correctUser = "{\"e_mail\": \"ivan@example.com\", \"password\" : \"password\"}";
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .content(correctUser).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.result").value("true"));

        String postId = "{\"post_id\": \"1\"}";
        mvc.perform(MockMvcRequestBuilders.post("/api/post/like")
                .content(postId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.result").value("true"));
    }
}
