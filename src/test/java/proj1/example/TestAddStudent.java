package proj1.example;

import domain.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;

public class TestAddStudent{
    public static Service service;

    @BeforeAll
    public static void setup(){
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        String filenameStudent = "fisiere/Studenti.xml";
        String filenameTema = "fisiere/Teme.xml";
        String filenameNota = "fisiere/Note.xml";

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @Test
    public void addStudentWorks(){
        // valid student
        Student student2 = new Student("1234567", "maria", 933, "maria@email.com");

        service.addStudent(student2);
    }

    @Test
    public void addStudentFails(){
        Student student1 = new Student("", "ana", 933, "ana@email.com");

        try{
            service.addStudent(student1);
        } catch (Exception e){

        }


    }
}