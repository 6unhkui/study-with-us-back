package switus.user.back.studywithus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import switus.user.back.studywithus.domain.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
