import java.awt.Color;
import java.awt.Graphics;


class BlockHolder {
	private int blockCount;
	protected static Line[] lines;
	private int TARGETHEIGHT;

	
	public BlockHolder() {
		TARGETHEIGHT = Breakout.TARGETHEIGHT;
		lines = new Line[6];
		prepareBlocks();
	}

	// Called at the end of a game in order to reset all of the block lines.
	public void restart() {
		blockCount = 0;
		for(int i = 0; i < lines.length; i++) {
			lines[i].restart();
			blockCount += lines[i].numberblocks;
		}
	}

	// The initial creation of the lines of blocks and their color.  Note: Not graphically drawn yet.
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


	// Figures out which line the block that was just hit was on.
	public Block whohit(int x, int y) {
		for(int i = 0; i < lines.length; i++) {
			if(lines[i].bottom >= y && lines[i].top <= y) {
				Block hit = lines[i].whohit(x);
				if(hit != null){
					blockCount -= 1;
					scoreTracker(lines[i].color);
				}
				return hit;
			}
		}
		return null;
	}

	// Updates the score according to what line the block that was hit was on
	public void scoreTracker(Color c){
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].color == c && i == 0){
				Breakout.score = Breakout.score + 6;
			}
			else if (lines[i].color == c && i == 1){
				Breakout.score = Breakout.score + 5;
			}
			else if (lines[i].color == c && i == 2){
				Breakout.score = Breakout.score + 4;
			}
			else if (lines[i].color == c && i == 3){
				Breakout.score = Breakout.score + 3;
			}
			else if (lines[i].color == c && i == 4){
				Breakout.score = Breakout.score + 2;
			}
			else if (lines[i].color == c && i == 5){
				Breakout.score++;
			}
			else{}
				
			}
		}
	
	
	// Draws the line of blocks to be hit
	public void draw(Graphics g) {
		for(int i = 0; i < lines.length; i++)
			lines[i].draw(g);
	}

	// Returns the count of blocks that are left to be hit
	public int blocksLeft() {
		return blockCount;
	}
	
}
