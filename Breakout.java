import java.awt.*;
import java.applet.*;
import java.net.InetAddress;
import java.util.Random;
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

	// Initial display of the Breakout game, which is only called when the window is first opened
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
		new Sound(this);
		Sound.backGround.loop();
	}

	// Begins the game
	public void start() {
		if(animate == null) {
			animate = new Thread(this);
			animate.start();
		}
	}
	
	// Empty is used in other classes
	public void restart() {	}
		
	// Stops the game from continuing
	public void stop() {
		if(animate != null) {
			animate.stop();
			animate = null;
		}
	}

	// Called to create the graphical representation of the game
	public void paint(Graphics g) {
		super.paint(g);
		try {
			colorCheck();
			g.drawImage(buffer, 0, 0, MAXX, MAXY, this);			
		}
		catch(Exception e) {}
	}

	// Starts and runs the thread that is the game 
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

	//method that is called when we see a repaint(); call
	public void update(Graphics g) {
		paint(g);
	}

	// Sets the jobs for keys
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

	// Final declaration of jobs for the keys
	public boolean keyUp(Event e, int key) {
		if(key == 1006)
			leftArrow = false;
		if(key == 1007)
			rightArrow = false;
		return true;
	}

	// Displays all sounds and tips that go along with winning
	public void win() {
		showStatus("Congradulations From Group 3! Your score was: " + score);
		gContext.setColor(Breakout.BACKGROUND);
		gContext.fillRect(0,0,MAXX,MAXY);
		repaint();
		animate.stop();
		Sound.backGround.stop();
		Sound.win.play();
		tipWindow(tips());
		score = 0;
	}
	
	// Displays all sounds and tips that go along with losing
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
			Sound.backGround.stop();
			Sound.endGame.play();
			tipWindow(tips());
			score = 0;
		}
	}
	
	// Message box to display tips
	public void tipWindow(String[] s){
		Sound.listen.play();
		JTextArea text = new JTextArea();
		if(score >= 0 && score < 90){
			text = new JTextArea(s[0] + "\n" + s[1] +  "\n" + s[2] + "\n" + s[3]);
			scrollPane(text);
		}
		else if (score > 91 && score < 180){
			text = new JTextArea( s[0] + "\n" + s[1] + "\n" + s[2] + "\n" + s[3] + "\n" + s[4]);
			scrollPane(text);
		}
		else if (score > 181 && score < 270){
			text = new JTextArea(s[0] + "\n" + s[1] + "\n" +  s[2] + "\n" + s[3] + "\n" + s[4] + "\n" + s[5]);
			scrollPane(text);
		}
		else if (score > 271 && score < 360){
			text = new JTextArea( s[0] + "\n" + s[1] + "\n" + s[2] + "\n" + s[3] + "\n" + s[4] + "\n" + s[5]+ "\n" + s[6]);
			scrollPane(text);
		}
		else{
			text = new JTextArea(s[0] + "\n" + s[1] + "\n" + s[2] + "\n" + s[3] + "\n" + s[4] + "\n" + s[5]+ "\n" + s[6] + "\n" + s[7]);
			scrollPane(text);
		}
		
		Sound.backGround.loop();
	}
	
	
	// MAkes our text area wrappable and scrollable
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

s[0] = (">Firstly congratulations from group 3 on getting the a score of " + score + "\n\n\n" +
		
	"BSCIS Admission Requirements\n\n" +
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

	// Gets left arrow instructions
	public boolean getLeftArrow() {
		return leftArrow;
	}

	//gets right arrow instructions
	public boolean getRightArrow() {
		return rightArrow;
	}
	
	// Randomly generates a color 
	public static Color randomColorGen() {
		Random rand = new Random();
		int red = rand.nextInt(255);
		int green = rand.nextInt(255);
		int blue = rand.nextInt(255);
		return new Color(red, green, blue);
	}
	
	// Checks to make sure that all the items that require color have not generated the exact same color
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


