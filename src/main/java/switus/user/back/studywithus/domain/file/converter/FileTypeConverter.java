package switus.user.back.studywithus.domain.file.converter;

import switus.user.back.studywithus.domain.common.AbstractBaseEnumConverter;
import switus.user.back.studywithus.domain.file.FileType;

import javax.persistence.Converter;

@Converter
public class FileTypeConverter extends AbstractBaseEnumConverter<FileType, Integer> {
    @Override
    protected FileType[] getValueList() {
        return FileType.values();
    }
}
