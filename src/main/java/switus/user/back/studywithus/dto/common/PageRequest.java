package switus.user.back.studywithus.dto.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor @Builder
public class PageRequest {
    private int page;
    private int size;
    private Sort.Direction direction;

    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
    }

    public void setSize(int size) {
        int DEFAULT_SIZE = 10;
        int MAX_SIZE = 50;
        this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }

    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(page -1, size, direction, "name");
    }
}
