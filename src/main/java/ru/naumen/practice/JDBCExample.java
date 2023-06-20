package ru.naumen.practice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author vmikolyuk
 * @since 26.05.2023
 */
public class JDBCExample
{
    private static final String URL = "jdbc:h2:file:~/practice";

    public static void main(String[] args)
    {
        String query = "SELECT * FROM student"
                + " WHERE name LIKE '%Ivan%'"
                + " ORDER BY id";
        try (Connection connection = DriverManager.getConnection(URL, "sa", ""))
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next())
            {
                String studentName = resultSet.getString("name");
                System.out.println(studentName);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
