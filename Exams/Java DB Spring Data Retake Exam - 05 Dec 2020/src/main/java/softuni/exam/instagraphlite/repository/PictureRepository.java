package softuni.exam.instagraphlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import softuni.exam.instagraphlite.models.entity.Picture;

import java.util.List;
import java.util.Set;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {

    boolean existsByPath(String path);


    boolean existsPictureByPath(String path);



    List<Picture> findAllBySizeGreaterThanOrderBySizeAsc(double size);


    Picture findPictureByPath(String path);




}
