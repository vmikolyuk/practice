package ru.naumen.practice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ru.naumen.practice.entities.Client;

/**
 * Репозиторий для сущности "Клиент"
 * @author vmikolyuk
 * @since 23.06.2021
 */
@RepositoryRestResource(collectionResourceRel = "clients", path = "clients")
public interface ClientRepository extends JpaRepository<Client, Long>
{
}
