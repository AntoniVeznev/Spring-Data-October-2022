package Entity_task_4;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "visitations")
public class Visitation extends BaseClass {
    private Date date;
    private String comments;

    public Visitation() {
    }


    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "comments", unique = true, columnDefinition = "TEXT", length = 10000)
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
