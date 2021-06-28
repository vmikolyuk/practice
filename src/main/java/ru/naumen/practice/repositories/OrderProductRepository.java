package ru.naumen.practice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.naumen.practice.entities.OrderProduct;

/**
 * Репозиторий для сущности "Заказ-Продукт"
 * @author vmikolyuk
 * @since 24.06.2021
 */
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long>
{
}