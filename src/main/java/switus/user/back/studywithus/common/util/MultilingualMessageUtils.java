package switus.user.back.studywithus.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MultilingualMessageUtils {

    private final MessageSource messageSource;

    public String makeMultilingualMessage(String message, Object[] args) {
        return messageSource.getMessage(message, args, LocaleContextHolder.getLocale());
    }

    public String makeMultilingualMessage(String message) {
        return messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
    }


}
