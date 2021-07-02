package ru.naumen.practice;

import org.springframework.stereotype.Component;

/**
 * $
 * @author vmikolyuk
 * @since 01.07.2021
 */
@Component
public class Company
{
    private Address address;

    public Company(Address address)
    {
        this.address = address;
    }
}
