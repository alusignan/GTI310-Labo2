package gti310.tp2;

import gti310.tp2.audio.ConcreteAudioFilter;
import gti310.tp2.audio.SNRFilter;

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
			System.out.println(args[0]);
			if (args[0].equals("Programme2")) {
				System.out.println("SNR project!");
				SNRFilter snr = new SNRFilter();
				snr.InuputOutputFile(args[1],args[2]);
				snr.process();
		}
		else {
			System.out.println("Wut!?");
		}
		
	}
}
}