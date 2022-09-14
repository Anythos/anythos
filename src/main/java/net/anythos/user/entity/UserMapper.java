package net.anythos.user.entity;

public class UserMapper {
    public static User mapToUser(UserDto userDto) {
        return User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .roles(userDto.getRoles())
                .active(userDto.isActive())
                .build();
    }
}
