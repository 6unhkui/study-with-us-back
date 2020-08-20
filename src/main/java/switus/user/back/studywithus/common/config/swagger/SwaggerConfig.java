package switus.user.back.studywithus.common.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        // 에러 리스트
        List<ResponseMessage> resMsg = Arrays.asList(
                new ResponseMessageBuilder().code(401).message("No permission").responseModel(new ModelRef("Error")).build(),
                new ResponseMessageBuilder().code(400).message("Bad Request").responseModel(new ModelRef("Error")).build(),
                new ResponseMessageBuilder().code(404).message("URL does not exist").responseModel(new ModelRef("Error")).build(),
                new ResponseMessageBuilder().code(500).message("Server Error").responseModel(new ModelRef("Error")).build()
        );

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(swaggerInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("switus.user.back.studywithus.api")) // 현재 RequestMapping으로 할당된 모든 URL 리스트를 추출
                .paths(PathSelectors.ant("/api/**")) // 그중 /api/** 인 URL들만 필터링
                .build()
                .useDefaultResponseMessages(false);
//                .globalResponseMessage(RequestMethod.GET, resMsg)
//                .globalResponseMessage(RequestMethod.POST, resMsg)
//                .globalResponseMessage(RequestMethod.PUT, resMsg)
//                .globalResponseMessage(RequestMethod.DELETE, resMsg);
    }

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder()
                .title("Study With Us Rest API Docs")
                .version("1.0.0")
                .build();
    }

}
