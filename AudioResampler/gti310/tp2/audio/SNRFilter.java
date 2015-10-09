package gti310.tp2.audio;

import gti310.tp2.io.FileSource;

public class SNRFilter implements AudioFilter {

	private FileSource modifiedFile;
	private FileSource sourceFile;
	private double[] SNRArray;
	
	public void setSourceFile (FileSource fileSource) {
		this.sourceFile = fileSource;
	}
	
	public void setModifiedFile (FileSource fileSource) {
		this.modifiedFile = fileSource;
	}
	
	
	
	@Override
	public void process() {
		
		
		
	}

}
