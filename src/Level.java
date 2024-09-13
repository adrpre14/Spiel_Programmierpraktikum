import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Level {

    BufferedImage levelImg;
    BufferedImage outputImg;

    ArrayList<Tile> tiles = new ArrayList<>();
    ArrayList<Tile> dynamicTile = new ArrayList<>();

    public Level(String levelPath) throws IOException {
        levelImg = ImageIO.read(new File(levelPath));
        outputImg = new BufferedImage(levelImg.getWidth() * 70, levelImg.getHeight() * 70, BufferedImage.TYPE_INT_RGB);

        // Generate the output image from the level image
        generateLevel();
    }

    //create output image from level image by mapping colors to tiles
    public void generateLevel() throws IOException {
        BufferedImage backgroundImg = ImageIO.read(new File("assets/background0.png"));
        Graphics2D g2d = outputImg.createGraphics();
        g2d.drawImage(backgroundImg, 0, 0, outputImg.getWidth(), outputImg.getHeight(), null);
        for (int y = 0; y < levelImg.getHeight(); y++) {
            for (int x = 0; x < levelImg.getWidth(); x++) {
                Color pixelColor = new Color(levelImg.getRGB(x, y));

                Tile tile = null;
                int imageIndex = getImageIndex(pixelColor);

                if (imageIndex != -1) {
                    if (imageIndex == 19) {
                        tile = new Tile(x * 70, y * 70, 70, 70, imageIndex);
                        tiles.add(tile);
                        dynamicTile.add(tile);
                    } else {
                        tile = new Tile(x * 70, y * 70, 70, 70, imageIndex);
                        tiles.add(tile);
                        outputImg.getGraphics().drawImage(tile.getImage(), x * 70, y * 70, null);
                    }
                }
            }
        }
    }

    private int getImageIndex(Color pixelColor) {
        // Add logic to map pixel colors to image indices
        if (pixelColor.equals(Color.BLUE)) {
            return 0; // water
        } else if (pixelColor.equals(new Color(0, 0, 230))) {
            return 1; // water center
        } else if (pixelColor.equals(Color.BLACK)) {
            return 2; // grass
        } else if (pixelColor.equals(new Color(0, 0, 10))) {
            return 3; // grass half
        } else if (pixelColor.equals(new Color(255, 128, 0))) {
            return 4; // grass center
        } else if (pixelColor.equals(Color.RED)) {
            return 5; // lava
        } else if (pixelColor.equals(Color.YELLOW)) {
            return 6; // sand top
        } else if (pixelColor.equals(new Color(255, 255, 102))) {
            return 7; // sand center
        } else if (pixelColor.equals(new Color(255, 255, 10))) {
            return 8; // sand half
        } else if (pixelColor.equals(new Color(255, 0, 255))) {
            return 9; // stone top
        } else if (pixelColor.equals(new Color(255, 0, 230))) {
            return 10; // stone center
        } else if (pixelColor.equals(new Color(255, 10, 255))) {
            return 11; // stone half
        } else if (pixelColor.equals(new Color(102, 77, 0))) {
            return 12; // door bottom
        } else if (pixelColor.equals(new Color(128, 102, 0))) {
            return 13; // door top
        } else if (pixelColor.equals(new Color(255, 51, 0))) {
            return 14; // exit sign
        } else if (pixelColor.equals(new Color(0, 255, 255))) {
            return 15; // snow
        } else if (pixelColor.equals(new Color(0, 240, 255))) {
            return 16; // snow center
        } else if (pixelColor.equals(new Color(26, 26, 0))) {
            return 17; // door opened bottom
        } else if (pixelColor.equals(new Color(26, 13, 0))) {
            return 18; // door opened top
        } else if (pixelColor.equals(new Color(21, 10, 200))) {
            return 19; // Coin

        } else {
            return -1;
        }
    }

    public BufferedImage getImage() {
        return outputImg;
    }
}
