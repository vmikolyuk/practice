package ru.naumen.practice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 * Сущность "Заказ"
 * @author vmikolyuk
 * @since 23.06.2021
 */
@Entity
public class ClientOrder
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Client client;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false)
    private Double total;

    public Client getClient()
    {
        return client;
    }

    public Long getId()
    {
        return id;
    }

    public Integer getStatus()
    {
        return status;
    }

    public Double getTotal()
    {
        return total;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public void setTotal(Double total)
    {
        this.total = total;
    }
}
