import java.awt.*;
import java.applet.*;
import java.lang.Math;
import java.net.InetAddress;
import java.util.Random;


/* Here is the code for Group #3 Breakout Game, we tried to make the names
   of the variables simple as possible. */
public class Breakout extends Applet implements Runnable {
	
	
	
	private static final long serialVersionUID = 1L;
	public static final int MAXY = 320, MAXX = 320, SLEEPTIME = 5, 
		BALLSIZE = 5, TARGETLENGTH = 40, TARGETHEIGHT = 10, 
		PADDLEWIDTH = 40, PADDLEHEIGHT = 10, PADDLEALTITUDE = MAXY - 20, 
		LATSPEED = 3, BLOCKSPACINGY = 2, BLOCKSPACINGX = 2,
                LIVES = 3;
	public static final double INITSPEED_X = 1.25, INITSPEED_Y = -1.25, 
		XHIT_CHANGE = 0.4, YHIT_CHANGE = 0.2;
	public static final Color PADDLECOLOR = randomColorGen(), 
		BALLCOLOR = randomColorGen();
	public static final Color BACKGROUND = randomColorGen(), TEXTCOLOR = randomColorGen();
	public static final String BEEP_SOUND = "beep.au"; 

	private boolean paused = false;
	private int numberlost = 0;
	static int score;
	private Graphics gContext;
	private Image buffer;
	private Thread animate;
	private boolean leftArrow = false, rightArrow = false, ballready = true;
	private Ball ball;
	private BlockHolder blocks;
	private AudioClip hitsound;
	private Paddle paddle;

/*starts the game and create the blocks, paddle, and box 
   in the graphics buffer */
	public void init() {
		hitsound = getAudioClip(getDocumentBase(), BEEP_SOUND);
		hitsound.play();
		buffer = createImage(MAXX, MAXY);
		gContext = buffer.getGraphics();
		gContext.setColor(BACKGROUND);
		blocks = new BlockHolder();
		paddle = new Paddle();
		ball = new Ball(blocks, hitsound);
		gContext.fillRect(0, 0, MAXX, MAXY);
		paddle.draw(gContext);
		ball.draw(gContext);
		blocks.draw(gContext);
	}

	/* Necessary method for starting the game's thread */
	public void start() {
		if(animate == null) {
			animate = new Thread(this);
			animate.start();
		}
	}

	public void restart() {	}
		
	public void stop() {
		if(animate != null) {
			animate.stop();
			animate = null;
		}
	}

	/* Paints the graphics buffer onto the screen */
	public void paint(Graphics g) {
		try {
			g.drawImage(buffer, 0, 0, this);
		}
		catch(Exception e) {}
	}

	/* This method clears the paddle and ball from the buffer.
	   Checks to see if any blocks have been hit, then redraws
	   the screen as it should now be drawn. Since the ball is
	   drawn as a point, but is actually a few pixels in radius, 
	   blocks will sometimes be partially cleared, but not broken.
	   Once the drawing is in the buffer, the thread sleeps for 
	   the time set in SLEEPTIME, before moving the ball once more */
	public void run() {
		//showStatus("Click on Field to Play");
		try {
                   showStatus(InetAddress.getLocalHost().toString());
		} catch(Exception e) {};

		while(true) {
			paddle.clearPaddle(gContext);
			ball.clear(gContext);

			if(leftArrow) {
				paddle.move(-LATSPEED);
				if(ballready && paddle.moved)
					ball.move(-LATSPEED,0,gContext);
			}

			if(rightArrow) {
				paddle.move(LATSPEED);
				if(ballready && paddle.moved)
					ball.move(LATSPEED,0,gContext);
			}

			if(!ballready) {
				ball.selfMove(gContext,paddle,this);
			}

			if(blocks.blocksLeft() == 0)
				win();

			paddle.draw(gContext);
			ball.draw(gContext);

			try {
				Thread.sleep(SLEEPTIME);
			}
			catch (InterruptedException e) {}

			repaint();
		}
	}

	public void update(Graphics g) {
		paint(g);
	}

	/* In this method the different keys represent certain aspects of the game 
	   controle if you press the up arrow key (#1004), and the ball is on the paddle, 
	   then the game starts. If you press the left arrow key  (#1006) or right 
	   arrow key (#1007) then paddle is moved in the direction of the key, and if 
	   it was the 'P' key (#104), then the game is paused.*/

