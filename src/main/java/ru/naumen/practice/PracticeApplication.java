package ru.naumen.practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class PracticeApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(PracticeApplication.class, args);

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class);
        Company company = context.getBean(Company.class);

        company.hashCode();
    }
}
