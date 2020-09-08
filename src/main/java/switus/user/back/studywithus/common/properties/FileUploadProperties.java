package switus.user.back.studywithus.common.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties
public class FileUploadProperties {

    @Value("${file.type.cover-image}")
    private String coverImagePath;

}
