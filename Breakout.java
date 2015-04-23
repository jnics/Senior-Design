import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.applet.*;
import java.lang.Math;
import java.net.InetAddress;
import java.net.URL;
import java.util.Random;
import java.io.*;

import sun.audio.*;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Breakout extends Applet implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int MAXY = 500;
	public static int MAXX = 720;
	public static final int SLEEPTIME = 5;
	public static final int BALLSIZE = 5;
	public static final int TARGETLENGTH = 40;
	public static final int TARGETHEIGHT = 10;
	public static final int PADDLEWIDTH = 40;
	public static final int PADDLEHEIGHT = 10;
	public static final int PADDLEALTITUDE = MAXY - 20;
	public static final int LATSPEED = 3;
	public static final int BLOCKSPACINGY = 2;
	public static final int BLOCKSPACINGX = 2;
	public static final int LIVES = 3;
	public static final double INITSPEED_X = 1.25, INITSPEED_Y = -1.25, 
		XHIT_CHANGE = 0.4, YHIT_CHANGE = 0.2;
	public static final Color PADDLECOLOR = randomColorGen(), 
		BALLCOLOR = randomColorGen();
	public static final Color BACKGROUND = randomColorGen();
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
	private Paddle paddle;
	private int games = 0;
	
	public void init() {
		buffer = createImage(MAXX, MAXY);
		gContext = buffer.getGraphics();
		gContext.setColor(BACKGROUND);
		blocks = new BlockHolder();
		paddle = new Paddle();
		ball = new Ball(blocks);
		gContext.fillRect(0, 0, MAXX, MAXY);
		paddle.draw(gContext);
		ball.draw(gContext);
		blocks.draw(gContext);
		if (games < 0){
			Sound.endGame.stop();
			new Sound(this);
			Sound.backGround.loop();
		}
		else{
			new Sound(this);
			Sound.backGround.loop();
		}
		games++;
	}

	
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

	
	public void paint(Graphics g) {
		super.paint(g);
		try {
			colorCheck();
			g.drawImage(buffer, 0, 0, MAXX, MAXY, this);
		}
		catch(Exception e) {}
	}

	
	public void run() {
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


	public boolean keyUp(Event e, int key) {
		if(key == 1006)
			leftArrow = false;
		if(key == 1007)
			rightArrow = false;
		return true;
	}


	public void win() {
		showStatus("Congradulations From Group 3! Your score was: " + score);
		gContext.setColor(Breakout.BACKGROUND);
		gContext.fillRect(0,0,MAXX,MAXY);
		repaint();
		animate.stop();
		Sound.backGround.stop();
		Sound.win.play();
		Sound.endGame.loop();
		tipWindow(tips());
		score = 0;
	}


	public void lose() {
		if(numberlost < LIVES) {
			Sound.miss.play();
			numberlost++;
			showStatus("Try Again. Your score was: " + score);
			gContext.setColor(Breakout.BACKGROUND);
			paddle.clearPaddle(gContext);
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
			Sound.miss.play();
			tipWindow(tips());
			

			score = 0;
		}
	}
	
	// Message box to display tips
	public void tipWindow(String[] s){
		Sound.backGround.stop();
		Sound.listen.play();
		Sound.endGame.loop();
		JTextArea text = new JTextArea();
		if(score >= 0 && score < 10){
			text = new JTextArea(s[0] + "\n" + s[1] +  "\n" + s[2] + "\n" + s[3]);
			scrollPane(text);
		}
		else if (score > 9 && score < 20){
			text = new JTextArea( s[0] + "\n" + s[1] + "\n" + s[2] + "\n" + s[3] + "\n" + s[4]);
			scrollPane(text);
		}
		else if (score > 19 && score < 30){
			text = new JTextArea(s[0] + "\n" + s[1] + "\n" +  s[2] + "\n" + s[3] + "\n" + s[4] + "\n" + s[5]);
			scrollPane(text);
		}
		else if (score > 29 && score < 40){
			text = new JTextArea( s[0] + "\n" + s[1] + "\n" + s[2] + "\n" + s[3] + "\n" + s[4] + "\n" + s[5]+ "\n" + s[6]);
			scrollPane(text);
		}
		else{
			text = new JTextArea(s[0] + "\n" + s[1] + "\n" + s[2] + "\n" + s[3] + "\n" + s[4] + "\n" + s[5]+ "\n" + s[6] + "\n" + s[7]);
			scrollPane(text);
		}
	}
	
	
	
	public void scrollPane(JTextArea textArea){
		JScrollPane scrollPane = new JScrollPane(textArea);  
		textArea.setLineWrap(true);  
		textArea.setWrapStyleWord(true); 
		scrollPane.setPreferredSize( new Dimension( MAXX, MAXY ) );
		JOptionPane.showMessageDialog(null, scrollPane, "Cleveland State CIS Dept. Info",  
		                                       JOptionPane.INFORMATION_MESSAGE);
	}
	
	// where tips can be added to an array
	public String[] tips(){
		String[] s = new String[8];

s[0] = ("BSCIS Admission Requirements\n" +
		        
	">Firstly congratulations from group 3 on getting the a score of " + score + "\n" +
	">Students may declare CIS as their major as soon as they have met the program’s admission requirements:\n\t"+

	">A 2.0 grade point average overall.\n\t "+
	">A grade of C or better in MTH 181, or an average of C+ in MTH 148-149.\n\t "+
	">A grade of C+ or better in CIS 260.\n\t"+
	">Submit an official Declaration of Major form in the Office of Undergraduate Advising, BU 219.\n\t"+
	">The curriculum reflects current computing trends and incorporates current topics to enable a CIS graduate to be competitive in the marketplace\n\n");



		s[1] = ("Two tracks are offered under the BSCIS degree\n"+
		     "   _____________________________________________\n\n"+
	"The CIS track is designed for students interested in incorporating an application area in their degree program.\n"+
	"The CSC track is designed for students interested in theoretical and quantitative foundations of CS.\n\n");



		s[2] = ("Random CIS facts\n"+
		        "________________\n\n"+
				" CSU offers a Co-op program that help gets CIS/IST majors experience in the workforce and possible permanent job opportunities after graduation.\n"+
                 "Computer Science focuses on teaching programming and computing.");
		
		s[3] = ("Careers you can peruse with a CIS & IST degree Part 1\n"+
               

               "	Application Developer\n\t"+
               	"Digital Energy ERP Project Manager\n"+
			   "	IT Consultant\n"+
			   "	IT Support Specialist\n\t"+
			   	"Programmer/Analyst\n");
			 

		s[4] = ("Careers you can peruse with a CIS & IST degree Part 2\n"+
               " ______________________________________________\n\n\t"+
			   	"Project Manager\n\t"+
			   	"Quality Assurance Specialist\n\t"+
		       	"Setup Configuration Specialist\n\t"+
			   	"Information Management Leadership Program\n\t"+
			   	"Infrastructure Architect\n");



		s[5] = "Computer Information Systems (CIS) has an average starting salary of $49,100 and an average mid-career income of $85,965\n";

		s[6] = "Computer Information System is defined as the study of interconnected networks of hardware and software which companies use to collect, filter, process and build data.\n "; 

		s[7] = (" Fun Facts about CSU CIS & IST Department Staff\n"+
		         "______________________________________________\n\n "+
				"   Dr. Henry has a boat named SEA ++ \n"+
				"Professor Ardnt use to teach in Italy\n"+ 
				 "Professor Dolah use to work for IBM\n"+
		        " Professor H. Wang Likes to Compete in programming competitions\n"+
		        " Dr. Blake is a certified level 1 snowboard instruct"); 
		return s;
	}

	public boolean getLeftArrow() {
		return leftArrow;
	}

	public boolean getRightArrow() {
		return rightArrow;
	}
	

	public static Color randomColorGen() {
		Random rand = new Random();
		int red = rand.nextInt(255);
		int green = rand.nextInt(255);
		int blue = rand.nextInt(255);
		return new Color(red, green, blue);
	}
	
	
	public void colorCheck(){
		Color[] colorA = new Color[9];
		colorA[0] = BACKGROUND;
		colorA[1] = PADDLECOLOR;
		colorA[2] = BALLCOLOR;
		colorA[3] = BlockHolder.lines[0].color;
		colorA[4] = BlockHolder.lines[1].color;
		colorA[5] = BlockHolder.lines[2].color;
		colorA[6] = BlockHolder.lines[3].color;
		colorA[7] = BlockHolder.lines[4].color;
		colorA[8] = BlockHolder.lines[5].color;
		try {
			for (int i = 0; i < colorA.length; i++){
				for (int j = 0; j < colorA.length; j++){
					if (i == j){
						j++;
						if (j == colorA.length){
							break;
						}
					}
					if (colorA[i].getRGB() == colorA[j].getRGB()){
						colorA[i] = colorA[i].darker();
						colorA[i] = colorA[j].brighter();
						colorCheck();
					}
				}
			}
		}
		catch(Exception e){}	
	}
}

