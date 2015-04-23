import java.applet.AudioClip;
import java.net.URL;

class Sound extends Breakout{
	
	static AudioClip backGround, hit, miss, win, endGame, listen; // Sound player
	URL url;
	static Breakout bo;

	// Main control center of the sounds that are played throughout the game.  
	// All sounds are stored in a Music folder in the src folder of the program for reference.
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
		miss = bo.getAudioClip(url, "Music/G3Miss.au");
	}
	

}
