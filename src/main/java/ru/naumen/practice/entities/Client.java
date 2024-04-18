package ru.naumen.practice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Сущность "Клиент"
 * @author vmikolyuk
 * @since 23.06.2021
 */
@Entity
public class Client
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer externalId;

    @Column(nullable = false)
    private String fullName;

    @Column
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    public String getAddress()
    {
        return address;
    }

    public Integer getExternalId()
    {
        return externalId;
    }

    public Long getId()
    {
        return id;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public void setExternalId(Integer externalId)
    {
        this.externalId = externalId;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }
}
