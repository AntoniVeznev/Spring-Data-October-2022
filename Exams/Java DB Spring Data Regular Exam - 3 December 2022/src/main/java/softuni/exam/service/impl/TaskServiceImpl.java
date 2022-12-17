package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TasksSeedRootDtoXML;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.models.entity.Part;
import softuni.exam.models.entity.Task;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.repository.PartRepository;
import softuni.exam.repository.TaskRepository;
import softuni.exam.service.TaskService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TaskServiceImpl implements TaskService {
    private final static String TASK_PATH = "src/main/resources/files/xml/tasks.xml";
    private TaskRepository taskRepository;
    private MechanicRepository mechanicRepository;
    private CarRepository carRepository;
    private PartRepository partRepository;
    private ValidationUtil validationUtil;
    private ModelMapper modelMapper;
    private XmlParser xmlParser;

    public TaskServiceImpl(TaskRepository taskRepository, MechanicRepository mechanicRepository, CarRepository carRepository, PartRepository partRepository, ValidationUtil validationUtil, ModelMapper modelMapper, XmlParser xmlParser) {
        this.taskRepository = taskRepository;
        this.mechanicRepository = mechanicRepository;
        this.carRepository = carRepository;
        this.partRepository = partRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return taskRepository.count() > 0;
    }

    @Override
    public String readTasksFileContent() throws IOException {
        return Files.readString(Path.of(TASK_PATH));
    }

    @Override
    public String importTasks() throws IOException, JAXBException {
        StringBuilder builder = new StringBuilder();
        xmlParser
                .fromFile(TASK_PATH,
                        TasksSeedRootDtoXML.class)
                .getTasks()
                .stream()
                .filter(taskDtoXML -> {
                    boolean isValid = validationUtil.isValid(taskDtoXML)
                            && ifMechanicNameExist(taskDtoXML.getMechanic().getFirstName());
                    if (isValid) {
                        builder.append(String.format("Successfully imported task %.2f%n",
                                taskDtoXML.getPrice()));
                    } else {
                        builder.append("Invalid task").append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(taskDtoXML -> {
                    Task task = modelMapper.map(taskDtoXML, Task.class);
                    task.setCar(findCarById(taskDtoXML.getCar().getId()));
                    task.setMechanic(findMechByFname(taskDtoXML.getMechanic().getFirstName()));
                    task.setPart(findPartById(taskDtoXML.getPart().getId()));
                    return task;
                })
                .forEach(taskRepository::save);


        return builder.toString();
    }

    private Part findPartById(Long id) {
        return partRepository.findPartById(id);
    }

    private Mechanic findMechByFname(String firstName) {
        return mechanicRepository.findMechanicByFirstName(firstName);
    }

    private Car findCarById(Long id) {
        return carRepository.findCarById(id);
    }

    private boolean ifMechanicNameExist(String firstName) {
        return mechanicRepository.existsMechanicByFirstName(firstName);
    }

    @Override
    public String getCoupeCarTasksOrderByPrice() {
        StringBuilder builder = new StringBuilder();
        taskRepository.plsWork()
                .forEach(task -> builder.append(String.format("Car %s %s with %dkm\n" +
                                "-Mechanic: %s %s - task №%d:\u00AD\u00AD\n" +
                                "--Engine: %.1f\n" +
                                "---Price: %.2f$%n",
                        task.getCar().getCarMake(),
                        task.getCar().getCarModel(),
                        task.getCar().getKilometers(),
                        task.getMechanic().getFirstName(),
                        task.getMechanic().getLastName(),
                        task.getId(),
                        task.getCar().getEngine(),
                        task.getPrice())));
        return builder.toString();
    }
}
