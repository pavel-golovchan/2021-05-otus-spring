package ru.project.photoblog.facade;

import ru.project.photoblog.dto.UserDTO;
import ru.project.photoblog.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    public UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUserName(user.getUsername());
        return userDTO;
    }

}