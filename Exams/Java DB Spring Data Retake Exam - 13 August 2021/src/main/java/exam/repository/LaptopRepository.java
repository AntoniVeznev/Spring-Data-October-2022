package exam.repository;

import exam.model.entity.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//ToDo:
@Repository
public interface LaptopRepository extends JpaRepository<Laptop, Long> {

    boolean existsByMacAddress(String macAddress);

    @Query("select l from Laptop l order by l.cpuSpeed desc, l.ram desc, l.storage desc, l.macAddress asc ")
    List<Laptop> findLaptopsBySomeStuff();
}