	public boolean keyDown(Event e, int key) {
		if(key == 1004 && ballready) {
			showStatus("Press P to pause. Score: " + Breakout.score);
			ballready = false;
			ball.xchange = INITSPEED_X;
			ball.ychange = INITSPEED_Y;
		}
		if(key == 112) {
			if(!paused) {
				paused = true;
				animate.suspend();
				showStatus("Press P to unpause Score: " + Breakout.score);
			}
			else {
				showStatus("Press P to pause Score: " + Breakout.score);
				paused = false;
				animate.resume();
			}	
		}
		if(key == 1006)
			leftArrow = true;
		if(key == 1007)
			rightArrow = true;
		return true;
	}

	/* Stops moving the paddle, if one of the arrows was down */
	public boolean keyUp(Event e, int key) {
		if(key == 1006)
			leftArrow = false;
		if(key == 1007)
			rightArrow = false;
		return true;
	}

	/* This method is called when you win the first and only level
	   of the breakout game */

	public void win() {
<<<<<<< HEAD
<<<<<<< HEAD
		showStatus("Congradulations Ben!");
		gContext.setColor(BACKGROUND);
=======
		showStatus("Congradulations From Group 3! Your score was: " + score);
=======
		showStatus("Congradulations From Group 3!!!");
>>>>>>> master
		gContext.setColor(Breakout.BACKGROUND);
>>>>>>> origin/CIS457
		gContext.fillRect(0,0,MAXX,MAXY);
		repaint();
		animate.stop();
		tipWindow(tips());
		score = 0;
	}

	/* This method is called when you lose, and the game must be
	reinitialized */
	public void lose() {
		if(numberlost < LIVES) {
			numberlost++;
<<<<<<< HEAD
<<<<<<< HEAD
			showStatus("Try Again");
			gContext.setColor(BACKGROUND);
=======
			showStatus("You Lose, Try Again");
			gContext.setColor(Breakout.BACKGROUND);
>>>>>>> master
			paddle.clear(gContext);
=======
			showStatus("Try Again. Your score was: " + score);
			gContext.setColor(Breakout.BACKGROUND);
			paddle.clearPaddle(gContext);
>>>>>>> origin/CIS457
			ball.clear(gContext);
			paddle.go(MAXX / 2 - 20, PADDLEALTITUDE);
			ball.go(MAXX / 2 - BALLSIZE, PADDLEALTITUDE - 5);
			ballready = true;
		}
		else {
			numberlost = 0;
			showStatus("You Lose.  Your score was: " + score);
			gContext.setColor(Breakout.BACKGROUND);
			gContext.fillRect(0,0,MAXX, MAXY);
			paddle.go(MAXX / 2 - 20, PADDLEALTITUDE);
			ball.go(MAXX / 2 - BALLSIZE, PADDLEALTITUDE - 5);
			blocks.restart();
			blocks.draw(gContext);
			ballready = true;
			tipWindow(tips());
			score = 0;
		}
	}
	
	// Message box to display tips
	public void tipWindow(String[] s){
		JOptionPane.showMessageDialog(null, s[0] + "\n" + s[1]);
	}
	
	// where tips can be added to an array
	public String[] tips(){
		String[] s = new String[5];
		s[0] = "Hi from Group 3";
		s[1] = "Also Tacos";
		return s;
	}

	public boolean getLeftArrow() {
		return leftArrow;
	}

	public boolean getRightArrow() {
		return rightArrow;
	}
	
	/* newly added code creates a random color for any item that uses color
	 This code is to be used through out calls to the color class
	 */
	public static Color randomColorGen() {
		Random rand = new Random();
		int red = rand.nextInt(255);
		int green = rand.nextInt(255);
		int blue = rand.nextInt(255);
		return new Color(red, green, blue);
	}
}


/* This class represents the group of blocks in their current rectangular
   arrangement, and can be rewritten to represent new arrangements for
   different levels, if anyone feels so inclined to write them */
class BlockHolder {
	private int blockCount;
	private Line[] lines;
	private int TARGETHEIGHT;

