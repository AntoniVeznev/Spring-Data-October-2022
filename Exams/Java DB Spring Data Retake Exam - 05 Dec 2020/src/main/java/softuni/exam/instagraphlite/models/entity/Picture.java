package softuni.exam.instagraphlite.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "pictures")
public class Picture extends BaseEntity {
    private String path;
    private double size;

    List<User> users;
    public Picture() {
    }
    @OneToMany(mappedBy = "profilePicture")
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Column(name = "path", unique = true, nullable = false)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Column(name = "size", nullable = false)
    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
