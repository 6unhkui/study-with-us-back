package switus.user.back.studywithus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import switus.user.back.studywithus.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AuthApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Test
    public void 회원가입() throws Exception {
        //given
        String name = "tester";
        String email = "tester@gmail.com";
        String password = "1234";

        UserDto.SaveRequest request = UserDto.SaveRequest.builder().name(name).email(email).password(password).build();

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/auth/register")
                                       .contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(request)));

        //then
        result.andDo(print())
              .andExpect(status().isOk());
    }


    @Test
    public void 로그인() throws Exception {
        //given
        String email = "tester@gmail.com";
        String password = "1234";

        UserDto.LoginRequest request = new UserDto.LoginRequest(email, password);

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/auth/login")
                                       .contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(request))
                                       .accept(MediaType.APPLICATION_JSON));

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.name").value("tester"))
                .andExpect(jsonPath("$.email").value(email));
    }


    @Test
    public void 이메일_중복체크_중복() throws Exception {
        //given
        String email = "tester@gmail.com";

        //when
        ResultActions result = mockMvc.perform(get("/api/v1/auth/check-email/").param("email", email));

        //then
        result.andDo(print())
              .andExpect(status().isOk())
              .andExpect(content().string(String.valueOf(false)));
    }


    @Test
    public void 이메일_중복체크_중복아님() throws Exception {
        //given
        String email = "tester2@gmail.com";

        //when
        ResultActions result = mockMvc.perform(get("/api/v1/auth/check-email/").param("email", email));

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(true)));
    }
}