package switus.user.back.studywithus.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
public class AppAccountProperties {
    @Value("${app.account.user.email}")
    private String userEmail;

    @Value("${app.account.user.password}")
    private String userPassword;

    @Value("${app.account.admin.email}")
    private String adminEmail;

    @Value("${app.account.admin.password}")
    private String adminPassword;
}
