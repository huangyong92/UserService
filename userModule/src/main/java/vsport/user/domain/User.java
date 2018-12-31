package vsport.user.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = -2491518179662281133L;
    private String userId;

    private String phone;

    private String password;

    private String nickName;

    private String avatar;

    private Integer gender;

    private String livingCity;

    private String sportTarget;
}
