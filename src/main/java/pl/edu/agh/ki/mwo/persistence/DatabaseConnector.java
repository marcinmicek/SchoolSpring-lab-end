package pl.edu.agh.ki.mwo.persistence;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.edu.agh.ki.mwo.model.School;
import pl.edu.agh.ki.mwo.model.SchoolClass;
import pl.edu.agh.ki.mwo.model.Student;

public class DatabaseConnector {
	
	protected static DatabaseConnector instance = null;
	
	public static DatabaseConnector getInstance() {
		if (instance == null) {
			instance = new DatabaseConnector();
		}
		return instance;
	}
	
	Session session;

	protected DatabaseConnector() {
		session = HibernateUtil.getSessionFactory().openSession();
	}
	
	public void teardown() {
		session.close();
		HibernateUtil.shutdown();
		instance = null;
	}
	
	public Iterable<School> getSchools() {
		String hql = "FROM School";
		Query query = session.createQuery(hql);
		List<School> schools = query.list();
		
		return schools;
	}
	
	public void addSchool(School school) {
		Transaction transaction = session.beginTransaction();
		session.save(school);
		transaction.commit();
	}
	
	public void deleteSchool(String schoolId) {
		String hql = "FROM School S WHERE S.id=" + schoolId;
		Query query = session.createQuery(hql);
		List<School> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (School s : results) {
			session.delete(s);
		}
		transaction.commit();
	}
	
	public void updateSchool(String schoolId, String name, String address) {
		String hql = "FROM School S WHERE S.id=" + schoolId;
		Query query = session.createQuery(hql);
		School school = (School) query.uniqueResult();
		school.setName(name);
		school.setAddress(address);
		Transaction transaction = session.beginTransaction();
		session.update(school);
		transaction.commit();
	}

	public Iterable<SchoolClass> getSchoolClasses() {
		String hql = "FROM SchoolClass";
		Query query = session.createQuery(hql);
		List<SchoolClass> schoolClasses = query.list();
		
		return schoolClasses;
	}
	
	public void addSchoolClass(SchoolClass schoolClass, String schoolId) {
		String hql = "FROM School S WHERE S.id=" + schoolId;
		Query query = session.createQuery(hql);
		List<School> results = query.list();
		Transaction transaction = session.beginTransaction();
		if (results.size() == 0) {
			session.save(schoolClass);
		} else {
			School school = results.get(0);
			school.addClass(schoolClass);
			session.save(school);
		}
		transaction.commit();
	}
	
	public void deleteSchoolClass(String schoolClassId) {
		String hql = "FROM SchoolClass SC WHERE SC.id=" + schoolClassId;
		Query query = session.createQuery(hql);
		List<SchoolClass> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (SchoolClass sc : results) {
			session.delete(sc);
		}
		transaction.commit();
	}
	
	public void updateSchoolClass(String schoolClassId, int startYear, int currentYear, String profile, String schoolId) {
		String hql = "FROM SchoolClass SC WHERE SC.id=" + schoolClassId;
		Query query = session.createQuery(hql);
		SchoolClass schoolClass = (SchoolClass) query.uniqueResult();
		schoolClass.setStartYear(startYear);
		schoolClass.setCurrentYear(currentYear);
		schoolClass.setProfile(profile);
		
		String hql2 = "FROM School S WHERE S.id=" + schoolId;
		Query query2 = session.createQuery(hql2);
		List<School> results = query2.list();
		
		String hql3 = "FROM School";
		Query query3 = session.createQuery(hql3);
		List<School> allSchools = query3.list();
		
		Transaction transaction = session.beginTransaction();
		
		for(School schoolAll : allSchools) {
			schoolAll.removeClass(schoolClass);
			session.save(schoolAll);
		}
		
		if (results.size() == 0) {
			session.update(schoolClass);
		} else {			
			School school = results.get(0);
			school.addClass(schoolClass);
			session.save(school);
		}
		transaction.commit();
	}
	
	public Iterable<Student> getStudents() {
		String hql = "FROM Student";
		Query query = session.createQuery(hql);
		List<Student> students = query.list();
		
		return students;
	}
	
	public void addStudent(Student student, String schoolClassId) {
		String hql = "FROM SchoolClass SC WHERE SC.id=" + schoolClassId;
		Query query = session.createQuery(hql);
		List<SchoolClass> results = query.list();
		Transaction transaction = session.beginTransaction();
		if (results.size() == 0) {
			session.save(student);
		} else {
			SchoolClass schoolClass = results.get(0);
			schoolClass.addStudent(student);
			session.save(schoolClass);
		}
		transaction.commit();
	}
	
	public void deleteStudent(String studentId) {
		String hql = "FROM Student ST WHERE ST.id=" + studentId;
		Query query = session.createQuery(hql);
		List<Student> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (Student st : results) {
			session.delete(st);
		}
		transaction.commit();
	}
	
	public void updateStudent(String studentId, String name, String surname, String pesel, String classId) {
		String hql = "FROM Student ST WHERE ST.id=" + studentId;
		Query query = session.createQuery(hql);
		Student student = (Student) query.uniqueResult();
		student.setName(name);
		student.setSurname(surname);
		student.setPesel(pesel);
	
		String hql2 = "FROM SchoolClass SC WHERE SC.id=" + classId;
		Query query2 = session.createQuery(hql2);
		List<SchoolClass> results = query2.list();
		
		String hql3 = "FROM SchoolClass";
		Query query3 = session.createQuery(hql3);
		List<SchoolClass> allSchoolClasses = query3.list();
		
		Transaction transaction = session.beginTransaction();
		
		for(SchoolClass classAll : allSchoolClasses) {
			classAll.removeStudent(student);
			session.save(classAll);
		}
		
		if (results.size() == 0) {
			session.update(student);
		} else {
			SchoolClass schoolClass = results.get(0);
			schoolClass.addStudent(student);
			session.save(schoolClass);
		}
		transaction.commit();
	}

	public School getSchool(String schoolId) {
		String hql = "FROM School S WHERE S.id=" + schoolId;
		Query query = session.createQuery(hql);
		School school =  (School) query.uniqueResult();
		
		return school;
	}
	
	public SchoolClass getSchoolClass(String schoolClassId) {
		String hql = "FROM SchoolClass SC WHERE SC.id=" + schoolClassId;
		Query query = session.createQuery(hql);
		SchoolClass schoolClass =  (SchoolClass) query.uniqueResult();
		
		return schoolClass;
	}
	
	public Student getStudent(String studentId) {
		String hql = "FROM Student ST WHERE ST.id=" + studentId;
		Query query = session.createQuery(hql);
		Student student =  (Student) query.uniqueResult();
		
		return student;
	}

}