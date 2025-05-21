import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

public abstract class GameObject {
    protected int x, y;
    protected BufferedImage image;
    protected Color fallbackColor;

    public GameObject(int x, int y, String imagePath, Color fallbackColor) {
        this.x = x;
        this.y = y;
        this.fallbackColor = fallbackColor;
        try {
            if (imagePath != null) {
                InputStream is = getClass().getResourceAsStream(imagePath);
                if (is != null) {
                    image = ImageIO.read(is);
                } else {
                    System.err.println("Image not found: " + imagePath);
                    image = null; // Use fallback color
                }
            } else {
                image = null; // No image path provided
            }
        } catch (IOException e) {
            System.err.println("Failed to load image: " + imagePath + " - " + e.getMessage());
            image = null; // Use fallback color
        }
    }

    public void draw(Graphics2D g2) {
        if (image != null) {
            g2.drawImage(image, x, y, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
        } else {
            g2.setColor(fallbackColor);
            g2.fillRect(x, y, Constants.TILE_SIZE, Constants.TILE_SIZE);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}