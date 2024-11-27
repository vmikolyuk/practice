package ru.naumen.practice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ru.naumen.practice.entities.OrderProduct;

/**
 * Репозиторий для сущности "Заказ-Продукт"
 * @author vmikolyuk
 * @since 24.06.2021
 */
@RepositoryRestResource(collectionResourceRel = "order-products", path = "order-products")
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long>
{
}