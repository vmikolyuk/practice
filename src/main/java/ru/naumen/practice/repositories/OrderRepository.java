package ru.naumen.practice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ru.naumen.practice.entities.ClientOrder;

/**
 * Репозиторий для сущности "Заказ"
 * @author vmikolyuk
 * @since 23.06.2021
 */
@RepositoryRestResource(collectionResourceRel = "orders", path = "orders")
public interface OrderRepository extends JpaRepository<ClientOrder, Long>
{
}
