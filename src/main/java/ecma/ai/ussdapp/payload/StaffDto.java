package ecma.ai.ussdapp.payload;

import lombok.Data;

@Data
public class StaffDto {
    private String username;

    private String fullName;

    private Integer roleId;

    private Integer filialId;

    private String position;

    private String password;
}
