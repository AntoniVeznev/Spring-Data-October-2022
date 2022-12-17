package com.example.exercise_6_springdataautomappingobjects.config;

import com.example.exercise_6_springdataautomappingobjects.model.dto.GameAddDto;
import com.example.exercise_6_springdataautomappingobjects.model.entity.Game;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationBeanConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper
                .typeMap(GameAddDto.class, Game.class)
                .addMappings(mapper ->
                        mapper.map(GameAddDto::getThumbnailURL,
                                        Game::setImageThumbnail));
        Converter<String, LocalDate> localDateConverter = new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(MappingContext<String, LocalDate> context) {
                return LocalDate.parse(context.getSource(),
                        DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            }
        };
        modelMapper.addConverter(localDateConverter);
        return modelMapper;
    }
}