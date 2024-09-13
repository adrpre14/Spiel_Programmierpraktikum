import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Platformer extends JFrame {
	private static final long serialVersionUID = 1L;

	private Level level;
	private Player player;
	private int scrollX = 0;  // Horizontal scroll position
	private final int scrollSpeed = 20;  // Speed of scrolling
	private BufferStrategy bufferStrategy;  // For double buffering

	public Platformer() {
		// Exit program when window is closed
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Initialize level and player
		try {
			level = new Level("my_level2.bmp");
			player = new Player(50, 300);
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
	private void draw(Graphics2D g2d) {
		BufferedImage levelImg = level.getImage();

		// Only show the visible part of the level image (1000x350 pixels)
		BufferedImage visibleLevel = levelImg.getSubimage(scrollX, 0, 1000, level.getImage().getHeight());
		g2d.drawImage(visibleLevel, 0, 0, null);

		// Draw the player on top of the level
		g2d.drawImage(player.getCurrentImg(), player.x - scrollX, player.y, null);
	}

	@Override
	public void paint(Graphics g) {
		// Double-buffered drawing
		Graphics2D g2d = null;
		if (bufferStrategy == null) return;
		try {
			g2d = (Graphics2D) bufferStrategy.getDrawGraphics();
			draw(g2d);
		} finally {
			if (g2d != null) {
				g2d.dispose();
			}
		}
		bufferStrategy.show();
	}

	private void checkCollision() {
		for (Tile tile : level.tiles) {
			if (player.getBoundingBox().intersect(tile.getBoundingBox())) {
				Vec2 result = player.getBoundingBox().overlapSize(tile.getBoundingBox());
				if (result.x > result.y) { // Vertical collision
					if (player.y < tile.getBoundingBox().max.y) { // Player is above the tile
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
	}
}
