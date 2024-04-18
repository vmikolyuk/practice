package ru.naumen.practice.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public Client getClientById(@RequestParam(name = "id") Long id) throws NotFoundException
    {
        return clientRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @RequestMapping(value = "/rest/client", params = { "name" })
    public Client getClientByName(@RequestParam(name = "name") String name) throws NotFoundException
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
    public ClientOrder getOrderById(@RequestParam(name = "id") Long id) throws NotFoundException
    {
        return orderRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @RequestMapping(value = "/rest/order", params = { "status" })
    public List<ClientOrder> getOrdersByStatus(@RequestParam(name = "status") Integer status)
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
    public Product getProductById(@RequestParam(name = "id") Long id) throws NotFoundException
    {
        return productRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @RequestMapping(value = "/rest/product", params = { "name" })
    public Product getProductsByName(@RequestParam(name = "name") String name) throws NotFoundException
    {
        Product product = new Product();
        product.setName(name);
        return productRepository.findOne(Example.of(product)).orElseThrow(NotFoundException::new);
    }

    @RequestMapping(value = "/rest/product", params = { "categoryId" })
    public List<Product> getCategoryProducts(@RequestParam(name = "categoryId") Long categoryId)
            throws NotFoundException
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

    @GetMapping(value = "/rest/clients/{clientId}/orders")
    public List<ClientOrder> getClientOrders(@PathVariable("clientId") Long clientId) throws NotFoundException
    {
        return clientRepository.findById(clientId)
                .map(entitiesService::getClientOrders)
                .orElseThrow(NotFoundException::new);
    }

    @GetMapping(value = "/rest/clients/{clientId}/products")
    public List<Product> getClientProducts(@PathVariable("clientId") Long clientId)
    {
        return entitiesService.getClientProducts(clientId);
    }

    @RequestMapping(value = "/rest/products/popular", params = { "limit" })
    public List<Product> getTopPopularProducts(@RequestParam(name = "limit") Integer limit)
    {
        return entitiesService.getTopPopularProducts(limit).stream().limit(limit).collect(Collectors.toList());
    }

    @GetMapping(value = "/rest/products/search", params = "categoryId")
    public List<Product> searchProductByCategoryId(@RequestParam(name = "categoryId") Long categoryId)
    {
        return productRepository.findByCategoryId(categoryId);
    }

    @GetMapping(value = "/rest/products/search", params = "name")
    public List<Product> searchProductByName(@RequestParam(name = "name") String name)
    {
        return entitiesService.findProductsByName(name);
    }

    @RequestMapping(value = "/rest/clients/search", params = "name")
    public List<Client> searchClientsByName(@RequestParam(name = "name") String name)
    {
        return clientRepository.findAll()
                .stream()
                .filter(client -> client.getFullName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}
