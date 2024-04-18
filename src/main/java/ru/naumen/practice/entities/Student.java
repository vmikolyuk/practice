package ru.naumen.practice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Сущность "Клиент"
 *
 * @author vmikolyuk
 * @since 23.06.2021
 */
@Entity
public class Student
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String name;

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
