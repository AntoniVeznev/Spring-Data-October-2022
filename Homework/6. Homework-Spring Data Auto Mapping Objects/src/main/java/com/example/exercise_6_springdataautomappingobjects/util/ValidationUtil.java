package com.example.exercise_6_springdataautomappingobjects.util;

import javax.validation.ConstraintViolation;
import java.util.Set;

public interface ValidationUtil {
    <E> boolean isValid(E entity);

    <E> Set<ConstraintViolation<E>> getViolations(E entity);
}