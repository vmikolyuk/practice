package ru.naumen.practice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * $
 * @author vmikolyuk
 * @since 01.07.2021
 */
@Configuration
@ComponentScan(basePackages = "ru.naumen.practice")
public class Config
{
    @Bean
    public Address getAddress()
    {
        return new Address("exampleStreet");
    }
}
