package ru.naumen.practice.entities;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.naumen.practice.Constants.OrderStatuses;
import ru.naumen.practice.repositories.CategoryRepository;
import ru.naumen.practice.repositories.ClientRepository;
import ru.naumen.practice.repositories.OrderProductRepository;
import ru.naumen.practice.repositories.OrderRepository;
import ru.naumen.practice.repositories.ProductRepository;

/**
 * Сервис для работы с сущностями
 * @author vmikolyuk
 * @since 24.06.2021
 */
@Service
@Transactional
public class EntitiesServiceImpl
{
    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final CategoryRepository categoryRepository;

    public EntitiesServiceImpl(ClientRepository clientRepository, OrderRepository orderRepository,
            ProductRepository productRepository,
            OrderProductRepository orderProductRepository,
            CategoryRepository categoryRepository)
    {
        this.clientRepository = clientRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
        this.categoryRepository = categoryRepository;
    }

    public Client ensureClient(Integer externalId, String fullName)
    {
        Client client = new Client();
        client.setExternalId(externalId);
        client.setFullName(fullName);
        return clientRepository.findOne(Example.of(client)).orElseGet(() -> clientRepository.save(client));
    }

    public ClientOrder ensureClientOrder(Client client)
    {
        ClientOrder order = new ClientOrder();
        order.setClient(client);
        order.setStatus(OrderStatuses.CREATED.getCode());
        return orderRepository.findOne(Example.of(order)).orElseGet(() -> orderRepository.save(order));
    }

    public List<OrderProduct> getOrderProducts(ClientOrder order)
    {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setClientOrder(order);
        return orderProductRepository.findAll(Example.of(orderProduct));
    }

    public List<Product> getCategoryProducts(Category category)
    {
        Product product = new Product();
        product.setCategory(category);
        return productRepository.findAll(Example.of(product));
    }

    public void addProductToOrder(ClientOrder order, Product product)
    {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setClientOrder(order);
        orderProduct.setProduct(product);
        OrderProduct foundOrderProduct = orderProductRepository.findOne(Example.of(orderProduct)).orElse(null);
        if (foundOrderProduct == null)
        {
            orderProduct.setCountProduct(1L);
            orderProductRepository.save(orderProduct);
        }
        else
        {
            foundOrderProduct.setCountProduct(foundOrderProduct.getCountProduct() + 1);
            orderProductRepository.save(foundOrderProduct);
        }
        order.setTotal(order.getTotal() == null ? product.getPrice() : order.getTotal() + product.getPrice());
        orderRepository.save(order);
    }

    public void changeStatusOrder(ClientOrder order, Integer status)
    {
        order.setStatus(status);
        orderRepository.save(order);
    }

    public List<String> getAllCategoryNames()
    {
        return categoryRepository.findAll().stream().map(Category::getName).collect(Collectors.toList());
    }

    public Product getProductById(Long id)
    {
        return productRepository.findById(id).orElse(null);
    }

    public Category getCategoryById(Long id)
    {
        return categoryRepository.findById(id).orElse(null);
    }

    public List<Product> getClientProducts(Long clientId)
    {
        return orderProductRepository.findAll()
                .stream()
                .filter(orderProduct -> clientId.equals(orderProduct.getClientOrder().getClient().getId()))
                .map(OrderProduct::getProduct)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Product> getTopPopularProducts(Integer limit)
    {
        return productRepository.getTopPopular().stream().limit(limit).collect(Collectors.toList());
    }

    public ClientOrder getOrderById(Long id)
    {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Category> getCategoriesByParentId(Long parentId)
    {
        Category categoryExample = new Category();
        categoryExample.setParent(categoryRepository.getReferenceById(parentId));
        return categoryRepository.findAll(Example.of(categoryExample));
    }

    public Category getCategoryByName(String name)
    {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.findOne(Example.of(category)).orElse(null);
    }

    public boolean isFinalCategory(Category category)
    {
        Category categoryForFind = new Category();
        categoryForFind.setParent(category);
        return categoryRepository.findAll(Example.of(categoryForFind)).isEmpty();
    }

    public void saveClientAddress(Client client, String address)
    {
        client.setAddress(address);
        clientRepository.save(client);
    }

    public Client getClientByExternalId(Integer externalId)
    {
        Client client = new Client();
        client.setExternalId(externalId);
        return clientRepository.findOne(Example.of(client)).orElse(null);
    }

    public List<ClientOrder> getClientOrders(Client client)
    {
        ClientOrder clientOrder = new ClientOrder();
        clientOrder.setClient(client);
        return orderRepository.findAll(Example.of(clientOrder));
    }

    public List<Product> findProductsByName(String name)
    {
        return productRepository.findAll()
                .stream()
                .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}
