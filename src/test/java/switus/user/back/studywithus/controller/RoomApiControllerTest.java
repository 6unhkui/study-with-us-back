//package switus.user.back.studywithus.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.CsvSource;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.junit.runner.RunWith;
//import org.mockito.internal.util.StringUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.util.StringUtils;
//import switus.user.back.studywithus.common.annotaion.TestDescription;
//import switus.user.back.studywithus.common.properties.AppAccountProperties;
//import switus.user.back.studywithus.common.security.JwtTokenProvider;
//import switus.user.back.studywithus.domain.account.Account;
//import switus.user.back.studywithus.domain.account.AccountRole;
//import switus.user.back.studywithus.dto.RoomDto;
//import switus.user.back.studywithus.dto.common.CurrentAccount;
//import switus.user.back.studywithus.dto.common.PageRequest;
//
//
//import java.io.File;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import static org.junit.Assert.assertNotNull;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static switus.user.back.studywithus.common.constant.SecurityConstants.TOKEN_PREFIX;
//
//@Transactional
////@ActiveProfiles("test")
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class RoomApiControllerTest {
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
//    @Autowired
//    JwtTokenProvider jwtTokenProvider;
//
//
//    public String getAccessToken() {
//        return TOKEN_PREFIX + jwtTokenProvider.generate(
//                new CurrentAccount(Account.builder().id(1L).email(this.account.getUserEmail()).name("user").role(AccountRole.USER).build()));
//    }
//
//
//
//    @ParameterizedTest
//    @MethodSource("paramsForCreateRoom")
//    public void 스터디방_생성(String name, String description, int maxCount, Long categoryId) throws Exception {
//        //given
//        RoomDto.SaveRequest request = new RoomDto.SaveRequest();
//        request.setName(name);
//        request.setDescription(description);
//        if(maxCount == 0) {
//            request.setUnlimited(true);
//        }else request.setUnlimited(false);
//        request.setMaxCount(maxCount);
//        request.setCategoryId(categoryId);
//
//        MockMultipartFile metadata = new MockMultipartFile("form", om.writeValueAsString(request).getBytes(StandardCharsets.UTF_8));
//
//        Resource resource = new UrlResource(new File("src/test/java/resources/test.png").toURI());
//        assertNotNull(resource);
//
//        MockMultipartFile file = new MockMultipartFile(
//                "file", resource.getFilename(), MediaType.MULTIPART_FORM_DATA_VALUE, resource.getInputStream());
//
//
//        //when
//        ResultActions result = mockMvc.perform(multipart("/api/v1/room")
//                .file(file)
//                .file(metadata)
//                .header(HttpHeaders.AUTHORIZATION, getAccessToken()));
//
//
//        //then
//        result.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").exists());
//    }
//
//    private static Stream<Arguments> paramsForCreateRoom() {
//        return Stream.of(
//                Arguments.of("우리 같이 공부해요!", "설명 블라블라", 10, 1L),
//                Arguments.of("Spring Data JPA 공부방", "Spring Data JPA 공부하실 개발자분들 구합니다.", 100, 6L),
//                Arguments.of("React 스터디원 모집합니다", "React 공부하실 개발자분들 구합니다.", 0, 6L),
//                Arguments.of("개발 지식 공유방", "개발 지식 공유", 20, 6L),
//                Arguments.of("UX 디자인 스터디", "UX 공부방", 10, 7L)
//        );
//    }
//
//
//    @Test
//    @TestDescription("스터디방 리스트. 인증 없이도 접근 가능하다")
//    public void 스터디방_리스트() throws Exception {
//        //given
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("size", "6");
//        params.add("page", "1");
//        params.add("direction", String.valueOf(Sort.Direction.ASC));
//
//        //when
//        ResultActions result = mockMvc.perform(get("/api/v1/rooms")
//                                       .params(params));
//
//        //then
//        result.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").exists());
//    }
//
//
//    @Test
//    public void 스터디방_리스트_검색() throws Exception {
//        //given
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("size", "6");
//        params.add("page", "1");
//        params.add("direction", String.valueOf(Sort.Direction.ASC));
//        params.add("keyword", "React");
//        params.add("orderType", RoomDto.SearchRequest.OrderType.CREATED_DATE.name());
//        params.add("categoriesId", String.valueOf(6L));
//        params.add("categoriesId", String.valueOf(1L));
//
//
//        //when
//        ResultActions result = mockMvc.perform(get("/api/v1/rooms")
//                                       .params(params));
//
//        //then
//        result.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").exists());
//    }
//
//
//
//    @Test
//    public void 현재_유저의_스터디방_리스트() throws Exception {
//        //given
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("size", "6");
//        params.add("page", "1");
//        params.add("direction", String.valueOf(Sort.Direction.ASC));
//
//
//        //when
//        ResultActions result = mockMvc.perform(get("/api/v1/rooms")
//                                       .header(HttpHeaders.AUTHORIZATION, getAccessToken())
//                                       .params(params));
//
//        //then
//        result.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").exists());
//    }
//
//
//    @Test
//    public void 스터디방_상세_정보() throws Exception {
//        //given
//        Long roomId = 1L;
//
//        //when
//        ResultActions result = mockMvc.perform(get("/api/v1/room/{roomId}", roomId)
//                                       .header(HttpHeaders.AUTHORIZATION, getAccessToken()));
//
//        //then
//        result.andDo(print())
//              .andExpect(status().isOk())
//              .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
//              .andExpect(jsonPath("$.success").value(true))
//              .andExpect(jsonPath("$.data").exists());
//    }
//
//
//}