	public BlockHolder() {
		TARGETHEIGHT = Breakout.TARGETHEIGHT;
		lines = new Line[6];
		prepareBlocks();
	}

	/* Called when the game is lost, and the blocks are redrawn */
	public void restart() {
		blockCount = 0;
		for(int i = 0; i < lines.length; i++) {
			lines[i].restart();
			blockCount += lines[i].numberblocks;
		}
	}

	/* prepareBlocks() is called by the BlockHolder constructor, and 
	   prepares all the lines of blocks to be drawn, by initializing
	   each line with the proper hight and colors of blocks. */
	public void prepareBlocks() {
		int spacing = Breakout.BLOCKSPACINGY;
<<<<<<< HEAD
		lines[0] = new Line(0, Breakout.randomColorGen());
=======
		lines[0] = new Line(0,  Breakout.randomColorGen());
>>>>>>> master
		lines[1] = new Line(TARGETHEIGHT+spacing, Breakout.randomColorGen());
		lines[2] = new Line(TARGETHEIGHT*2+2*spacing, Breakout.randomColorGen());
		lines[3] = new Line(TARGETHEIGHT*3+3*spacing, Breakout.randomColorGen());
		lines[4] = new Line(TARGETHEIGHT*4+4*spacing, Breakout.randomColorGen());
		lines[5] = new Line(TARGETHEIGHT*5+5*spacing, Breakout.randomColorGen());
		
		for(int i = 0; i < lines.length; i++) {
			blockCount += lines[i].numberblocks;
			lines[i].fill();
		}
	}


	/* Returns the block which is hit when a ball passes at a point of 
	(x,y) */
	public Block whohit(int x, int y) {
		for(int i = 0; i < lines.length; i++) {
			if(lines[i].bottom >= y && lines[i].top <= y) {
				Block hit = lines[i].whohit(x);
				if(hit != null)
					blockCount -= 1;
				return hit;
			}
		}
		return null;
	}

	/* Is called by the init method of the Breakout game to draw
	   the blocks into the graphics buffer */
	public void draw(Graphics g) {
		for(int i = 0; i < lines.length; i++)
			lines[i].draw(g);
	}

	/* Returns the number of blocks still unhit on the playing field */
	public int blocksLeft() {
		return blockCount;
	}
	
}

/* Representing a line of blocks on the screen, this class is used a lot
   by BlockHolder */
class Line {
	public int bottom, top, numberblocks, initial;
	public Block[] blocks;
	private boolean[] exists;
	private int TARGETLENGTH;
	private Color color = Color.CYAN;

	/* Prepares a Line of blocks, starting at hight top0, and
	   with the color color0 */
	public Line(int top0, Color color0) {
		top = top0;
		color = color0;
		TARGETLENGTH = Breakout.TARGETLENGTH;
		bottom = top + Breakout.TARGETHEIGHT;
		numberblocks = (int) Breakout.MAXX / TARGETLENGTH;
		initial = (int) (Breakout.MAXX - TARGETLENGTH * numberblocks) / 2;
		blocks = new Block[numberblocks];
		exists = new boolean[numberblocks];
	}
	
	/* Fills the line with its respective blocks */
	public void fill() {
		for(int i = 0; i < numberblocks; i++) {
			blocks[i] = new Block(TARGETLENGTH * i + initial, top + 1, color);
			exists[i] = true;
		}
	}

	public void restart() {
		for(int i = 0; i < numberblocks; i++) {
			exists[i] = true;
		}
	}

	/* Draws the line onto the graphics buffer */
	public void draw(Graphics g) {
		for(int i = 0; i < numberblocks; i++) {
			if(exists[i])
				blocks[i].draw(g);
		}
	}
	
	/* Used by the BlockHolder class to determine which block is 
	   hit, whohit returns which block is within location x */
	public Block whohit(int x) {
		int numhit = (int) (x - initial) / TARGETLENGTH;
		
		if(numhit > numberblocks) 
			numhit = numberblocks;

		if(exists[numhit]) {
			exists[numhit] = false;
			return blocks[numhit];
		}
		return null;
	}
	
}

/* Creates a block that can be drawn onto the graphics buffer. Also is
   used as a parent for the paddle class, since most functions are the
   same. */
class Block {
	public int x, y;
	protected Color color = Breakout.randomColorGen();
	protected int MAXX, MAXY;

