import static java.nio.file.StandardOpenOption.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

/**
 * Index Builder
 * Builds index
 *
 * @author Ahmad
 */
public class IndexBuilder {

	File file = new File("index");
	int numOOper;
	

	/**
	 * @return An integer of number of disk input operations 
	 */
	int getNumOOper()
	{
		return numOOper;
	}
	
	
	/**
	 * Creates empty indexes files preparing to build and write them.
	 */
	void initiate() throws IOException
	{
		file = new File("Index");
		file.mkdir();
		numOOper+=1;
		
		for(int f=18;f<100;f++)
		{
			file = new File("Index\\" + String.valueOf(f)+".txt");
			file.createNewFile();
			numOOper+=1;
		}
		
		
	}
	
	public byte[] longToBytes( final long number ) {
	    ByteBuffer bbb = ByteBuffer.allocate(10); 
	    bbb.putLong(number); 
//	    for(int i = String.valueOf(number).length(); i<10; i++)		//padding the byte with white spaces to be 10 bytes
//	    	bb.putChar(' ');
	    return bbb.array();
	} 
	
	
	
	
	/**
	 * creates partial indexes of each age, each index contains pointers to the records associated with this age 
	 * @return boolean
	 * @param Map relation of Ages as Keys and a set of pointers to associated records as Value
	 */
	@SuppressWarnings("null")
	boolean write2Disk( Map<Short, HashSet<Long>> container) throws IOException
	{
		
		
		PrintWriter out;
		//ByteBuffer bb = ByteBuffer.allocate(container. size()*1000) ;
		//RandomAccessFile raFile;
		//BufferedWriter bw;
		StringBuilder sb = new StringBuilder("");
		
		for(short f=18;f<99;f++)
		{
			
			
			if(container.get(f) != null)
			{
			for(long address  :  container.get(f))
			{
				
				sb.append(String.format("%-10s", String.valueOf(address)));
			}
			
			out = new PrintWriter(new BufferedWriter(new FileWriter("Index\\"+String.valueOf(f)+".txt", true)));
			
			
			numOOper +=  (container.get(f).size()/4000)  ;
			 
			out.append(sb);
			
			
			sb.setLength(0);
			
			out.close();
			}
		}
		
		//container.clear();
		return true;
		
	}
	
	
	/**
	 * method to retrieve the records from the main file using secondary index 
	 * @param age
	 * @param pos as position of the records
	 * @param file name
	 * @throws FileNotFoundException 
	 * @throws IOException
	 */
	 void retrieve_rec(int age, String fileName )throws IOException 
	{
		ArrayList<String> records = new ArrayList<String>();
	    char[] record = new char[100];
	    byte[] bytePos=new byte[10];
	    byte[] fullRecord = new byte[100];
	    long pos;
		File fl = new File("index\\"+age+".txt");

		File mainFl = new File(fileName);
	    long numRecs = fl.length()/10;
	    
	    int readLoad1 = (int)(Math.floor((0.5*Runtime.getRuntime().freeMemory())/100)*100);   //0.5 i.e. allocate 50% memory to readLoad 
		
		int readLoad = ((int)readLoad1 + 50) / 100 * 100;   //round to 100
	    
		long numIORead =   (fl.length()/readLoad);
//		System.out.println("numIO->"+numIORead);
//		System.out.println("read load->"+readLoad);
//		System.out.println("file size"+fl.length());
//		long count=0;
	    RandomAccessFile raFile = new RandomAccessFile(fl,"r");
	    RandomAccessFile raMailFile=new RandomAccessFile(mainFl, "r");
		Long ptr;
	    byte[] bytes = new byte[readLoad];
	    
	    for(int j=0;j<(long) numIORead;j++)
	    {
	    	raFile.readFully(bytes);
	    	showRecords(bytes, fileName, age);
	    }
	    
	    if(raFile.getFilePointer()<fl.length())
		{
			bytes = new byte[(int) (fl.length()-raFile.getFilePointer())];
			raFile.readFully(bytes);
			showRecords(bytes, fileName, age);
		}
	    
	    /**
	    
	    for(int j=0;j<numRecs;j++)  simple showing
	    {
	    	raFile.readFully(bytePos);
			pos=Long.parseLong(new String(bytePos).trim());
//			System.out.println(pos);
			raMailFile.seek(pos);
			raMailFile.read(fullRecord);
			System.out.println(new String(fullRecord));
			
//		count++;
	    }
	    
	**/    
	    
	    System.out.println("total recs->"+numRecs);
		////////////////////////////////////////////////////// start here
//	    for(int j=0;j<(long) numIORead;j++)		//read readLoad bytes each time	
//			{
//			raFile.readFully(bytes);
//			
//			for(int i=0 ; i<bytes.length ; i+=10 )
//			{
//				raFile.readFully(bytePos);
//				System.out.println(new String(bytePos));
//				
//				
//			}
//			System.out.println("");
//			
//			}
//			
//			if(raFile.getFilePointer()<fl.length())
//			{
//			bytes = new byte[(int) (fl.length()-raFile.getFilePointer())];
//			raFile.readFully(bytes);
//			
//			}
	    
		
//			while(bytes.length <-1)
//			{
//				raFile.readFully(bytePos);
//				System.out.println(new String(bytePos));
//			}
	   
	    
	    
	    
	    
	   
//	    for (int i = 0; i < Integer.parseInt(numRecsStr.trim()); i++)
//	    {
//	    	ptr = Long.parseLong(recsPtsStr.substring(i*13,(i+1)*13).trim());
//	    	
//	    	raIndex.seek(ptr);
//	    	raIndex.readFully(RecArr);
//		    String Rec = new String(RecArr);
//		    records.add(Rec);
//	    	
//	    }
//		
//		raSecIn.close();
	    raFile.close();
	    raMailFile.close();
//		return records;

	}


