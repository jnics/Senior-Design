import java.awt.Color;
import java.awt.Graphics;

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
	

	// Called to fill the blocks in for eaach line
	public void fill() {
		for(int i = 0; i < numberblocks; i++) {
			blocks[i] = new Block(TARGETLENGTH * i + initial, top + 1, color);
			exists[i] = true;
		}
	}

	// Called after a game in order to set the lines of blocks for a new game
	public void restart() {
		for(int i = 0; i < numberblocks; i++) {
			exists[i] = true;
		}
	}

	// Actually draws the blocks for each line.
	public void draw(Graphics g) {
		for(int i = 0; i < numberblocks; i++) {
			if(exists[i])
				blocks[i].draw(g);
		}
	}
	
	// Changes the block if it have been hit in the specific line.  Now it will need to be cleared
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
