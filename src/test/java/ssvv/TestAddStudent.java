package ssvv;

import domain.Student;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestAddStudent{
    public static Service service;

    @BeforeAll
    public static void setup(){
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        String filenameStudent = "fisiere/TestStudenti.xml";
        String filenameTema = "fisiere/TestTeme.xml";
        String filenameNota = "fisiere/TestNote.xml";

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @AfterAll
    public static void tearDown() {
        String filenameStudent = "fisiere/TestStudenti.xml";
        String filenameTema = "fisiere/TestTeme.xml";
        String filenameNota = "fisiere/TestNote.xml";

        try {
            Files.write(Paths.get(filenameStudent), "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><inbox> </inbox>".getBytes());
            Files.write(Paths.get(filenameTema), "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><inbox> </inbox>".getBytes());
            Files.write(Paths.get(filenameNota), "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><inbox> </inbox>".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(1)
    public void addStudentWorks() {
        Student student = new Student("20000", "Sharon", 933, "sharon@email.com");
        assertNull(service.addStudent(student));
    }

    @Test
    @Order(2)
    public void addStudentNonUniqueId(){
        Student student = new Student("20000", "Sharon", 933, "sharon1@email.com");
        Student retStudent = service.addStudent(student);
        assertEquals(retStudent.getID(), "20000");
    }

    @Test
    @Order(3)
    public void addStudentNullId(){
        Student student = new Student(null, "Sharon", 933, "sharon1@email.com");
        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    @Order(4)
    public void addStudentEmptyId(){
        Student student = new Student("", "Sharon", 933, "sharon1@email.com");
        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    @Order(5)
    public void addStudentNullName() {
        Student student = new Student("20001", null, 933, "sharon1@email.com");
        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    @Order(6)
    public void addStudentEmptyName() {
        Student student = new Student("20001", "", 933, "sharon1@email.com");
        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    @Order(7)
    public void addStudentNegativeGroupNo() {
        Student student = new Student("20001", "Sharon", -933, "sharon1@email.com");
        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    @Order(8)
    public void addStudentEmptyEmail() {
        Student student = new Student("20001", "Sharon", 933, "");
        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    @Order(9)
    public void addStudentNullEmail() {
        Student student = new Student("20001", "Sharon", 933, null);
        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    @Order(10)
    public void addStudentDuplicateEmail() {
        Student student = new Student("20001", "Sharon", 933, "sharon@email.com");
        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    @Order(11)
    public void addStudentNotStrictlyPositiveGroupNo() {
        Student student = new Student("20001", "Sharon", 0, "sharon@email.com");
        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }
}