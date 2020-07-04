package main;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("arkasha@example.com")
public class AccountTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void accountRegisterTest() throws Exception{
        String correctNewUser = "{\"e_mail\": \"newuser@user.my\", \"password\" : \"password123\", \"name\" : \"Vasia\", \"captcha\" : \"123456\", \"captcha_secret\" : \"69sdFd67df7Pd9d3\"}";
//        HashMap<String, String> data = new HashMap<>();
//        data.put("message", "ok");
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .content(correctNewUser).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.result").value("true"));
    }
}
