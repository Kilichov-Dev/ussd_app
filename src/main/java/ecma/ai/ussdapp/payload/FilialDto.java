package ecma.ai.ussdapp.payload;

import lombok.Data;

import java.util.List;

@Data

public class FilialDto {
    private String name;
    private String DirecUserName;
    private String DirecFullName;
    private Integer DirecRoleId;
    private Integer filialId;
    private String DirecPosition;
    private String DirecPassword;
    private List<String> StaffUsernames;
}
