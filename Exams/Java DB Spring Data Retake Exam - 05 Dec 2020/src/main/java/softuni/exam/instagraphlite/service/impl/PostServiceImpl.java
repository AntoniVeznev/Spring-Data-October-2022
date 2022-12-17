package softuni.exam.instagraphlite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import softuni.exam.instagraphlite.models.dto.PostRootDtoXML;
import softuni.exam.instagraphlite.models.entity.Picture;
import softuni.exam.instagraphlite.models.entity.Post;
import softuni.exam.instagraphlite.models.entity.User;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.PostService;
import softuni.exam.instagraphlite.util.ValidationUtil;
import softuni.exam.instagraphlite.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PostServiceImpl implements PostService {
    private static final String POST_PATH_FILE = "src/main/resources/files/posts.xml";
    private PostRepository postRepository;
    private UserRepository userRepository;
    private PictureRepository pictureRepository;
    private ValidationUtil validationUtil;
    private ModelMapper modelMapper;
    private XmlParser xmlParser;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, PictureRepository pictureRepository, ValidationUtil validationUtil, ModelMapper modelMapper, XmlParser xmlParser) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.pictureRepository = pictureRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return postRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(POST_PATH_FILE));
    }

    @Override
    public String importPosts() throws IOException, JAXBException {
        StringBuilder builder = new StringBuilder();
        xmlParser
                .fromFile(POST_PATH_FILE, PostRootDtoXML.class)
                .getPosts()
                .stream()
                .filter(postDtoXML -> {
                    boolean isValid = validationUtil.isValid(postDtoXML)
                            && ifPictureIsNotHere(postDtoXML.getPicture().getPath())
                            && ifUserIsNotHere(postDtoXML.getUser().getUsername());

                    if (isValid) {
                        builder
                                .append(String.format("Successfully imported Post, made by %s", postDtoXML.getUser().getUsername()))
                                .append(System.lineSeparator());
                    } else {
                        builder.append("Invalid Post").append(System.lineSeparator());
                    }

                    return isValid;
                }).map(postDtoXML -> {
                    Post post = modelMapper.map(postDtoXML, Post.class);
                    post.setPicture(findItByPath(postDtoXML.getPicture().getPath()));
                    post.setUser(findItByUsername(postDtoXML.getUser().getUsername()));
                    return post;
                })
                .forEach(postRepository::save);


        return builder.toString();
    }

    private Picture findItByPath(String path) {
        return pictureRepository.findPictureByPath(path);
    }

    private User findItByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }


    private boolean ifUserIsNotHere(String username) {
        return userRepository.existsByUsername(username);
    }

    private boolean ifPictureIsNotHere(String path) {
        return pictureRepository.existsPictureByPath(path);
    }
}
