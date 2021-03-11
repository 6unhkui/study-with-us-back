//package switus.user.back.studywithus.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import switus.user.back.studywithus.common.properties.AppAccountProperties;
//import switus.user.back.studywithus.common.security.JwtTokenProvider;
//import switus.user.back.studywithus.domain.account.Account;
//import switus.user.back.studywithus.domain.account.AccountRole;
//import switus.user.back.studywithus.dto.PostDto;
//import switus.user.back.studywithus.dto.common.CurrentAccount;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static switus.user.back.studywithus.common.constant.SecurityConstants.TOKEN_PREFIX;
//
//@Transactional
//@ActiveProfiles("test")
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class RoomPostApiControllerTest {
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
//    public String getAccessToken() {
//        return TOKEN_PREFIX + jwtTokenProvider.generate(
//                new CurrentAccount(Account.builder().id(1L).email(this.account.getUserEmail()).name("user").role(AccountRole.USER).build()));
//    }
//
//    @Test
//    public void 게시글_작성() throws Exception {
//        //given
//        Long roomId = 1L;
//
//        PostDto.SaveRequest request = new PostDto.SaveRequest();
//        request.setTitle("제목");
//        request.setContent("내용");
//
//        //when
//        ResultActions result = mockMvc.perform(post( "/api/v1/room/{roomId}/post", roomId)
//                .header(HttpHeaders.AUTHORIZATION, getAccessToken()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(request)));
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
//    public void 게시글_리스트() throws Exception {
//        //given
//        Long roomId = 1L;
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("size", "6");
//        params.add("page", "1");
//        params.add("direction", String.valueOf(Sort.Direction.ASC));
////        params.add("keyword", "user");
////        params.add("orderType", MemberDto.SearchRequest.OrderType.JOIN_DATE.name());
//
//        //when
//        ResultActions result = mockMvc.perform(get("/api/v1/room/{roomId}/posts", roomId)
//                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
//                .params(params));
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
//}