package switus.user.back.studywithus.dto;


import lombok.Data;
import switus.user.back.studywithus.domain.category.Category;

public class CategoryDto {

    @Data
    public static class CategoryResponse {
        private Long categoryId;
        private String name;

        public CategoryResponse(Category category){
            this.categoryId = category.getId();
            this.name = category.getName();
        }
    }
}
