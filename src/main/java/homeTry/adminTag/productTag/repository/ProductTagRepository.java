package homeTry.adminTag.productTag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import homeTry.adminTag.productTag.model.entity.ProductTag;

@Repository
public interface ProductTagRepository extends JpaRepository<ProductTag, Long>{   
}