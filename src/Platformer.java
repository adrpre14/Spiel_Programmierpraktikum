import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Platformer extends JFrame {
	private static final long serialVersionUID = 1L;

	private Level level;
	private Player player;
	private int scrollX = 20;  // Horizontal scroll position
	private final int scrollSpeed = 5;  // Speed of scrolling
	private BufferStrategy bufferStrategy;  // For double buffering
	private ArrayList<Tile> tilesToRemove = new ArrayList<>();

	public Platformer() {
		// Exit program when window is closed
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Initialize level and player
		try {
			level = new Level("my_level2_1.bmp");
			player = new Player(50, 300);
			player.playSound("assets/Sound/soundtrack.wav");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Set window size
		this.setBounds(0, 0, 1000, level.getImage().getHeight());
		this.setVisible(true);

		// Create buffer strategy for double buffering
		this.createBufferStrategy(2);
		bufferStrategy = this.getBufferStrategy();

		// Add key listener for player movement
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						player.walkLeft = true;
						break;
					case KeyEvent.VK_RIGHT:
						player.walkRight = true;
						break;
					case KeyEvent.VK_SPACE:
						player.jump = true;
						break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						player.walkLeft = false;
						break;
					case KeyEvent.VK_RIGHT:
						player.walkRight = false;
						break;
				}
			}
		});

		// Timer to update the game state periodically
		new Timer(10, event -> {
			player.move();  // Update player movement
			updateScrollPosition();  // Update the camera scroll
			checkCollision();
			repaint();
		}).start();
	}

	// Update scroll position to follow the player
	private void updateScrollPosition() {
		int screenCenter = 500;

		if(player.x > screenCenter) {
			scrollX = player.x - screenCenter;
		} else {
			scrollX = 0;
		}

		// Clamp the scroll position to the level bounds to prevent scrolling past the level
		scrollX = Math.max(0, Math.min(scrollX, level.getImage().getWidth() - 1000));
	}

	// New draw method to render the game elements
	private void draw(Graphics2D g2d) throws IOException {
		BufferedImage levelImg = level.getImage();

		// Only show the visible part of the level image (1000x350 pixels)
		BufferedImage visibleLevel = levelImg.getSubimage(scrollX, 0, 1000, level.getImage().getHeight());
		g2d.drawImage(visibleLevel, 0, 0, null);

		for (Tile tile : level.dynamicTile) {
			 if (tile.getImageIndex() == 19) {
				 if (tilesToRemove.contains(tile)) {
					 continue;
				 }
				g2d.drawImage(tile.getImage(),(int) tile.getBoundingBox().min.x - scrollX, (int) tile.getBoundingBox().min.y, null);
			}
		}

		if (player.walkLeft) {
			// Flip the image horizontally
			g2d.drawImage(flipImageHorizontally(player.currentImg), player.x - scrollX - player.currentImg.getWidth()+100, player.y, null);
		}
		else {
			g2d.drawImage(player.currentImg, player.x - scrollX, player.y, null);
		}

	}

	@Override
	public void paint(Graphics g) {
		// Double-buffered drawing
		Graphics2D g2d = null;
		if (bufferStrategy == null) return;
		try {
			g2d = (Graphics2D) bufferStrategy.getDrawGraphics();
			draw(g2d);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (g2d != null) {
				g2d.dispose();
			}
		}
		bufferStrategy.show();
	}

	private void checkCollision() {
		for (Tile tile : level.tiles) {
			if (tile.getImageIndex() == 0 || tile.getImageIndex() == 1
					|| tile.getImageIndex() == 5 || tile.getImageIndex() == 13
					|| tile.getImageIndex() == 14 || tile.getImageIndex() == 12
					|| tile.getImageIndex() == 17 || tile.getImageIndex() == 18) {
				// player should not collide with water or water center tiles
				continue;
			} else if (tile.getImageIndex() == 19) {
				// player should be able to collect coins
				if (player.getBoundingBox().intersect(tile.getBoundingBox())) {
					tilesToRemove.add(tile);
					player.playSound("assets/Sound/coin.wav");
					continue;
				}
			} else if (player.getBoundingBox().intersect(tile.getBoundingBox())) {
				Vec2 result = player.getBoundingBox().overlapSize(tile.getBoundingBox());
				if (result.x > result.y) { // Vertical collision
					if (player.getBoundingBox().max.y < tile.getBoundingBox().max.y) { // Player is above the tile
						player.y -= (int) result.y;
						player.speedY = 0;
//						System.out.println("Collision bottom");
					} else { // Player is below the tile
						player.y += (int) result.y;
						player.speedY = 0;
//						System.out.println("Collision top");
					}
				} else {
					// Horizontal collision
					if (player.x < tile.getBoundingBox().min.x) { // Player is to the left of the tile
						player.x -= (int) result.x;
//						System.out.println("Collision right");
					} else { // Player is to the right of the tile
						player.x += (int) result.x;
//						System.out.println("Collision left");
					}
				}
			}
		}
		level.tiles.removeAll(tilesToRemove);
	}

	private BufferedImage flipImageHorizontally(BufferedImage img) {
		AffineTransform transform = AffineTransform.getScaleInstance(-1, 1);  // Flip the x-axis
		transform.translate(-img.getWidth(), 0);  // Adjust translation to fit the flipped image

		// Create a new image with the same dimensions
		BufferedImage flippedImage = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

		// Draw the flipped image
		Graphics2D g2d = flippedImage.createGraphics();
		g2d.drawImage(img, transform, null);
		g2d.dispose();  // Clean up graphics context

		return flippedImage;
	}

}