	/* Creates block starting at postition (x0, y0) of color color0 */
	public Block(int x0, int y0, Color color0) {
		x = x0;
		y = y0;
		color = color0;
		MAXX = Breakout.TARGETLENGTH - Breakout.BLOCKSPACINGX / 2;
		MAXY = Breakout.TARGETHEIGHT - Breakout.BLOCKSPACINGY / 2;
	}

<<<<<<< HEAD
	/* Erases the block, by making it the color of the background */
	public void clear(Graphics g) {
<<<<<<< HEAD
=======
	/* Erases the block, by making it the color of the background and keeping score of each hit */
	public void clearBlock(Graphics g) {
		g.setColor(Breakout.BACKGROUND);
		Breakout.score++;
		g.fillRect(x, y, MAXX, MAXY);
	}
	
	/* clears the paddle so as we move the previous position the paddle was in is now the background color */
	public void clearPaddle(Graphics g) {
>>>>>>> origin/CIS457
=======
>>>>>>> master
		g.setColor(Breakout.BACKGROUND);
		g.fillRect(x, y, MAXX, MAXY);
	}

	/* Draws the block onto g */
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, MAXX, MAXY);
	}

	public int getTop() {
		return y;
	}

	public int getBottom() {
		return y + MAXY;
	}

	public int getleftx() {
		return x;
	}

	public int getrightx() {
		return x + MAXX;
	}

}
	
/* This class makes the paddle, it works similar to 
   the block class, but also has movement methods */ 
class Paddle extends Block {
	public boolean moved = false;

	public Paddle() {
		super(Breakout.MAXX / 2 - Breakout.PADDLEWIDTH / 2, 
			Breakout.PADDLEALTITUDE, 
			Breakout.PADDLECOLOR);
		MAXX = Breakout.PADDLEWIDTH;
		MAXY = Breakout.PADDLEHEIGHT;

	}

	/* Place block on (x0, y0) */
	public void go(int x0, int y0) {
		x=x0;
		y=y0;
	}
	
	/* Move the block change places on the x axis */
	public void move(int change) {
		if((x+MAXX < Breakout.MAXX || change < 0) && (x > 0 || change > 0)) { 
			x = x + change;
			moved = true;
		}
		else
			moved = false;
	}

}

/* This class creates the most important part to the Breakout game the ball, 
   this class draws the ball, moves the ball  and also works with the BlockHolder to erase 
   the blocks the ball hits.  */
   
class Ball {
	public double xchange, ychange;
	private double x, y;
	private int radius;
	private Color color = Breakout.randomColorGen();
	private BlockHolder blocks;
	private AudioClip beep;

	/* Creates a ball in a design with a blocks0 BlockHolder and a 
	   beeping noise of beep0. */
	public Ball(BlockHolder blocks0, AudioClip beep0) {
		xchange = 0;
		ychange = 0;
		radius = Breakout.BALLSIZE;
		x = Breakout.MAXX / 2 - radius;
		y = Breakout.PADDLEALTITUDE - 5;
		color = Breakout.BALLCOLOR;
		blocks = blocks0;
		beep = beep0;
	}

	public void go(int x0, int y0) {
		x = (double) x0;
		y = (double) y0;
		xchange = 0.0;
		ychange = 0.0;
	}

	/* Here the ball moves itself across the screen based on the position
	   of the blocks and paddle */
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

	/* Moves the ball xchange in the x direction and ychange in the y
	   direction */
	public void move(double xchange, double ychange, Graphics g) {
		x = x + xchange;
		y = y + ychange;
	}

	/* Checks to see if the ball is about to hit a block, if so it 
	   reverses the ychange variable and makes the beep noise */
	public void checkBlocks(Graphics g) {
		Block hit = blocks.whohit((int) (x + xchange), (int) (y + ychange));
	
		if(hit != null) {
			hit.clearBlock(g);
			beep.play();
			ychange = -ychange;
		}
	}

	/* Erases the ball */
	public void clear(Graphics g) {
		g.setColor(Breakout.BACKGROUND);
		g.fillOval((int) x, (int) y, radius * 2,radius * 2);
	}

	/* Draws the ball at its current position */
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval((int) x,(int) y, radius * 2, radius * 2);
	}
}
