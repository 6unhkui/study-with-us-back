package switus.user.back.studywithus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import sun.security.util.SecurityConstants;
import switus.user.back.studywithus.common.properties.AppAccountProperties;
import switus.user.back.studywithus.common.security.JwtTokenProvider;
import switus.user.back.studywithus.domain.account.AccountRole;
import switus.user.back.studywithus.dto.AccountDto;

import java.io.File;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static switus.user.back.studywithus.common.security.constant.SecurityConstants.TOKEN_PREFIX;


@Transactional
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountApiControllerTest {
    
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper om;

    @Autowired
    AppAccountProperties account;

    @Autowired
    JwtTokenProvider jwtTokenProvider;


    public String getAccessToken() {
        return TOKEN_PREFIX + jwtTokenProvider.generate(account.getUserEmail(), AccountRole.USER);
    }


    @Test
    public void 계정_정보_조회() throws Exception {
        //given

        //when
        ResultActions result = mockMvc.perform(get("/api/v1/account")
                                      .header(HttpHeaders.AUTHORIZATION, getAccessToken()));

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value(account.getUserEmail()))
                .andExpect(jsonPath("$.data.name").exists());
    }


    @Test
    public void 프로필_이미지_업로드() throws Exception {
        //given
        Resource resource = new UrlResource(new File("src/test/java/resources/test.png").toURI());

        assertNotNull(resource);

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", resource.getFilename(), MediaType.MULTIPART_FORM_DATA_VALUE, resource.getInputStream());

        //when
        ResultActions result = mockMvc.perform(multipart("/api/v1/account/profile")
                                      .file(multipartFile).header(HttpHeaders.AUTHORIZATION, getAccessToken()));

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists());
    }


    @Test
    public void 계정_정보_수정() throws Exception {
        //given
        AccountDto.UpdateRequest request = new AccountDto.UpdateRequest();
        request.setName("tester");

        //when
        ResultActions result = mockMvc.perform(put("/api/v1/account")
                                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(om.writeValueAsString(request)));

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.success").value(true));
    }


    @Test
    public void 유저_비밀번호_변경() throws Exception {
        //given
        AccountDto.PasswordChangeRequest request = new AccountDto.PasswordChangeRequest();
        request.setOldPassword(account.getUserPassword());
        request.setNewPassword("123456789");

        //when
        ResultActions result = mockMvc.perform(put("/api/v1/account/password")
                                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(om.writeValueAsString(request)));

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.success").value(true));
    }


    @Test
    public void 계정_탈퇴() throws Exception {
        //given

        //when
        ResultActions result = mockMvc.perform(delete("/api/v1/user")
                                      .header(HttpHeaders.AUTHORIZATION, getAccessToken()));

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.success").value(true));
    }

}