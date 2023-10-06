package com.shinhan.walfi.dto.banking;

import com.shinhan.walfi.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupReqDto {

    private String userId;

    private String password;

    private String name;

    public User dtoToEntity() {
        User user = new User();
        user.setUserId(this.userId);
        user.setPassword(this.password);
        user.setName(this.name);
        return user;
    }
}
