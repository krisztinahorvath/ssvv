package validation;

import domain.Student;
import repository.StudentXMLRepo;

import java.util.Objects;

public class StudentValidator implements Validator<Student> {

    /**
     * Valideaza un student
     * @param entity - studentul pe care il valideaza
     * @throws ValidationException - daca studentul nu e valid
     */
    @Override
    public void validate(Student entity) throws ValidationException {
        if(entity.getID() == null){ // bug fix 1, moved if up
            throw new ValidationException("Id incorect!");
        }

        if(entity.getID().equals("")){
            throw new ValidationException("Id incorect!");
        }

        if(entity.getNume() == ""){
            throw new ValidationException("Nume incorect!");
        }

        if(entity.getGrupa() <= 0) { // bug fix 2, before it was < 0
            throw new ValidationException("Grupa incorecta!");
        }

        if(entity.getEmail() == null){
            throw new ValidationException("Email incorect!");
        }

        if(entity.getNume() == null){
            throw new ValidationException("Nume incorect!");
        }

        if(entity.getEmail().equals("")){
            throw new ValidationException("Email incorect!");
        }
    }

    public void validateEmail(StudentXMLRepo studentFileRepository, String email){
        for(Student student: studentFileRepository.findAll()){
            if(Objects.equals(student.getEmail(), email))
                throw new ValidationException("Email is not unque!");
        }
    }
}
