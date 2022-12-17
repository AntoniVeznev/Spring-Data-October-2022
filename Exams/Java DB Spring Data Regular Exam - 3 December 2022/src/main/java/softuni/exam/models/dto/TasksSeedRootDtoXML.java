package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "tasks")
@XmlAccessorType(XmlAccessType.FIELD)
public class TasksSeedRootDtoXML {
    @XmlElement(name = "task")
    private List<TaskDtoXML> tasks;

    public TasksSeedRootDtoXML() {
    }

    public List<TaskDtoXML> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDtoXML> tasks) {
        this.tasks = tasks;
    }
}
