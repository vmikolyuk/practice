package ru.naumen.practice.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.naumen.practice.entities.Category;
import ru.naumen.practice.entities.Client;
import ru.naumen.practice.entities.ClientOrder;
import ru.naumen.practice.entities.EntitiesServiceImpl;
import ru.naumen.practice.entities.Product;
import ru.naumen.practice.repositories.CategoryRepository;
import ru.naumen.practice.repositories.ClientRepository;
import ru.naumen.practice.repositories.OrderRepository;
import ru.naumen.practice.repositories.ProductRepository;

/**
 * Контролер для работы с рест-запросами
 * @author vmikolyuk
 * @since 25.06.2021
 */
@RestController
public class RestQueryController
{
    private final EntitiesServiceImpl entitiesService;
    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public RestQueryController(EntitiesServiceImpl entitiesService, ClientRepository clientRepository,
            OrderRepository orderRepository, ProductRepository productRepository,
            CategoryRepository categoryRepository)
    {
        this.entitiesService = entitiesService;
        this.clientRepository = clientRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/rest/clients")
    public List<Client> getAllClients()
    {
        return clientRepository.findAll();
    }

    @RequestMapping(value = "/rest/client", params = { "id" })
    public Client getClientById(@RequestParam Long id) throws NotFoundException
    {
        return clientRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @RequestMapping(value = "/rest/client", params = { "name" })
    public Client getClientByName(@RequestParam String name) throws NotFoundException
    {
        Client client = new Client();
        client.setFullName(name);
        return clientRepository.findOne(Example.of(client)).orElseThrow(NotFoundException::new);
    }

    @GetMapping("/rest/orders")
    public List<ClientOrder> getAllOrders()
    {
        return orderRepository.findAll();
    }

    @RequestMapping(value = "/rest/order", params = { "id" })
    public ClientOrder getOrderById(@RequestParam Long id) throws NotFoundException
    {
        return orderRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @RequestMapping(value = "/rest/order", params = { "status" })
    public List<ClientOrder> getOrdersByStatus(@RequestParam Integer status)
    {
        ClientOrder order = new ClientOrder();
        order.setStatus(status);
        return orderRepository.findAll(Example.of(order));
    }

    @GetMapping("/rest/products")
    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }

    @RequestMapping(value = "/rest/product", params = { "id" })
    public Product getProductById(@RequestParam Long id) throws NotFoundException
    {
        return productRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @RequestMapping(value = "/rest/product", params = { "name" })
    public Product getProductsByName(@RequestParam String name) throws NotFoundException
    {
        Product product = new Product();
        product.setName(name);
        return productRepository.findOne(Example.of(product)).orElseThrow(NotFoundException::new);
    }

    @RequestMapping(value = "/rest/product", params = { "categoryId" })
    public List<Product> getCategoryProducts(@RequestParam Long categoryId) throws NotFoundException
    {
        return categoryRepository.findById(categoryId)
                .map(entitiesService::getCategoryProducts)
                .orElseThrow(NotFoundException::new);
    }

    @GetMapping("/rest/categories")
    public List<Category> getAllCategories()
    {
        return categoryRepository.findAll();
    }

    @RequestMapping(value = "/rest/clientOrders", params = { "clientId" })
    public List<ClientOrder> getClientOrders(@RequestParam Long clientId) throws NotFoundException
    {
        return clientRepository.findById(clientId)
                .map(entitiesService::getClientOrders)
                .orElseThrow(NotFoundException::new);
    }

    @RequestMapping(value = "/rest/clientProducts", params = { "clientId" })
    public List<Product> getClientProducts(@RequestParam Long clientId)
    {
        return entitiesService.getClientProducts(clientId);
    }

    @RequestMapping(value = "/rest/popularProducts", params = { "top" })
    public List<Product> getTopPopularProducts(@RequestParam Integer top)
    {
        return entitiesService.getTopPopularProducts(top).stream().limit(top).collect(Collectors.toList());
    }

}
