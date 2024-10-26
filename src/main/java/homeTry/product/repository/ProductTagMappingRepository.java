package homeTry.product.repository;

import homeTry.product.model.entity.ProductTagMapping;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTagMappingRepository extends JpaRepository<ProductTagMapping, Long> {

    List<ProductTagMapping> findByProductTagIdIn(List<Long> tagIds);

}
