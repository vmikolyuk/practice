package ru.naumen.practice;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import net.bytebuddy.utility.RandomString;
import ru.naumen.practice.Constants.CategoryNames;
import ru.naumen.practice.entities.Category;
import ru.naumen.practice.entities.Product;
import ru.naumen.practice.repositories.CategoryRepository;
import ru.naumen.practice.repositories.ProductRepository;

/**
 * Тесты наполнения данными
 * @author vmikolyuk
 * @since 23.06.2021
 */
@SpringBootTest
public class FillingTests
{
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void createCategories()
    {
        categoryRepository.deleteAll();

        Long menuId = createAndSaveCategory(CategoryNames.MENU, 0L);

        createAndSaveCategory(CategoryNames.ABOUT_US, 0L);

        createAndSaveCategory(CategoryNames.PIZZA, menuId);

        Long rollsId = createAndSaveCategory(CategoryNames.ROLLS, menuId);
        createAndSaveCategory(CategoryNames.ROLLS_BAKED, rollsId);
        createAndSaveCategory(CategoryNames.ROLLS_CLASSIC, rollsId);
        createAndSaveCategory(CategoryNames.ROLLS_SWEET, rollsId);
        createAndSaveCategory(CategoryNames.ROLLS_SETS, rollsId);

        Long burgersId = createAndSaveCategory(CategoryNames.BURGERS, menuId);
        createAndSaveCategory(CategoryNames.BURGERS_CLASSIC, burgersId);
        createAndSaveCategory(CategoryNames.BURGERS_SPICY, burgersId);
    }

    @Test
    void createProducts()
    {
        productRepository.deleteAll();

        Category pizza = getCategoryByName(CategoryNames.PIZZA);
        Category rollsBacked = getCategoryByName(CategoryNames.ROLLS_BAKED);
        Category rollsClassic = getCategoryByName(CategoryNames.ROLLS_CLASSIC);
        Category rollsSweet = getCategoryByName(CategoryNames.ROLLS_SWEET);
        Category rollsSets = getCategoryByName(CategoryNames.ROLLS_SETS);
        Category burgersClassic = getCategoryByName(CategoryNames.BURGERS_CLASSIC);
        Category burgersSpacy = getCategoryByName(CategoryNames.BURGERS_SPICY);
        for (int i = 0; i < getRandomInt(); i++)
        {
            String strNum = String.valueOf(i);
            createAndSaveProduct("Pizza:" + strNum, getRandomDouble(), pizza);
            createAndSaveProduct("RollsBacked:" + strNum, getRandomDouble(), rollsBacked);
            createAndSaveProduct("RollsClassic:" + strNum, getRandomDouble(), rollsClassic);
            createAndSaveProduct("RollsSweet:" + strNum, getRandomDouble(), rollsSweet);
            createAndSaveProduct("RollsSets:" + strNum, getRandomDouble(), rollsSets);
            createAndSaveProduct("BurgersClassic:" + strNum, getRandomDouble(), burgersClassic);
            createAndSaveProduct("BurgersSpacy:" + strNum, getRandomDouble(), burgersSpacy);
        }

    }

    private Long createAndSaveCategory(String name, Long parentId)
    {
        Category category = new Category();
        category.setName(name);
        category.setParentId(parentId);
        return categoryRepository.save(category).getId();
    }

    private void createAndSaveProduct(String name, Double price, Category category)
    {
        Product product = new Product();
        product.setName(name);
        product.setDescription(RandomString.make());
        product.setPrice(price);
        product.setCategory(category);
        productRepository.save(product);
    }

    private Category getCategoryByName(String name)
    {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.findOne(Example.of(category)).orElse(null);
    }

    private static int getRandomInt()
    {
        return new Random().nextInt(5 - 3) + 5;
    }

    private static double getRandomDouble()
    {
        return 10 + (100 - 10) * new Random().nextDouble();
    }

}
