class Paddle extends Block {
	public boolean moved = false;

	public Paddle() {
		super(Breakout.MAXX / 2 - Breakout.PADDLEWIDTH / 2, 
			Breakout.PADDLEALTITUDE, 
			Breakout.PADDLECOLOR);
		MAXX = Breakout.PADDLEWIDTH;
		MAXY = Breakout.PADDLEHEIGHT;

	}

	// Indicates the location that the paddle is moving to graphically
	public void go(int x0, int y0) {
		x=x0;
		y=y0;
	}
	
	//Indicates if the paddle has been moved or not
	public void move(int change) {
		if((x+MAXX < Breakout.MAXX || change < 0) && (x > 0 || change > 0)) { 
			x = x + change;
			moved = true;
		}
		else
			moved = false;
	}

}
