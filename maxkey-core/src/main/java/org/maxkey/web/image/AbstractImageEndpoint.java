package org.maxkey.web.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.maxkey.configuration.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


/**
 * AbstractImageEndpoint  Producer Image .
 * @author Crystal.Sea
 *
 */

public class AbstractImageEndpoint {
    private static final Logger _logger = LoggerFactory.getLogger(AbstractImageEndpoint.class);

    @Autowired
    @Qualifier("applicationConfig")
    ApplicationConfig applicationConfig;

    /**
     * producerImage.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param bufferedImage BufferedImage
     * @throws IOException error
     */
    public static void producerImage(HttpServletRequest request, 
                              HttpServletResponse response,
                              BufferedImage bufferedImage) throws IOException {
        // Set to expire far in the past.
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg/gif
        response.setContentType("image/gif");
        _logger.trace("create the image");
        // create the image
        if (bufferedImage != null) {
            ServletOutputStream out = response.getOutputStream();
            // write the data out
            ImageIO.write(bufferedImage, "gif", out);
            try {
                out.flush();
            } finally {
                out.close();
            }
        }
    }
    
    /**
     * byte2BufferedImage.
     * @param imageByte bytes
     * @return
     */
    public static BufferedImage byte2BufferedImage(byte[] imageByte) {
        try {
            InputStream in = new ByteArrayInputStream(imageByte);
            BufferedImage bufferedImage = ImageIO.read(in);
            return bufferedImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * bufferedImage2Byte.
     * @param bufferedImage  BufferedImage
     * @return
     */
    public static byte[] bufferedImage2Byte(BufferedImage bufferedImage) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "gif", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

}
