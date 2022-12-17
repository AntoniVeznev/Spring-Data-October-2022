package softuni.exam.instagraphlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import softuni.exam.instagraphlite.models.entity.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


    @Query("select p from Post p where p.user.id = p.id order by p.picture.size asc")
    List<Post> testPleaseWork();

}
