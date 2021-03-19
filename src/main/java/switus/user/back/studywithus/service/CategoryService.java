package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import switus.user.back.studywithus.common.error.exception.NoContentException;
import switus.user.back.studywithus.common.util.MultilingualMessageUtils;
import switus.user.back.studywithus.domain.category.Category;
import switus.user.back.studywithus.dto.CategoryDto;
import switus.user.back.studywithus.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MultilingualMessageUtils message;

    public List<CategoryDto.CategoryResponse> findAll() {
        return categoryRepository.findAll().stream().map(CategoryDto.CategoryResponse::new).collect(Collectors.toList());
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NoContentException(message.makeMultilingualMessage("category.notExist")));
    }

}
