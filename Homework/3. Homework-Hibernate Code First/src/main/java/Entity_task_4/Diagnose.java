package Entity_task_4;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Table;


@Entity
@Table(name = "diagnoses")
public class Diagnose extends BaseClass {
    private String name;
    private String comments;


    public Diagnose() {
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "comments", unique = true, columnDefinition = "TEXT", length = 10000)
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
