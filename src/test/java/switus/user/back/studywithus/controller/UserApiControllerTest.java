package switus.user.back.studywithus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.transaction.annotation.Transactional;
import switus.user.back.studywithus.common.security.JwtTokenProvider;
import switus.user.back.studywithus.domain.user.UserRole;
import switus.user.back.studywithus.dto.UserDto;

import java.io.File;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserApiControllerTest {

    private String email = "tester@gmail.com";
    private String token;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Before
    public void createToken() {
        token = "Bearer " + jwtTokenProvider.generate(this.email, UserRole.USER);
    }


    @Test
    public void 유저_정보_조회() throws Exception {
        //given

        //when
        ResultActions result = mockMvc.perform(get("/api/v1/user")
                                               .header("Authorization", token));

        //then
        result.andDo(print())
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
              .andExpect(jsonPath("$.success").value(true))
              .andExpect(jsonPath("$.data.email").value(this.email))
              .andExpect(jsonPath("$.data.name").value("tester"));
    }


    @Test
    public void 프로필_이미지_업로드() throws Exception {
        //given
        Resource resource = new UrlResource(new File("src/test/java/resources/test.png").toURI());
        assertNotNull(resource);

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", resource.getFilename(), MediaType.MULTIPART_FORM_DATA_VALUE, resource.getInputStream());

        //when
        ResultActions result = mockMvc.perform(multipart("/api/v1/user/profile")
                                      .file(multipartFile).header("Authorization", token));

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists());
    }


    @Test
    public void 유저_정보_수정() throws Exception {
        //given
        UserDto.UpdateRequest request = new UserDto.UpdateRequest();
        request.setName("tester2");

        //when
        ResultActions result = mockMvc.perform(put("/api/v1/user")
                                        .header("Authorization", token)
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
        UserDto.PasswordChangeRequest request = new UserDto.PasswordChangeRequest();
        request.setOldPassword("1234");
        request.setNewPassword("9999");

        //when
        ResultActions result = mockMvc.perform(put("/api/v1/user/password")
                                        .header("Authorization", token)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(om.writeValueAsString(request)));

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.success").value(true));
    }


    @Test
    public void 유저_탈퇴() throws Exception {
        //given

        //when
        ResultActions result = mockMvc.perform(delete("/api/v1/user")
                                      .header("Authorization", token));

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.success").value(true));
    }

}