package gti310.tp2.audio;

import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import gti310.tp2.io.FileSink;
import gti310.tp2.io.FileSource;

public class SNRFilter implements AudioFilter {

	private FileSource modifiedFile;
	private FileSource sourceFile;
	private short[] dataOri;
	private double[] SNRArray;
	private byte[] DataArraySource, DataArrayModified;
	private byte[] TotalDataSource, TotalDataModified;
	private byte[] HeaderSource, HeaderModified;
	short calcSNR;
	short calcTotTop;
	short calcTotBot;
	short calcTop;
	short calcBot;
	short calcBot2;
	
	
	
	public void setSourceFile (FileSource fileSource) {
		this.sourceFile = fileSource;
	}
	
	public void setModifiedFile (FileSource fileSource) {
		this.modifiedFile = fileSource;
	}
	
	int byteArrayToInt(byte[] bytes) {
	     return ((bytes[3] & 0xFF >> 24) |  (bytes[2] & 0xFF >> 16) |(bytes[1] & 0xFF >> 8) | (bytes[0] & 0xFF) );
		 //return bytes[0] | (bytes[1] & 0xFF) << 8 | (bytes[2] & 0xFF) << 16 | (bytes[3] & 0xFF << 24);
	}
	
	public void InuputOutputFile(String sourceLocation, String modifiedLocation) {
		try {
			sourceFile = new FileSource(sourceLocation);
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
		HeaderModified = modifiedFile.pop(40);
		
		/*Lire les bits 40-44 pour savoir cmb de temps loop*/
		
		//Plus une boucle avec celui là je crois
		TotalDataSource = sourceFile.pop(4);
		//int myDataSizeSource = byteArrayToInt(TotalDataSource);
		int myDataSizeSource = ByteBuffer.wrap(TotalDataSource).order(ByteOrder.LITTLE_ENDIAN).getInt();
		
		//Pas sur que tu as besoin de boucler avec celui là
		TotalDataModified = modifiedFile.pop(4);
		int myDataSizeModified = ByteBuffer.wrap(TotalDataModified).order(ByteOrder.LITTLE_ENDIAN).getInt();
		
		System.out.println(myDataSizeSource);
		System.out.println(myDataSizeModified);
		
		for(int i=0; i<myDataSizeSource; i++){
			
			short dataOri2 = 0;
			short calcBot = 0;
			short calcBot2 = 0;
			
			//dataOri[i] = (short) (sourceFile.pop(1)[0] & 0xff);
			short dataOri =  (short) (modifiedFile.pop(1)[0] & 0xff);
			short dataModif =  (short) (modifiedFile.pop(1)[0] & 0xff);
			
			dataOri2 = (short) (dataOri*dataOri);
			
			calcTop += (short)  dataOri2;
			calcBot = (short) (dataOri-dataModif);
			calcBot2 = (short) (calcBot*calcBot);
			calcTotTop += (short) (calcTop);
			calcTotBot += (short) (calcBot2);
			System.out.println(i);
			
		}
		System.out.println(calcTotTop);
		System.out.println(calcTotBot);
		calcSNR = (short) (10*Math.log10(calcTotTop/calcTotBot));
		System.out.println(calcSNR);
		/*Loop sur les bits 44 et + pour calculer l'échantillion*/
	}

}
