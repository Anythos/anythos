package net.anythos.user.entity;

public class UserDtoMapper {
    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .active(user.isActive())
                .roles(user.getRoles())
                .build();
    }
}
