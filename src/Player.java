import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    BufferedImage[] walkImg;
    BufferedImage currentImg;
    int currentFrame = 0;

    //Player position and speed
    int x, y;
    int width, height;
    int speedX = 20;
    int speedY = 0; // for jumping

    boolean walkLeft = false;
    boolean walkRight = false;
    boolean jump = false;

    //Constants for gravity and jumping
    final int GRAVITY = 1;
    final int JUMP_POWER = -15;

    private BoundingBox boundingBox;

    public Player(int startX, int StartY) {
        x = startX;
        y = StartY;

        try {
            walkImg = new BufferedImage[]{
                    ImageIO.read(new File("assets/Player/p1_walk/PNG/p1_walk01.png")),
                    ImageIO.read(new File("assets/Player/p1_walk/PNG/p1_walk02.png")),
                    ImageIO.read(new File("assets/Player/p1_walk/PNG/p1_walk03.png")),
                    ImageIO.read(new File("assets/Player/p1_walk/PNG/p1_walk04.png")),
                    ImageIO.read(new File("assets/Player/p1_walk/PNG/p1_walk05.png")),
                    ImageIO.read(new File("assets/Player/p1_walk/PNG/p1_walk06.png")),
                    ImageIO.read(new File("assets/Player/p1_walk/PNG/p1_walk07.png")),
                    ImageIO.read(new File("assets/Player/p1_walk/PNG/p1_walk08.png")),
                    ImageIO.read(new File("assets/Player/p1_walk/PNG/p1_walk09.png")),
                    ImageIO.read(new File("assets/Player/p1_walk/PNG/p1_walk10.png")),
                    ImageIO.read(new File("assets/Player/p1_walk/PNG/p1_walk11.png")),
            };

            currentImg = walkImg[0];
            width = currentImg.getWidth();
            height = currentImg.getHeight();

            boundingBox = new BoundingBox(new Vec2(x, y), new Vec2(width, height));

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    //Update player position
    public void move() {
        if (walkLeft) {
            x -= speedX;
        }else if (walkRight) {
            x += speedX;
        }

        if (jump) {
            speedY = JUMP_POWER;
            jump = false; // only jump once per key press
        }

        //Apply gravity
        y += speedY;
        speedY += GRAVITY;

        // respawn in the beginning
        if (y > 2000) {
            y = 300;
            x = 50;
        }

        //update animation frame
        if (walkLeft || walkRight) {
            currentFrame = (currentFrame + 1) % walkImg.length;
            currentImg = walkImg[currentFrame];
        }

        boundingBox = new BoundingBox(new Vec2(x, y), new Vec2(x + width, y + height));
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public BufferedImage getCurrentImg() {
        return currentImg;
    }
}
