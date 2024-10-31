package homeTry.product.repository;

import homeTry.product.model.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Slice<Product> findAllByOrderByPriceAsc(Pageable pageable);

    Slice<Product> findByIdInOrderByPriceAsc(List<Long> ids, Pageable pageable);

    Page<Product> findAllByOrderByIdAsc(Pageable pageable);

}
