package Entity_task_2;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseClass {
    private Long id;

    public BaseClass() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
