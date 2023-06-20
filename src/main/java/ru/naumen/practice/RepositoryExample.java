package ru.naumen.practice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.naumen.practice.entities.Student;
import ru.naumen.practice.repositories.StudentRepository;

/**
 * @author vmikolyuk
 * @since 26.05.2023
 */
@Component
public class RepositoryExample
{
    @Autowired
    private StudentRepository studentRepository;

    public void main(String[] args)
    {
        List<Student> students = studentRepository.findAll();
        students.forEach(entity ->
                System.out.println(entity.getName())
        );
    }
}
