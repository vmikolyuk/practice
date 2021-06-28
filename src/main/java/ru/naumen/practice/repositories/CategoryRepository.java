package ru.naumen.practice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ru.naumen.practice.entities.Category;

/**
 * Репозиторий для сущности "Категория"
 * @author vmikolyuk
 * @since 23.06.2021
 */
@RepositoryRestResource(collectionResourceRel = "categories", path = "categories")
public interface CategoryRepository extends JpaRepository<Category, Long>
{
}
