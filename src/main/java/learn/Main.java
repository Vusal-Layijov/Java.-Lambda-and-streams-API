package learn;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        StudentDataStore ds = new StudentDataStore();
        List<Student> students = ds.all();

        // 0. Print all students
        // iteration solution
//        for (Student student : students) {
//            System.out.println(student);
//        }
//
//        // stream solution
//        students.stream().forEach(System.out::println);

        // 1. Print students from Argentina
        System.out.println("1. Print students from Argentina");
        students.stream()
                .filter(i->i.getCountry().equalsIgnoreCase("Argentina"))
                .forEach(System.out::println);

        // 2. Print students whose last names starts with 'T'.
        students.stream()
                .filter(s->s.getFirstName().startsWith("T"))
                .forEach(System.out::println);

        // 3. Print students from Argentina, ordered by GPA
        students.stream()
                .filter(i->i.getCountry().equalsIgnoreCase("Argentina"))
                .sorted(Comparator.comparing(Student::getGpa).reversed())
                .forEach(System.out::println);

        // 4. Print the bottom 10% (100 students) ranked by GPA.
        students.stream()
                .sorted(Comparator.comparing(Student::getGpa))
                .limit(100)
                .forEach(System.out::println);

        // 5. Print the 4th - 6th ranked students by GPA from Argentina
        students.stream()
                .filter(s->s.getCountry().equalsIgnoreCase("Argentina"))
                .sorted(Comparator.comparing(Student::getGpa).reversed())
                .skip(3)
                .limit(3)
                .forEach(System.out::println);

        // 6. Is anyone from Maldives?
        System.out.println("Is anyone from Maldives?");
        System.out.println("From Maldives?: "+students.stream()
                .anyMatch(s->s.getCountry().equalsIgnoreCase("Maldives"))

        );

        // 7. Does everyone have a non-null, non-empty email address?
        System.out.println("Does everyone have a non-null, non-empty email address?");
        System.out.println("All have email?: "+students.stream()
                .allMatch(i->i.getEmailAddress() !=null && !i.getEmailAddress().isBlank())
        );

        // 8. Print students who are currently registered for 5 courses.
        System.out.println(" Print students who are currently registered for 5 courses.");
        students.stream()
                .filter(s->s.getRegistrations().size()==5)
                .forEach(System.out::println);

        // 9. Print students who are registered for the course "Literary Genres".
        System.out.println("Print students who are registered for the course \"Literary Genres\".");
        students.stream()
                .filter(s->s.getRegistrations().stream()
                        .anyMatch(r->r.getCourse().equalsIgnoreCase("Literary Genres")))
                .forEach(System.out::println);
        // 10. Who has the latest birthday? Who is the youngest?
        System.out.println("Who has the latest birthday? Who is the youngest?");
        students.stream()
                .sorted(Comparator.comparing(Student::getBirthDate).reversed())
                .limit(1)
                .forEach(System.out::println);

        // 11. Who has the highest GPA? There may be a tie.
        System.out.println("Who has the highest GPA? There may be a tie.");
        BigDecimal maxGpa = students.stream()
                .map(Student::getGpa).sorted(Comparator.reverseOrder())
                .findFirst()
                .orElse(BigDecimal.ZERO);
        students.stream()
                .sorted(Comparator.comparing(Student::getGpa).reversed())
                .takeWhile(i->i.getGpa().equals(maxGpa))
                .forEach(System.out::println);
        // 12. Print every course students are registered for, including repeats.
        System.out.println("Print every course students are registered for, including repeats.");
        students.stream()
                .flatMap(i->i.getRegistrations().stream())
                .map(i->i.getCourse())
                .forEach(System.out::println);


        // 13. Print a distinct list of courses students are registered for.
        System.out.println("Print a distinct list of courses students are registered for.");
        students.stream()
                .flatMap(i->i.getRegistrations().stream())
                .map(Registration::getCourse)
                .distinct()
                .forEach(System.out::println);

        // 14. Print a distinct list of courses students are registered for, ordered by name.
        System.out.println("Print a distinct list of courses students are registered for, ordered by name.");
        students.stream()
                .flatMap(i->i.getRegistrations().stream())
                .map(i->i.getCourse())
                .distinct()
                .sorted()
                .forEach(System.out::println);
        // 15. Count students per country.
        System.out.println("Count students per country.");
        students.stream()
                .collect(Collectors.groupingBy(Student::getCountry, Collectors.counting()))
                .entrySet()
                .stream()
                .forEach(entry-> System.out.println(entry.getKey()+" "+entry.getValue()));


        // 16. Count students per country. O rder by most to fewest students.
        System.out.println("Count students per country. O rder by most to fewest students.");
        students.stream()
                .collect(Collectors.groupingBy(Student::getCountry,Collectors.counting()))
                .entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String,Long>::getValue).reversed())
                .forEach(entry-> System.out.println(entry.getKey()+" "+entry.getValue()));



        // 17. Count registrations per course.
        System.out.println("Count registrations per course.");
        students.stream()
                .flatMap(i->i.getRegistrations().stream())
                .collect(Collectors.groupingBy(Registration::getCourse,Collectors.counting()))
                .entrySet().stream()
                .forEach(entry-> System.out.println(entry.getKey()+" "+entry.getValue()));

        // 18. How many registrations are not graded (GradeType == AUDIT)?
        System.out.println("How many registrations are not graded (GradeType == AUDIT)?");
        long auditCount = students.stream()
                .flatMap(i->i.getRegistrations().stream())
                .filter(i->i.getGradType()==GradeType.AUDIT)
                .count();
        System.out.println("Audit registrations: "+auditCount);

        // 19. Create a new type, StudentSummary with fields for Country, Major, and IQ.
        //     Map Students to StudentSummary, then sort and limit by IQ (your choice of low or high).
        System.out.println(" Create a new type, StudentSummary with fields for Country, Major, and IQ.");
        students.stream()
                .map(i->new StudentSummary(i.getCountry(),i.getMajor(),i.getIq()))
                .sorted((a,b)->-Double.compare(a.iq,b.iq))
                .limit(10)
                .forEach(System.out::println);

        // 20. What is the average GPA per country (remember, it's random fictional data).
        System.out.println("What is the average GPA per country (remember, it's random fictional data).");
        students.stream()
                .collect(Collectors.groupingBy(Student::getCountry,Collectors.averagingDouble(s->s.getGpa().doubleValue())))
                .entrySet().stream()
                .forEach(entry-> System.out.println(entry.getKey()+" "+entry.getValue()));


        // 21. What is the maximum GPA per country?
        System.out.println("What is the maximum GPA per country?");
        System.out.println();
        students.stream()
                .collect(Collectors.groupingBy(
                        Student::getCountry,
                        Collectors.maxBy(Comparator.comparingDouble(s->s.getGpa().doubleValue()))                ))
                .entrySet().stream()
                .forEach(entry-> System.out.println(entry.getKey()+" "+entry.getValue().orElse(null).getGpa()));
        // 22. Print average IQ per Major ordered by IQ ascending.
        System.out.println("Print average IQ per Major ordered by IQ ascending.");
        students.stream()
                .collect(Collectors.groupingBy(Student::getMajor,Collectors.averagingDouble(s->s.getGpa().doubleValue())))
                .entrySet().stream()
                .sorted(Comparator.comparingDouble(e->e.getValue()))
                .forEach(entry-> System.out.println(entry.getKey()+" "+entry.getValue()));

        // 23. STRETCH GOAL!
        // Who has the highest pointPercent in "Sacred Writing"?
        System.out.println("Who has the highest pointPercent in \"Sacred Writing\"?");
        Student s = new Student();
        students.stream()
                .flatMap(student -> student.getRegistrations().stream()
                        .filter(reg -> reg.getCourse().equalsIgnoreCase("Sacred Writing")))
                .max(Comparator.comparingDouble(Registration::getPointPercent))
                .ifPresent(reg -> System.out.println("Highest pointPercent: " + reg.getPointPercent() + " in course " + reg.getCourse()));




    }
}
