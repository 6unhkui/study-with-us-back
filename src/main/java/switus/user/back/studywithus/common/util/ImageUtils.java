package switus.user.back.studywithus.common.util;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import switus.user.back.studywithus.common.error.exception.BadRequestException;
import switus.user.back.studywithus.common.error.exception.InternalServerException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ImageUtils {

    private final MultilingualMessageUtils message;
    private final static int THUMBNAIL_W = 100;
    private final static int THUMBNAIL_H = 100;

    public String base64Encoding(BufferedImage src, String imageType) {
        StringBuilder encodeString = new StringBuilder();
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ImageIO.write(src, imageType, bos);
            byte[] imageBytes = bos.toByteArray();
            encodeString.append("data:image/png;base64,");
            encodeString.append(Base64.getEncoder().encodeToString(imageBytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodeString.toString();
    }


    public BufferedImage makeThumbnail(BufferedImage src, int width, int height) throws IOException {
        BufferedImage dest;
        // 가로와 세로중 최소값
        int side = Math.min(src.getHeight(), src.getWidth());

        // 정사각형으로 크롭
        dest = Scalr.crop(src, (src.getWidth() - side) / 2, (src.getHeight() - side) / 2, side, side, (BufferedImageOp) null);

        // 리사이징
        dest = Scalr.resize(dest, width, height, (BufferedImageOp) null);
        return dest;
    }


    public String getThumbnailAsBase64(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String fileExtension = FilenameUtils.getExtension(filename);

        try {
            BufferedImage read = ImageIO.read(file.getInputStream());
            BufferedImage image = makeThumbnail(read, THUMBNAIL_W, THUMBNAIL_H);
            return base64Encoding(image, fileExtension);
        } catch (IOException e) {
            throw new InternalServerException(message.makeMultilingualMessage("file.uploadError"));
        }
    }


    public static String extractImageFromHtml(String html) {
        Pattern pattern = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
        Matcher match = pattern.matcher(html);

        if(match.find()) {
            return match.group(1);
        }
        return "";
    }


    public void verifyImageFile(MultipartFile file) {
        if (file.getContentType() != null && !file.getContentType().startsWith("image/")) {
            throw new BadRequestException("Invalid content-type");
        }
    }
}