//**********************************************************************************************************************
//**********************************************************************************************************************
//**********************************************************************************************************************
//**********************************************************************************************************************

class BlockHolder {
	private int blockCount;
	protected static Line[] lines;
	private int TARGETHEIGHT;

	public BlockHolder() {
		TARGETHEIGHT = Breakout.TARGETHEIGHT;
		lines = new Line[6];
		prepareBlocks();
	}

	
	public void restart() {
		blockCount = 0;
		for(int i = 0; i < lines.length; i++) {
			lines[i].restart();
			blockCount += lines[i].numberblocks;
		}
	}


	public void prepareBlocks() {
		int spacing = Breakout.BLOCKSPACINGY;
		lines[0] = new Line(0, Breakout.randomColorGen());
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

	public void draw(Graphics g) {
		for(int i = 0; i < lines.length; i++)
			lines[i].draw(g);
	}


	public int blocksLeft() {
		return blockCount;
	}
	
}

//**********************************************************************************************************************
//**********************************************************************************************************************
//**********************************************************************************************************************
//**********************************************************************************************************************

class Line {
	public int bottom, top, numberblocks, initial;
	public Block[] blocks;
	private boolean[] exists;
	private int TARGETLENGTH;
	Color color = Breakout.randomColorGen();


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


	public void draw(Graphics g) {
		for(int i = 0; i < numberblocks; i++) {
			if(exists[i])
				blocks[i].draw(g);
		}
	}
	

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

//**********************************************************************************************************************
//**********************************************************************************************************************
//**********************************************************************************************************************
//**********************************************************************************************************************

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


