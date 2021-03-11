//package switus.user.back.studywithus.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.transaction.annotation.Transactional;
//import switus.user.back.studywithus.common.properties.AppAccountProperties;
//import switus.user.back.studywithus.dto.AccountDto;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@Transactional
//@ActiveProfiles("test")
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class AuthApiControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    ObjectMapper om;
//
//    @Autowired
//    AppAccountProperties account;
//
//
//    @Test
//    public void 회원가입() throws Exception {
//        //given
//        String name = "tester";
//        String email = "tester@gmail.com";
//        String password = "1234";
//
//        AccountDto.SaveRequest request = AccountDto.SaveRequest.builder().name(name).email(email).password(password).build();
//
//        //when
//        ResultActions result = mockMvc.perform(post( "/api/v1/auth/register")
//                                            .contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(request)));
//
//        //then
//        result.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
//                .andExpect(jsonPath("$.success").value(true));
//    }
//
//
//    @Test
//    public void 로그인() throws Exception {
//        //given
//        AccountDto.LoginRequest request = new AccountDto.LoginRequest(account.getUserEmail(), account.getUserPassword());
//
//        //when
//        ResultActions result = mockMvc.perform(post("/api/v1/auth/login")
//                                      .contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(request))
//                                      .accept(MediaType.APPLICATION_JSON));
//
//        //then
//        result.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.accessToken").exists())
//                .andExpect(jsonPath("$.data.name").exists())
//                .andExpect(jsonPath("$.data.email").value(account.getUserEmail()));
//    }
//
//
//    @ParameterizedTest
//    @CsvSource({
//            "user@gmail.com, true", // 중복
//            "nonuser@gmail.com, false" // 중복 아님
//    })
//    public void 이메일_중복체크(String email, boolean data) throws Exception {
//        //given
//
//        //when
//        ResultActions result = mockMvc.perform(get("/api/v1/auth/check-email/").param("email", email));
//
//        //then
//        result.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").value(data));
//    }
//
//}