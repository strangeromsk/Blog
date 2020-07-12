package main;

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
//@WithUserDetails("arkasha@example.com")
public class AccountTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void accountLoginTest() throws Exception{
        String correctUser = "{\"e_mail\": \"ivan@example.com\", \"password\" : \"password\"}";
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .content(correctUser).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.result").value("true"));
    }

//    @Test
//    public void accountRegisterTest() throws Exception{
//        String correctNewUser = "{\"email\": \"newuser@user.my\", \"password\" : \"password123\"," +
//                " \"name\" : \"Vasia\", \"captcha\" : \"123456\", \"captcha_secret\" : \"69sdFd67df7Pd9d3\"}";
//
//        mvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
//                .content(correctNewUser).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"))
//                .andExpect(jsonPath("$.result").value("true"));
//    }
}
