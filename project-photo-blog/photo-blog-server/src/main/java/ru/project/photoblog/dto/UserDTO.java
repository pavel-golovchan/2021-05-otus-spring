package ru.project.photoblog.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserDTO {
    private Long id;
    @NotEmpty
    private String firstName;//name
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String userName;
}
