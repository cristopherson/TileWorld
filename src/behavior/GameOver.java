package behavior;

import jade.core.behaviours.SimpleBehaviour;

public class GameOver extends SimpleBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void action() {
		// TODO Auto-generated method stub
		System.out.println("This is the end my friends");		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return true;
	}

}
