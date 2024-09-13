import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class Player {
    BufferedImage[] walkImg = new BufferedImage[11];
    BufferedImage currentImg;
    int currentFrame = 0;

    //Player position and speed
    int x, y;
    final int width, height;
    int speedX = 5;
    int speedY = 1; // for jumping

    boolean walkLeft = false;
    boolean walkRight = false;
    boolean jump = false;
    boolean isGrounded = false;

    //Constants for gravity and jumping
    final int GRAVITY = 1;
    final int JUMP_POWER = -20;
    final int MAX_FULL_SPEED = 100;

    private BoundingBox boundingBox;
    ArrayList<String> jumpSounds = Stream.of(
            "assets/Sound/jump1.wav",
            "assets/Sound/jump2.wav",
            "assets/Sound/jump3.wav"
    ).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

    public Player(int startX, int StartY, String player) {
        x = startX;
        y = StartY;

        try {
            for (int i = 1; i < 12; i++) {
                String number = i < 10 ? "0" + i : "" + i;
                walkImg[i - 1] = ImageIO.read(new File("assets/Player/" + player + "_walk/PNG/" + player + "_walk" + number + ".png"));
            }

            currentImg = walkImg[0];
            width = currentImg.getWidth();
            height = currentImg.getHeight();

            boundingBox = new BoundingBox(new Vec2(x, y), new Vec2(x + width, y + height));

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    //Update player position
    public void move() {
        if (walkLeft) {
            x -= speedX;
        } else if (walkRight) {
            x += speedX;
        }

        if (jump && isGrounded) {
            speedY = JUMP_POWER;
            jump = false; // only jump once per key press
            isGrounded = false;
            playSound(jumpSounds.get((int) (Math.random() * jumpSounds.size())));
        }

        //Apply gravity
        if (!isGrounded) {
            speedY += GRAVITY;
            if (speedY > MAX_FULL_SPEED) {
                speedY = MAX_FULL_SPEED;
            }
            y += speedY;
        }

        // respawn in the beginning
        if (y > 2000) {
            y = 0;
            x = 50;
        }

        //update animation frame
        if (walkLeft || walkRight) {
            currentFrame = (currentFrame + 1) % walkImg.length;
            currentImg = walkImg[currentFrame];
        }

        boundingBox = new BoundingBox(new Vec2(x, y), new Vec2(x + width, y + height));
        isGrounded = false;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public BufferedImage getCurrentImg() {
        return currentImg;
    }

    public void playSound(String path){
        File lol = new File(path);
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(lol));
            clip.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
