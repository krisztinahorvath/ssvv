package example;
import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.io.Serializable;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;


public class BigBangTest {

    @Mock
    private StudentValidator studentValidator;

    @Mock
    private NotaValidator notaValidator;

    @Mock
    private TemaValidator temaValidator;

    @Mock
    private NotaXMLRepo notaXMLRepo;

    @Mock
    private StudentXMLRepo studentXMLRepo;

    @Mock
    private TemaXMLRepo temaXMLRepo;

    private Service service;

    @BeforeEach
    public void setup(){
        studentValidator = mock(StudentValidator.class);
        temaValidator = mock(TemaValidator.class);
        notaValidator = mock(NotaValidator.class);

        studentXMLRepo = mock(StudentXMLRepo.class);
        temaXMLRepo = mock(TemaXMLRepo.class);
        notaXMLRepo = mock(NotaXMLRepo.class);

        service = new Service(studentXMLRepo, studentValidator, temaXMLRepo, temaValidator, notaXMLRepo, notaValidator);
    }

    @AfterEach
    public void teardown(){
        studentValidator = null;
        temaValidator = null;
        notaValidator = null;

        studentXMLRepo = null;
        temaXMLRepo = null;
        notaXMLRepo = null;

        service = null;
    }

    @Test
    public void addStudent_nullId() {
        Student student = new Student(null, "Sharon", 933, "sharon1@email.com");

        doThrow(new ValidationException("Null student id!")).when(studentValidator).validate(student);
        Assertions.assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    public void addAssignment_emptyDescription(){
        Tema tema = new Tema("1", "", 2, 2);

        doThrow(new ValidationException("Empty description")).when(temaValidator).validate(tema);
        assertThrows(ValidationException.class, () -> service.addTema(tema));
    }

    @Test
    public void addGrade_emptyId(){
        Student student = new Student("20000", "Sharon", 933, "sharon@email.com");
        assertNull(service.addStudent(student));

        Tema tema = new Tema("1", "decriere", 2, 2);
        assertNull(service.addTema(tema));

        Nota nota = new Nota(null, "20000", "1", 10,LocalDate.of(2024,4,8));

        doThrow(new ValidationException("Empty description")).when(notaValidator).validate(nota);
        assertThrows(ValidationException.class, () -> service.addNota(nota, ""));
    }

    @Test
    public void integration_test(){
        addStudent_nullId();
        addAssignment_emptyDescription();
        addGrade_emptyId();
    }
}
