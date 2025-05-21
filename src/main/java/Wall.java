import java.awt.Color;

public class Wall extends GameObject {
    public Wall(int x, int y) {
        super(x, y, Constants.WALL_IMAGE, Color.GRAY);
    }
}