package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.UserDtoJSON;
import softuni.exam.instagraphlite.models.entity.Picture;
import softuni.exam.instagraphlite.models.entity.Post;
import softuni.exam.instagraphlite.models.entity.User;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


@Service
public class UserServiceImpl implements UserService {
    private static final String USER_PATH_FILE = "src/main/resources/files/users.json";
    private UserRepository userRepository;
    private PictureRepository pictureRepository;
    private PostRepository postRepository;
    private ValidationUtil validationUtil;
    private ModelMapper modelMapper;
    private Gson gson;

    public UserServiceImpl(UserRepository userRepository, PictureRepository pictureRepository, PostRepository postRepository, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson) {
        this.userRepository = userRepository;
        this.pictureRepository = pictureRepository;
        this.postRepository = postRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return userRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(USER_PATH_FILE));
    }

    @Override
    public String importUsers() throws IOException {
        StringBuilder builder = new StringBuilder();
        UserDtoJSON[] userDtoJSONS = gson.fromJson(readFromFileContent(), UserDtoJSON[].class);

        Arrays.stream(userDtoJSONS)
                .filter(userDtoJSON -> {
                    boolean isValid = validationUtil.isValid(userDtoJSON)
                            && ifPictureExists(userDtoJSON.getProfilePicture());
                    if (isValid) {
                        builder
                                .append(String.format("Successfully imported User: %s", userDtoJSON.getUsername()))
                                .append(System.lineSeparator());
                    } else {
                        builder
                                .append("Invalid User").append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(userDtoJSON -> {
                    User user = modelMapper.map(userDtoJSON, User.class);
                    user.setProfilePicture(findPictureWithPath(userDtoJSON.getProfilePicture()));
                    return user;
                })
                .forEach(userRepository::save);
        return builder.toString();
    }

    private Picture findPictureWithPath(String profilePicture) {
        return pictureRepository.findPictureByPath(profilePicture);
    }

    private boolean ifPictureExists(String path) {
        return pictureRepository.existsByPath(path);
    }


    @Override
    public String exportUsersWithTheirPosts() {
        StringBuilder builder = new StringBuilder();
        List<User> users = userRepository.letsTestIt();
        for (User user : users) {
            builder
                    .append(String.format("User: %s\n" +
                                    "Post count: %d%n",
                            user.getUsername(),
                            user.getPosts().size()));
                user.setPosts(postRepository.testPleaseWork());
            Stream<Post> sorted = user
                    .getPosts()
                    .stream()
                    .sorted((x, y) -> {
                        if (Double.compare(x.getPicture().getSize(), y.getPicture().getSize()) == 0) {
                            return 0;
                        }
                        if (Double.compare(x.getPicture().getSize(), y.getPicture().getSize()) == 1) {
                            return 1;
                        }
                        if (Double.compare(x.getPicture().getSize(), y.getPicture().getSize()) == -1) {
                            return -1;
                        }
                        return Double.compare(x.getPicture().getSize(), y.getPicture().getSize());
                    });
            sorted.forEach(post -> {
                builder.append(String.format("==Post Details:\n" +
                                "----Caption: %s\n" +
                                "----Picture Size: %.2f%n",
                        post.getCaption(),
                        post.getPicture().getSize()));
            });
        }
        return builder.toString();
    }
}