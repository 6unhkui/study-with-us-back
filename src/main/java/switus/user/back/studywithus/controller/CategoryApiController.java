package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import switus.user.back.studywithus.domain.category.Category;
import switus.user.back.studywithus.dto.CategoryDto;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.service.CategoryService;

import java.util.List;

@Api(tags = {"4. Category"})
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CategoryApiController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public CommonResponse<List<CategoryDto.CategoryResponse>> categories() {
        return CommonResponse.success(categoryService.findAll());
    }

}
