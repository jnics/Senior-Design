import java.awt.Color;
import java.awt.Graphics;

class Block {
	public int x, y;
	protected Color color = Breakout.randomColorGen();
	protected int MAXX, MAXY;


	public Block(int x0, int y0, Color color0) {
		x = x0;
		y = y0;
		color = color0;
		MAXX = Breakout.TARGETLENGTH - Breakout.BLOCKSPACINGX / 2;
		MAXY = Breakout.TARGETHEIGHT - Breakout.BLOCKSPACINGY / 2;
	}

	// Draws the block from its lines color to the background color after it is hit with the ball.
	public void clearBlock(Graphics g) {
		g.setColor(Breakout.BACKGROUND);
		g.fillRoundRect(x, y, MAXX, MAXY, 9, 9);
	}
	
	// Clears out the paddle to the background color
	public void clearPaddle(Graphics g) {
		g.setColor(Breakout.BACKGROUND);
		g.fillRoundRect(x, y, MAXX, MAXY, 9, 9);
	}

	// Actual draw call that creates a visible representation of the block it is being called on.
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRoundRect(x, y, MAXX, MAXY, 9, 9);
	}

	// Gets the highest y value of the block
	public int getTop() {
		return y;
	}

	// Gets the lowest y value of the block
	public int getBottom() {
		return y + MAXY;
	}

	// Gets the lowest x value of block
	public int getleftx() {
		return x;
	}

	// Gets the highest x value of the block
	public int getrightx() {
		return x + MAXX;
	}

}
