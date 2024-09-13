import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

enum TileType {
    WATER,
    WATER_CENTER,
    GRASS,
    GRASS_HALF,
    GRASS_CENTER,
    LAVA,
    SAND_TOP,
    SAND_CENTER,
    SAND_HALF,
    STONE_TOP,
    STONE_CENTER,
    STONE_HALF,
    DOOR_BOTTOM,
    DOOR_TOP,
    EXIT_SIGN,
    SNOW,
    SNOW_CENTER,
    DOOR_OPENED_BOTTOM,
    DOOR_OPENED_TOP
}

public class Level {

    BufferedImage levelImg;
    BufferedImage outputImg;

    private ArrayList<Tile> tiles = new ArrayList<>();
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
            return TileType.WATER.ordinal(); // water
        } else if (pixelColor.equals(new Color(0, 0, 230))) {
            return TileType.WATER_CENTER.ordinal(); // water center
        } else if (pixelColor.equals(Color.BLACK)) {
            return TileType.GRASS.ordinal(); // grass
        } else if (pixelColor.equals(new Color(0, 0, 10))) {
            return TileType.GRASS_HALF.ordinal(); // grass half
        } else if (pixelColor.equals(new Color(255, 128, 0))) {
            return TileType.GRASS_CENTER.ordinal(); // grass center
        } else if (pixelColor.equals(Color.RED)) {
            return TileType.LAVA.ordinal(); // lava
        } else if (pixelColor.equals(Color.YELLOW)) {
            return TileType.SAND_TOP.ordinal(); // sand top
        } else if (pixelColor.equals(new Color(255, 255, 102))) {
            return TileType.SAND_CENTER.ordinal(); // sand center
        } else if (pixelColor.equals(new Color(255, 255, 10))) {
            return TileType.SAND_HALF.ordinal(); // sand half
        } else if (pixelColor.equals(new Color(255, 0, 255))) {
            return TileType.STONE_TOP.ordinal(); // stone top
        } else if (pixelColor.equals(new Color(255, 0, 230))) {
            return TileType.STONE_CENTER.ordinal(); // stone center
        } else if (pixelColor.equals(new Color(255, 10, 255))) {
            return TileType.STONE_HALF.ordinal(); // stone half
        } else if (pixelColor.equals(new Color(102, 77, 0))) {
            return TileType.DOOR_BOTTOM.ordinal(); // door bottom
        } else if (pixelColor.equals(new Color(128, 102, 0))) {
            return TileType.DOOR_TOP.ordinal(); // door top
        } else if (pixelColor.equals(new Color(255, 51, 0))) {
            return TileType.EXIT_SIGN.ordinal(); // exit sign
        } else if (pixelColor.equals(new Color(0, 255, 255))) {
            return TileType.SNOW.ordinal(); // snow
        } else if (pixelColor.equals(new Color(0, 240, 255))) {
            return TileType.SNOW_CENTER.ordinal(); // snow center
        } else if (pixelColor.equals(new Color(26, 26, 0))) {
            return TileType.DOOR_OPENED_BOTTOM.ordinal(); // door opened bottom
        } else if (pixelColor.equals(new Color(26, 13, 0))) {
            return TileType.DOOR_OPENED_TOP.ordinal(); // door opened top
        } else if (pixelColor.equals(new Color(21, 10, 200))) {
            return 19; // Coin
        } else {
            return -1;
        }
    }

    public BufferedImage getImage() {
        return outputImg;
    }

    public ArrayList<Tile> getVisibleTiles(int posX) {
        return tiles.stream().filter(tile -> {
            int tileX = (int) tile.getBoundingBox().min.x;
            int tileWidth = (int) (tile.getBoundingBox().max.x - tile.getBoundingBox().min.x);
            return tileX >= posX - tileWidth && tileX <= posX + 1000;
        }).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
