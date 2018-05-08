package pl.edu.agh.ki.mwo.web.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.edu.agh.ki.mwo.model.School;
import pl.edu.agh.ki.mwo.model.Student;
import pl.edu.agh.ki.mwo.persistence.DatabaseConnector;

@Controller
public class StudentsController {

    @RequestMapping(value="/Students")
    public String listStudents(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";

    	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("schools", DatabaseConnector.getInstance().getSchools());
    	model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
    	
        return "studentsList";    
    }
    
    @RequestMapping(value="/AddStudent")
    public String displayAddStudentForm(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
    	
        return "studentForm";    
    }

    @RequestMapping(value="/CreateStudent", method=RequestMethod.POST)
    public String createStudent(@RequestParam(value="studentName", required=false) String name,
    		@RequestParam(value="studentSurname", required=false) String surname,
    		@RequestParam(value="studentPesel", required=false) String pesel,
    		@RequestParam(value="studentClass", required=false) String classId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	Student student = new Student();
    	student.setName(name);
    	student.setSurname(surname);
    	student.setPesel(pesel);
    	
    	DatabaseConnector.getInstance().addStudent(student, classId);    	
       	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("message", "Nowy uczeń został dodany");
         	
    	return "studentsList";
    }
    
    @RequestMapping(value="/DeleteStudent", method=RequestMethod.POST)
    public String deleteStudent(@RequestParam(value="studentId", required=false) String studentId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    
    	DatabaseConnector.getInstance().deleteStudent(studentId);    	
       	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("message", "Uczeń został usunięty");
         	
    	return "studentsList";
    }
    
    @RequestMapping(value="/ModifyStudent")
    public String modifyStudent(@RequestParam(value="studentId", required=true) String studentId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	model.addAttribute("student", DatabaseConnector.getInstance().getStudent(studentId));
    	model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
    	
    	return "studentModifyForm";
    }
    
    @RequestMapping(value="/UpdateStudent", method=RequestMethod.POST)
    public String updateStudent(@RequestParam(value="studentName", required=false) String name,
    		@RequestParam(value="studentSurname", required=false) String surname,
    		@RequestParam(value="studentPesel", required=false) String pesel,
    		@RequestParam(value="studentId", required=false) String studentId,
    		@RequestParam(value="studentClass", required=false) String class_id,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	DatabaseConnector.getInstance().updateStudent(studentId, name, surname, pesel, class_id);    	
       	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("message", "Dane ucznia zostały zaktualizowane");
         	
    	return "studentsList";
	}

}