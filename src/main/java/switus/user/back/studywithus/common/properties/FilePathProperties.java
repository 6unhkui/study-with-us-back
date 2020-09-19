package switus.user.back.studywithus.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
@ConfigurationProperties("file.path")
public class FilePathProperties {
    private String coverImage;
    private String editorImage;
}
