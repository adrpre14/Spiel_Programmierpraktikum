import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Tile {
    private BoundingBox boundingBox;
    private int imageIndex = 0;
    private final BufferedImage[] tileImages;

    {
        try {
            tileImages = new BufferedImage[] {
                    ImageIO.read(new File("assets/Tiles/liquidWaterTop_mid.png")),
                    ImageIO.read(new File("assets/Tiles/liquidWater.png")),
                    ImageIO.read(new File("assets/Tiles/grassMid.png")),
                    ImageIO.read(new File("assets/Tiles/grassHalf.png")),
                    ImageIO.read(new File("assets/Tiles/grassCenter.png")),
                    ImageIO.read(new File("assets/Tiles/liquidLavaTop_mid.png")),
                    ImageIO.read(new File("assets/Tiles/sandMid.png")),
                    ImageIO.read(new File("assets/Tiles/sandCenter.png")),
                    ImageIO.read(new File("assets/Tiles/sandHalf.png")),
                    ImageIO.read(new File("assets/Tiles/stoneMid.png")),
                    ImageIO.read(new File("assets/Tiles/stoneCenter.png")),
                    ImageIO.read(new File("assets/Tiles/stoneHalf.png")),
                    ImageIO.read(new File("assets/Tiles/door_closedMid.png")),
                    ImageIO.read(new File("assets/Tiles/door_closedTop.png")),
                    ImageIO.read(new File("assets/Tiles/signExit.png")),
                    ImageIO.read(new File("assets/Tiles/snowMid.png")),
                    ImageIO.read(new File("assets/Tiles/snowCenter.png")),
                    ImageIO.read(new File("assets/Tiles/door_openMid.png")),
                    ImageIO.read(new File("assets/Tiles/door_openTop.png")),
                    ImageIO.read(new File("assets/Items/coinGold.png")),
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Tile(int x, int y, int width, int height, int imageIndex) {
        boundingBox = new BoundingBox(new Vec2(x, y), new Vec2(x + width, y + height));
        this.imageIndex = imageIndex;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public BufferedImage getImage() {
        return tileImages[imageIndex];
    }

    public int getImageIndex() {
        return imageIndex;
    }
}
