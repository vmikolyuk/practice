package ru.naumen.practice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.naumen.practice.entities.Student;

/**
 * Репозиторий для сущности "Клиент"
 *
 * @author vmikolyuk
 * @since 23.06.2021
 */
public interface StudentRepository extends JpaRepository<Student, Long>
{
}
