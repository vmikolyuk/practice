package ru.naumen.practice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ru.naumen.practice.entities.Product;

/**
 * Репозиторий для сущности "Продукт"
 * @author vmikolyuk
 * @since 23.06.2021
 */
@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends JpaRepository<Product, Long>
{
    String GET_TOP_POPULAR_QUERY = "select o.product from OrderProduct as o group by o.product.id order by "
            + "sum(o.countProduct) desc";

    @Query(GET_TOP_POPULAR_QUERY)
    List<Product> getTopPopular();

    List<Product> findByCategoryId(Long id);
}
