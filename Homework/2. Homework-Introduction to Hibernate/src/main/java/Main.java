import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory amf =
                Persistence.createEntityManagerFactory("softuni");

        EntityManager entityManager = amf.createEntityManager();

        Engine engine = new Engine(entityManager);

        engine.run();

    }
}
