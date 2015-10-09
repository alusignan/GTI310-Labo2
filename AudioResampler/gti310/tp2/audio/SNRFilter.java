package gti310.tp2.audio;

import gti310.tp2.io.FileSource;

public class SNRFilter implements AudioFilter {

	private FileSource modifiedFile;
	private FileSource sourceFile;
	private double[] SNRArray;
	private byte[] DataArraySource, DataArrayModified;
	private byte[] TotalDataSource, TotalDataModified;
	private byte[] HeaderSource, HeaderModified;
	
	public void setSourceFile (FileSource fileSource) {
		this.sourceFile = fileSource;
	}
	
	public void setModifiedFile (FileSource fileSource) {
		this.modifiedFile = fileSource;
	}
	
	@Override
	public void process() {
		
		//Skip les 2 headers
		HeaderSource = sourceFile.pop(40);
		HeaderModified = sourceFile.pop(40);
		
		/*Lire les bits 40-44 pour savoir cmb de temps loop*/
		
		/*Loop sur les bits 44 et + pour calculer l'échantillion*/
	}

}