	private void showRecords(byte[] bytes, String fileName, int age) throws IOException {

		File fl = new File("index\\"+age+".txt");

		File mainFl = new File(fileName);
		RandomAccessFile raFile = new RandomAccessFile(fl,"r");
	    RandomAccessFile raMailFile=new RandomAccessFile(mainFl, "r");
	    
	    byte[] bytePos=new byte[10];
	    byte[] fullRecord = new byte[100];
	    long pos;

		for(long i =0 ; i <bytes.length ; i+=10)
		{
			raFile.readFully(bytePos);
			pos=Long.parseLong(new String(bytePos).trim());
//			System.out.println(pos);
			raMailFile.seek(pos);
			raMailFile.read(fullRecord);
			System.out.println(new String(fullRecord));

		}
		
		raFile.close();
	    raMailFile.close();
	   
	}
	
	
	/**
	 * merges all partial indexes into one Secondary Index
	 * @return Second Level Index, pointer to start of records of each age in Secondary Index
	 */
//	long[] mergeFiles() throws IOException
//	{
//		int accumulator=0; 
//		long[] secondLevelIndex = new long[81];
//		
//		Path outFile=  Paths.get("SecondaryIndex.txt");
//	    //System.out.println("TO "+outFile);
//	    try(FileChannel out=FileChannel.open(outFile, CREATE, WRITE)) {
//	      for(int ix=18, n=99; ix<n; ix++) {
//	    	  File ptId = new File("Index\\"+String.valueOf(ix)+".txt");
//	    	  secondLevelIndex[ix-18] = accumulator;
//	    	  accumulator += ptId.length();
//	    	  Path inFile=Paths.get(ptId.getAbsolutePath());
//	        //System.out.println(inFile+"...");
//	        try(FileChannel in=FileChannel.open(inFile, READ)) {
//	          for(long p=0, l=in.size(); p<l; )
//	            p+=in.transferTo(p, l-p, out);
//	        }
//	      }
//	    }
//	    System.out.println("Secondary Index has been built");
//	    return secondLevelIndex;
//	}
//	
//	
	
	}
