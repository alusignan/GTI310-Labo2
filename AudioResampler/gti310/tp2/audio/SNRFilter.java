package gti310.tp2.audio;

import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import gti310.tp2.io.FileSink;
import gti310.tp2.io.FileSource;

public class SNRFilter implements AudioFilter {
// variable pour les fichiers a comparer
	private FileSource modifiedFile;
// variable du fichier original	
	private FileSource sourceFile;
//Data apr�s le pop de 44 	
	private short[] dataOri;
	private double[] SNRArray;
	private byte[] DataArraySource, DataArrayModified;
	private byte[] TotalDataSource, TotalDataModified;
	private byte[] HeaderSource, HeaderModified;
// variable du calcul final	
	double calcSNR;
	long calcTotTopFin;
	long calcTotTop;
	long calcTotBot;
	long calcTop;
	long calcBot;
	long calcBot2;

//la liste ou sera stocker les valeurs a comparer	
    List<Double> SNRtbl = new ArrayList<Double>(8);
	
	
	
	public void setSourceFile (FileSource fileSource) {
		this.sourceFile = fileSource;
	}
	
//premier test pour essayer de convertir les donn�es hexad�cimal en entier	
	
//	int byteArrayToInt(byte[] bytes) {
//	     return ((bytes[3] & 0xFF >> 24) |  (bytes[2] & 0xFF >> 16) |(bytes[1] & 0xFF >> 8) | (bytes[0] & 0xFF) );
//       return bytes[0] | (bytes[1] & 0xFF) << 8 | (bytes[2] & 0xFF) << 16 | (bytes[3] & 0xFF << 24);
//	}
	
//lecture des fichiers originaux	
	public void InuputOutputFile(String sourceLocation, String modifiedLocation){ //, String F3, String F4, String F5, String F6, String F7, String F8, String F9, String F0) {
		try {
			sourceFile = new FileSource(sourceLocation);
			modifiedFile = new FileSource(modifiedLocation);
			
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//fonction principale	
	@Override
	public void process() {
		
		int t=0;
		
		//Skip le header source
		HeaderSource = sourceFile.pop(40);
		
//variable qui contient le hexa pour savoir combien il y a de boucle a faire		
		TotalDataSource = sourceFile.pop(4);
		
		// le pop necessaire pour le fichier a compar� et aller directement aux data
		TotalDataModified = modifiedFile.pop(4);
		HeaderModified = modifiedFile.pop(40);
// la variable qui permet de prendre le hexa et le mettre en entier		
		int myDataSizeSource = ByteBuffer.wrap(TotalDataSource).order(ByteOrder.LITTLE_ENDIAN).getInt();
		
		//System.out.println(myDataSizeSource);
		
		calcTotTop =0;
		calcTotBot =0;
		
		for(int i=0; i<myDataSizeSource; i++){ //O(N)
			
			long dataOri2 = 0;
			long calcBot = 0;
			long calcBot2 = 0;
			
//permet de sortir les valeurs une par une pour pouvoir les compar�es
			long dataOri =  (long) (sourceFile.pop(1)[0] & 0xff);
			long dataModif =  (long) (modifiedFile.pop(1)[0] & 0xff);
			
//Tout le calculs qui permettre d'avoir la valeur du SNR			
			dataOri2 = (long) (dataOri*dataOri);
			
			calcBot = (long) (dataOri-dataModif); //O(1)
			calcBot2 = (long) (calcBot*calcBot);  //O(1)
			calcTotTop += (long) (dataOri2);      //O(1)
			calcTotBot += (long) (calcBot2);      //O(1)
			
		}
		t=8;
//calcul final avec les valeurs plus haut
		calcSNR = (double) (10*Math.log10(calcTotTop/calcTotBot)); //O(1)
//ajout de la valeur d'un calcul � la liste
//Calcule bon individuellement mais a plusieurs mauvais r�sultat		
		SNRtbl.add(calcSNR);
//Trie des r�sultats

if(t == SNRtbl.size()){
		for (int i=0; i<SNRtbl.size();i++){     //O(N)
			//System.out.println((SNRtbl.get(i)));
			Collections.sort(SNRtbl);
			System.out.println((SNRtbl.get(i)));
		}
}	
		/*Loop sur les bits 44 et + pour calculer l'�chantillion*/
	}


}
