package ru.naumen.practice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ru.naumen.practice.entities.Client;
import ru.naumen.practice.entities.Product;

/**
 * Репозиторий для сущности "Клиент"
 * @author vmikolyuk
 * @since 23.06.2021
 */
@RepositoryRestResource(collectionResourceRel = "clients", path = "clients")
public interface ClientRepository extends JpaRepository<Client, Long>
{
    String GET_ALL_PRODUCT_QUERY = "from Product";

    @Query(GET_ALL_PRODUCT_QUERY)
    List<Product> getAllProduct(@Param("clientId") Long clientId);
}
