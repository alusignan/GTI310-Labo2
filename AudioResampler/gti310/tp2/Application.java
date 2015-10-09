package gti310.tp2;

import gti310.tp2.audio.ConcreteAudioFilter;

public class Application {

	/**
	 * Launch the application
	 * @param args This parameter is ignored
	 */
	public static void main(String args[]) {
		System.out.println(args[0]);
		if (args[0].equals("Programme1")) {
			System.out.println("Audio Resample project!");
			ConcreteAudioFilter caf = new ConcreteAudioFilter();
			caf.InuputOutputFile(args[1], args[2]);
			caf.process(); 
		}
		else if (args[0].equals("Programme2")) {
			//Écrit ton code ici Benoit Richard Panini
		}
		else {
			System.out.println("Wut!?");
		}
		
	}
}
