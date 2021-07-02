package ru.naumen.practice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * $
 * @author vmikolyuk
 * @since 30.06.2021
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestHomePage
{
    @Test
    void testHomePage() throws IOException, InterruptedException
    {
        URL url = new URL("http://localhost:8080");
        String content;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream())))
        {
            content = reader.lines().collect(Collectors.joining());
            System.out.println(content);
            assert content.contains("Hello");
            assert content.contains("world");
        }
    }
}
