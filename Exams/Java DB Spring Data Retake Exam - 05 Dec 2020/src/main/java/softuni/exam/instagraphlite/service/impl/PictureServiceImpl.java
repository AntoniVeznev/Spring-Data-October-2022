package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.PictureDtoJSON;
import softuni.exam.instagraphlite.models.entity.Picture;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;


@Service
public class PictureServiceImpl implements PictureService {
    private static final String PICTURE_PATH_FILE = "src/main/resources/files/pictures.json";
    private PictureRepository pictureRepository;
    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;
    private Gson gson;

    public PictureServiceImpl(PictureRepository pictureRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return pictureRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(PICTURE_PATH_FILE));
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder builder = new StringBuilder();
        PictureDtoJSON[] pictureDtoJSONS = gson.fromJson(readFromFileContent(), PictureDtoJSON[].class);
        Arrays.stream(pictureDtoJSONS)
                .filter(pictureDtoJSON -> {
                    boolean isValid = validationUtil.isValid(pictureDtoJSON)
                            && !isEntityExists(pictureDtoJSON.getPath());
                    if (isValid) {
                        builder
                                .append(String.format("Successfully imported Picture, with size %.2f",
                                        pictureDtoJSON.getSize())).append(System.lineSeparator());
                    } else {
                        builder.append("Invalid Picture").append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(pictureDtoJSON -> modelMapper.map(pictureDtoJSON, Picture.class))
                .forEach(pictureRepository::save);
        return builder.toString();
    }

    private boolean isEntityExists(String path) {
        return pictureRepository.existsByPath(path);
    }

    @Override
    public String exportPictures() {
        StringBuilder builder = new StringBuilder();
        List<Picture> allBySizeGreaterThan =
                pictureRepository.findAllBySizeGreaterThanOrderBySizeAsc(30000);
        for (Picture picture : allBySizeGreaterThan) {
            builder.append(String.format("%.2f – %s",
                            picture.getSize(),
                            picture.getPath()))
                    .append(System.lineSeparator());
        }
        return builder.toString();
    }
}
