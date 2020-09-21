package switus.user.back.studywithus.common.util;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ImageUtils {

    private static int THUMBNAIL_W = 100;
    private static int THUMBNAIL_H = 100;

    public String getBase64String(BufferedImage src, String imageType) {
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


    public BufferedImage makeThumbnail(BufferedImage src) throws IOException {
        BufferedImage dest;
        // 가로와 세로중
        int side = Math.min(src.getHeight(), src.getWidth());

        // 정사각형으로 크롭
        dest = Scalr.crop(src, (src.getWidth() - side) / 2, (src.getHeight() - side) / 2, side, side, (BufferedImageOp) null);

        // 리사이징
        dest = Scalr.resize(dest, THUMBNAIL_W, THUMBNAIL_H, (BufferedImageOp) null);
        return dest;
    }


    public String getBase64Thumbnail(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String fileExtension = FilenameUtils.getExtension(filename);

        BufferedImage read = ImageIO.read(file.getInputStream());
        BufferedImage image = makeThumbnail(read);

        return getBase64String(image, fileExtension);
    }


    public static String extractImageFromHtml(String html) {
        Pattern pattern = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
        Matcher match = pattern.matcher(html);
        String imgTag = null;
        if(match.find()) {
            imgTag = match.group(1);
        }

        return imgTag;
    }

}
