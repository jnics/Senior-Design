import java.awt.Color;
import java.awt.Graphics;

class Ball {
	public double xchange, ychange;
	private double x, y;
	private int radius;
	private Color color = Breakout.BALLCOLOR;
	private BlockHolder blocks;



	public Ball(BlockHolder blocks0) {
		xchange = 0;
		ychange = 0;
		radius = Breakout.BALLSIZE;
		x = Breakout.MAXX / 2 - radius;
		y = Breakout.PADDLEALTITUDE - 5;
		color = Breakout.BALLCOLOR;
		blocks = blocks0;
	}

	// Indicates where the ball is to be graphically changed to
	public void go(int x0, int y0) {
		x = (double) x0;
		y = (double) y0;
		xchange = 0.0;
		ychange = 0.0;
	}

	// Indicates the self propulsion of the ball
	public void selfMove(Graphics g, Block paddle, Breakout ap) {
		if(x + xchange >= Breakout.MAXX)
			xchange =- Math.abs(xchange);
		if(x + xchange <= 0)
			xchange = Math.abs(xchange);
		if(y + ychange <= 0)
			ychange =- ychange;
		if(paddle.getTop() <= y + ychange && paddle.getTop() >= y - ychange &&
			x + xchange >= paddle.getleftx() && x + xchange <= paddle.getrightx()) {
			ychange =- Math.abs(ychange);
			if(ap.getLeftArrow()) {
				xchange -= Breakout.XHIT_CHANGE;
				ychange -= Breakout.YHIT_CHANGE;
			}
			if(ap.getRightArrow()) {
				xchange += Breakout.XHIT_CHANGE;
				ychange -= Breakout.YHIT_CHANGE;
			}
		}
		if(y + ychange >= Breakout.MAXX)	{
			ap.lose();
		}
		checkBlocks(g);

		move(xchange, ychange, g);
	}

	// Indicates the movement of the ball
	public void move(double xchange, double ychange, Graphics g) {
		x = x + xchange;
		y = y + ychange;
	}

	// Checks to see if a block has been hit. if it has then it needs to be cleared
	public void checkBlocks(Graphics g) {
		Block hit = blocks.whohit((int) (x + xchange), (int) (y + ychange));
		if(hit != null) {
			hit.clearBlock(g);
			Sound.hit.play();
			ychange = -ychange;
		}
		
	}

	// clears out the ball to the background color
	public void clear(Graphics g) {
		g.setColor(Breakout.BACKGROUND);
		g.fillOval((int) x, (int) y, radius * 2,radius * 2);
	}

	// Actual visible representation of the ball
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval((int) x,(int) y, radius * 2, radius * 2);
	}
}
