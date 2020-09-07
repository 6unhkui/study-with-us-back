package switus.user.back.studywithus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import switus.user.back.studywithus.common.security.JwtTokenProvider;
import switus.user.back.studywithus.domain.user.UserRole;
import switus.user.back.studywithus.dto.RoomDto;
import switus.user.back.studywithus.dto.common.PageRequest;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class RoomApiControllerTest {

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
    @Rollback(value = false)
    public void 스터디방_만들기() throws Exception {
        //given
        String name = "테스트 스터디룸2";
        String desc = "공부하는 공간입니다.2";

        RoomDto.SaveRequest request = new RoomDto.SaveRequest(name, desc, 100, false);

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/room")
                                        .header("Authorization", token)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(om.writeValueAsString(request)));
        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists());
    }

    
    @Test
    public void 나의_스터디방_리스트() throws Exception {
        //given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("size", "6");
        params.add("page", "1");
        params.add("direction", String.valueOf(Sort.Direction.ASC));
        params.add("keyword", "테스트");

        //when
        ResultActions result = mockMvc.perform(get("/api/v1/user/rooms")
                                        .header("Authorization", token)
                                        .params(params));

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists());
    }

    
    @Test
    public void 스터디방_상세() throws Exception {
        //given

        //when
        ResultActions result = mockMvc.perform(get("/api/v1/room/1")
                                      .header("Authorization", token));
        
        //then
        result.andDo(print())
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.data").exists());
    }


}