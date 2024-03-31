package example;

import domain.Tema;
import org.junit.jupiter.api.*;

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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class AddAssignmentTest {
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
    public void addAssignmentWorks(){
        Tema tema = new Tema("1", "decriere", 2, 2);
        assertNull(service.addTema(tema));
    }

    @Test
    public void addAssignmentEmpyDescription(){
        Tema tema = new Tema("1", "", 2, 2);
        assertThrows(ValidationException.class, () -> service.addTema(tema));
    }

    @Test
    public void addAssignmentInvalidDeadline(){
        Tema tema = new Tema("1", "decriere", 0, 2);
        assertThrows(ValidationException.class, () -> service.addTema(tema));
    }

    @Test
    public void addAssignmentInvalidPrimire(){
        Tema tema = new Tema("1", "decriere", 2, 0);
        assertThrows(ValidationException.class, () -> service.addTema(tema));
    }

    @Test
    public void addAssignmentNullNrTema(){
        Tema tema = new Tema(null, "decriere", 2, 2);
        assertThrows(ValidationException.class, () -> service.addTema(tema));
    }

    @Test
    public void addAssignmentEmptyNrTema(){
        Tema tema = new Tema("", "decriere", 2, 2);
        assertThrows(ValidationException.class, () -> service.addTema(tema));
    }
}
