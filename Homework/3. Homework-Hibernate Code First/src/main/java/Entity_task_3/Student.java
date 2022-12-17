package Entity_task_3;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student extends User {
    private Float averageGrade;
    private Integer attendance;
    private Set<Course> courses;

    public Student() {
    }

    @ManyToMany(mappedBy = "students")
    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    @Column(name = "average_grade")
    public Float getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(Float averageGrade) {
        this.averageGrade = averageGrade;
    }

    @Column(name = "attendance")
    public Integer getAttendance() {
        return attendance;
    }

    public void setAttendance(Integer attendance) {
        this.attendance = attendance;
    }
}
