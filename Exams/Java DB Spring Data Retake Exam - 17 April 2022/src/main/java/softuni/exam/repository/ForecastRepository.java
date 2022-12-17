package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.DayOfWeekEnums;
import softuni.exam.models.entity.Forecast;

import java.util.List;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {


    boolean existsById(Long id);

    boolean existsByDayOfWeek(DayOfWeekEnums dayOfWeek);

    boolean existsByIdAndDayOfWeek(Long id, DayOfWeekEnums dayOfWeek);

    @Query("SELECT  f from Forecast f  where f.dayOfWeek = 'SUNDAY' and f.city.population < 150000 " +
            "order by f.maxTemperature desc, f.id asc")
        List<Forecast> test();
}
