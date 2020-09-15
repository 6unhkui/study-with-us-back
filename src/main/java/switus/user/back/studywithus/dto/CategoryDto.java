package switus.user.back.studywithus.dto;


import lombok.Data;
import switus.user.back.studywithus.domain.category.Category;

public class CategoryDto {

    @Data
    public static class CategoryResponse {
        private Long id;
        private String name;

        public CategoryResponse(Category category){
            this.id = category.getId();
            this.name = category.getName();
        }
    }
}
