package ru.naumen.practice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 * Сущность "Заказ-продукт"
 * @author vmikolyuk
 * @since 23.06.2021
 */
@Entity
public class OrderProduct
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private ClientOrder clientOrder;

    @ManyToOne(optional = false)
    private Product product;

    @Column(nullable = false)
    private Long countProduct;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public ClientOrder getClientOrder()
    {
        return clientOrder;
    }

    public void setClientOrder(ClientOrder clientOrder)
    {
        this.clientOrder = clientOrder;
    }

    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public Long getCountProduct()
    {
        return countProduct;
    }

    public void setCountProduct(Long countProduct)
    {
        this.countProduct = countProduct;
    }
}
