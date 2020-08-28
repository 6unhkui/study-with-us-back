package switus.user.back.studywithus.common.util;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Component
public class ImageUtils {
    private static int THUMBNAIL_W = 160;
    private static int THUMBNAIL_H = 160;

    public static String getBase64String(BufferedImage image, String type) {
        StringBuilder encodeString = new StringBuilder();
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();
            encodeString.append("data:image/png;base64,");
            encodeString.append(Base64.getEncoder().encodeToString(imageBytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodeString.toString();
    }


    public static BufferedImage makeThumbnail(BufferedImage src) throws IOException {
        BufferedImage dest;
        // 가로와 세로중
        int side = Math.min(src.getHeight(), src.getWidth());

        // 정사각형으로 크롭
        dest = Scalr.crop(src, (src.getWidth() - side) / 2, (src.getHeight() - side) / 2, side, side, null);

        // 리사이징
        dest = Scalr.resize(dest, THUMBNAIL_W, THUMBNAIL_H, null);
        return dest;
    }

}
