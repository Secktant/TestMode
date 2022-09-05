
package data;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class UserInfo {
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_BLOCKED = "blocked";

    String login;
    String password;
    String status;
}