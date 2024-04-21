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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


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
    public void addStudent_emptyId(){
        Student student = new Student("", "Sharon", 933, "sharon1@email.com");

        doThrow(new ValidationException("Null student id!")).when(studentValidator).validate(student);
        Assertions.assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    public void addAssignment_invalidDeadline(){
        Tema tema = new Tema("1", "decriere", 0, 2);

        doThrow(new ValidationException("Invalid deadline!")).when(temaValidator).validate(tema);
        Assertions.assertThrows(ValidationException.class, () -> service.addTema(tema));
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

    @Test
    public void addAssignment_incremental() {
        // add student
        Student student = new Student("20000", "Sharon", 933, "sharon@email.com");

        assertNull(service.addStudent(student));

        // add assignment
        Tema tema = new Tema("1", "decriere", 2, 2);
        assertNull(service.addTema(tema));
    }

    @Test
    public void addGradeAssignment_incremental() {
        Student student = new Student("1", "mariana", 933, "mariana@email.com");

        Tema tema = new Tema("1", "descriere", 12, 2);

        Nota nota = new Nota("1", "1", "1", 10, LocalDate.of(2024, 12, 10));

        doNothing().when(studentValidator).validate(student);
        when(studentXMLRepo.save(student)).thenReturn(null);

        doNothing().when(temaValidator).validate(tema);
        when(temaXMLRepo.save(tema)).thenReturn(null);

        when(studentXMLRepo.findOne("1")).thenReturn(student);
        when(temaXMLRepo.findOne("1")).thenReturn(tema);

        Student returnedStudent = service.addStudent(student);
        Assertions.assertNull(returnedStudent);

        Tema returnedTema = service.addTema(tema);
        Assertions.assertNull(returnedTema);

        doThrow(new ValidationException("Assignment deadline passed")).when(notaValidator).validate(nota);
        assertThrows(ValidationException.class, () -> service.addNota(nota, ""));
    }
}
