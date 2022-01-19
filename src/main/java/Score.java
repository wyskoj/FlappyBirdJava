import com.jme3.scene.Node;

import static com.jme3.scene.Spatial.CullHint.Always;
import static com.jme3.scene.Spatial.CullHint.Dynamic;

/**
 * The score in Flappy Bird is determined by the number of barriers that Faby has passed. The score is displayed
 * on-screen using sprites of the numbers 0 through 9. There is a tens-place and a ones-place. To accurately display the
 * score we store two arrays of sprites each containing each of the numbers. We then show the sprite in the correct
 * array position to show that number.
 */
public class Score {
	
	/**
	 * Context to the main class.
	 */
	private final FlappyBird context;
	
	/**
	 * Contains both the tensNode and the onesNode.
	 */
	final Node scoreNode = new Node("score");
	
	/**
	 * Contains each of the sprites from 0 through 9, used for the tens-place.
	 */
	final Node tensNode = new Node("tens");
	
	/**
	 * Contains each of the sprites from 0 through 9, used for the tens-place.
	 */
	final Node onesNode = new Node("tens");
	
	public Score(FlappyBird context) {
		this.context = context;
		
		for (int i = 0; i <= 9; i++) { // For each digit from 0 to 9
			tensNode.attachChild(context.getSprite(i + ".png"));
			onesNode.attachChild(context.getSprite(i + ".png"));
			
			/* To begin with, we just want the "0"s to be visible. */
			if (i == 0) {
				tensNode.getChild(i).setCullHint(Dynamic);
				onesNode.getChild(i).setCullHint(Dynamic);
			} else {
				tensNode.getChild(i).setCullHint(Always);
				onesNode.getChild(i).setCullHint(Always);
			}
		}
		
		/* Attach the tensNode and the onesNode to the scoreNode */
		scoreNode.attachChild(tensNode);
		scoreNode.attachChild(onesNode);
		
		/* The onesNode needs to move to the right a bit, so it is right of the tensNode. */
		onesNode.move(24, 0, 0);
		
		/* Position the score */
		scoreNode.move(context.centerX() - 24, context.centerY() + 100, 100);
		
		context.getGuiNode().attachChild(scoreNode);
	}
	
	public void tick() {
		/* Update the visibility of each number in both the ones and tens nodes. */
		for (int i = 0; i <= 9; i++) {
			if (context.score() / 10 == i) { // To get the tens-place, we use integer division
				tensNode.getChild(i).setCullHint(Dynamic);
			} else {
				tensNode.getChild(i).setCullHint(Always);
			}
			
			if (context.score() % 10 == i) { // To get the tens-place, we use modulus (remainder)
				onesNode.getChild(i).setCullHint(Dynamic);
			} else {
				onesNode.getChild(i).setCullHint(Always);
			}
		}
	}
}
