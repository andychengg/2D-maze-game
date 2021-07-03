package dev.project276.display;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Class that contains image utility functions.
 * */
public class ImageUtils {

    /**
     * Creates a BufferedImage object of the file at the given filePath, and returns it.
     * Raises an IOException if the image is not found.
     * */
    public static BufferedImage loadImage(String filePath) {
        try {
            return ImageIO.read(ImageUtils.class.getResource(filePath));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Flips the image parameter horizontally.
     * @param image The image to flip.
     * @return A BufferedImage containing the flipped image.
     */
    public static BufferedImage flipHorizontal(BufferedImage image){
        // Flip the image horizontally
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);
        return image;
    }

    /**
     * Makes a new image with its alpha multiplied by newAlpha.
     * @param image The BufferedImage to change the alpha of.
     * @param newAlpha The float to multiply the image's alpha by.
     * @return A BufferedImage with the same image of image, and alpha multiplied by newAlpha.
     */
    public static BufferedImage makeTranslucent(BufferedImage image, float newAlpha){
        BufferedImage translucentImage = new BufferedImage(image.getWidth(), image.getHeight(),
                Transparency.TRANSLUCENT);
        Graphics2D graphics2D = translucentImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, newAlpha));
        graphics2D.drawImage(image, 0, 0, null);
        return translucentImage;
    }
}
