package com.example.xmlprocessing.survice;


import com.example.xmlprocessing.model.dto.UserSeedDto;
import com.example.xmlprocessing.model.dto.UsersViewRootDto;
import com.example.xmlprocessing.model.entity.User;

import java.util.List;

public interface UserService {
   Long getCount();

    void seedUsers(List<UserSeedDto> users);

    User getRandomUser();

    UsersViewRootDto findUsersWithMoreThenOneSoldProduct();

}
