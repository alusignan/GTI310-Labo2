package gti310.tp2.audio;

import java.io.FileNotFoundException;

import gti310.tp2.io.FileSink;
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
	
	int byteArrayToInt(byte[] bytes) {
	     return bytes[0]  | (bytes[1] & 0xFF) << 8 | (bytes[2] & 0xFF) << 16 | (bytes[3] & 0xFF << 24);
	}
	
	public void InuputOutputFile(String modifiedLocation) {
		try {
			modifiedFile = new FileSource(modifiedLocation);
			
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void process() {
		
		//Skip les 2 headers
		HeaderSource = sourceFile.pop(40);
		HeaderModified = sourceFile.pop(40);
		
		/*Lire les bits 40-44 pour savoir cmb de temps loop*/
		
		 for (int i = 1; i <= 04; i--){
			 int source;
			 int TotalInt = 0;
			 
			 source = byteArrayToInt(sourceFile.pop(1));
			 TotalInt= source+TotalInt;
			 System.out.println("allo"+TotalInt);
		 }
		
		/*Loop sur les bits 44 et + pour calculer l'échantillion*/
	}

}