	public void clearBlock(Graphics g) {
		g.setColor(Breakout.BACKGROUND);
		Breakout.score++;
		g.fillRect(x, y, MAXX, MAXY);
	}
	

	public void clearPaddle(Graphics g) {
		g.setColor(Breakout.BACKGROUND);
		g.fillRect(x, y, MAXX, MAXY);
	}


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
	
//**********************************************************************************************************************
//**********************************************************************************************************************
//**********************************************************************************************************************
//**********************************************************************************************************************
 
class Paddle extends Block {
	public boolean moved = false;

	public Paddle() {
		super(Breakout.MAXX / 2 - Breakout.PADDLEWIDTH / 2, 
			Breakout.PADDLEALTITUDE, 
			Breakout.PADDLECOLOR);
		MAXX = Breakout.PADDLEWIDTH;
		MAXY = Breakout.PADDLEHEIGHT;

	}


	public void go(int x0, int y0) {
		x=x0;
		y=y0;
	}
	

	public void move(int change) {
		if((x+MAXX < Breakout.MAXX || change < 0) && (x > 0 || change > 0)) { 
			x = x + change;
			moved = true;
		}
		else
			moved = false;
	}

}

//**********************************************************************************************************************
//**********************************************************************************************************************
//**********************************************************************************************************************
//**********************************************************************************************************************

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

	public void go(int x0, int y0) {
		x = (double) x0;
		y = (double) y0;
		xchange = 0.0;
		ychange = 0.0;
	}

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

	public void move(double xchange, double ychange, Graphics g) {
		x = x + xchange;
		y = y + ychange;
	}


	public void checkBlocks(Graphics g) {
		Block hit = blocks.whohit((int) (x + xchange), (int) (y + ychange));
	
		if(hit != null) {
			hit.clearBlock(g);
			Sound.hit.play();
			ychange = -ychange;
		}
	}


	public void clear(Graphics g) {
		g.setColor(Breakout.BACKGROUND);
		g.fillOval((int) x, (int) y, radius * 2,radius * 2);
	}


	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval((int) x,(int) y, radius * 2, radius * 2);
	}
}

//**********************************************************************************************************************
//**********************************************************************************************************************
//**********************************************************************************************************************
//**********************************************************************************************************************

class Sound extends Breakout{
	
	static AudioClip backGround, hit, miss, win, endGame, listen; // Sound player
	URL url;
	static Breakout bo;

	// In order to get music to work use new Sound(this); and on next line use Sound.(sound wanting to play).play() or Loop()
	public Sound(Breakout bo){
		try {
			url = bo.getDocumentBase();
		}
		catch(Exception e){
		}
		backGround = bo.getAudioClip(url, "Music/G3BackgroundMusic.au");
		win = bo.getAudioClip(url, "Music/G3WinMusic.au");
		hit = bo.getAudioClip(url, "Music/G3Hit.au");
		endGame = bo.getAudioClip(url, "Music/G3EndGame.au");
		listen = bo.getAudioClip(url, "Music/G3Info.au");
		miss = bo.getAudioClip(url, "Music/GEMiss.au");
	}
	

}
