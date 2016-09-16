
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class Execute {
	
	
	
	/**
	 * The main method for the program.
	 *
	 * @param args Not used
	 */
	public static void main(String[] arges) throws IOException 
	{
		
		File fl;
		int n=0;
		
		IndexBuilder  indexBldr = new IndexBuilder();
		Parser par = new Parser();
		String fileName="person.txt";
		
		Scanner in = new Scanner(System.in);
		System.out.println("Enter 1 for creating the index and 2 to perfotm thr Queries");
		n=in.nextInt();  
		if(n==1)
		{
			
			
		for(int f=18;f<100;f++)
		{
			fl = new File("Index\\" + String.valueOf(f)+".txt");
			fl.delete();
		}
		

		float  memory=0;
		int age = 0;
		
		//-Xms5m -Xmx5m
		
		
		System.out.println("Enter available Memory in MB, minimum is 2, Maximum is 1300");
	    
		while(memory<2 || memory>1300)
		//memory = in.nextInt();       //asks user for memory available
	    
	    memory = 5;
	    System.out.println("Enter filename");
	    //fileName = in.next();			//ask user for Priary Index
		
	    //fileName="person.txt";
	    RandomAccessFile raFile = new RandomAccessFile(fileName, "r");
		
	    long fileSize;			//index size
		double numIORead;    		//estimated number of IO operations to read the file
		
		//long pos=0;				//position of reading from the file
		
		fl = new File(fileName);		
		fileSize =  fl.length();
		
		double readLoad1 = (memory/3)*1000000;			//how much data will be read each time, depends on memory available, -1 to fee space for the program to perform
//		int readLoad1 = (int)(Math.floor((0.5*Runtime.getRuntime().freeMemory())/100)*100);
//		
		int readLoad = ((int)readLoad1 + 50) / 100 * 100;
		
		if(readLoad>fileSize)
			readLoad=(int) fileSize;
		
		
		byte[] bytes = new byte[(int)readLoad];
		
		numIORead =   (fileSize/readLoad);		//estimation on how many reading operations will be performed
		
		System.out.println("An estimated number of "+(fileSize/4000)+" reading operations will be performed to read the file from disk");
		
		System.out.println("creating index folder");
		
		indexBldr.initiate();
		System.out.println("building index....");
		long creatingIndexStartTime = System.nanoTime();
		
		raFile.seek(0);
		
		//indexBldr=null;

		Runtime.getRuntime().gc();
		
		for(int j=0;j<(long) numIORead;j++)	//two loops to read, first to read maximum possible load each time, 
										//the second to read the rest at the end
		{
			raFile.readFully(bytes);
			par.parse(bytes);
			
		}
		
		
		
		if(raFile.getFilePointer()<fileSize)
		{
			bytes = new byte[(int) (fileSize-raFile.getFilePointer())];
			raFile.readFully(bytes);
			par.parse(bytes);
		}
		System.out.println("Index has been build");
		long creatingIndexEndTime = System.nanoTime();
		System.out.println("Time for creating secondary index: "+(creatingIndexEndTime-creatingIndexStartTime)/1000000+" ms");
		System.out.println("An estimated number of "+indexBldr.getNumOOper()+" write-to-disk operations have been performed to build the secondary index");
		bytes=null;
		par.container=null;
		par=null;
		Runtime.getRuntime().gc();
		n=2;
		}
		
		if(n==2)
		{
			do
			{
			int age=0;
			String a;
			System.out.println("Enter the Age, Min is 18, Max is 99");
			 while(age<18 || age>99)
			    	age = in.nextInt();
			 
			 indexBldr.retrieve_rec(age,fileName);
			 int age1,age2;
				String a1;
				System.out.println("Enter the age range for averge salary(Age must be between 18 to 98) \n Lower limit of range:");
				age1=in.nextInt();
			    System.out.println("Upper limit of range");
			    age2=in.nextInt();
			    if(age2<age1)
			    {
			    	System.out.println("Upper limit must be larger than lower limit");
			    }
			    else
			    {
			    	avgSal.retrieve_avg_sal(age1,age2,fileName);
			    } 
			 System.out.println("press 'y' to do another query");
			 a=in.next();
			 if(a.equals("y")==false)
				 System.exit(0);
			 
			
			}while(true);
			
			
			
		}
		
		
//		System.out.println("Done.....");
	   
	    
//	    do
//	    {
//	    
//		    System.out.println("Enter the Age, Min is 18, Max is 99");
//		    
//		   
//		    
//		    long startTime = System.nanoTime();
//		    
//		   // ArrayList<String> records= retrieve_rec(age,fileName);
//		    
//		    long endTime = System.nanoTime();
//		    long secondartIndexDuration = (endTime - startTime); 
//		   
//		    
//		    
//		    int age1,age2;
//		    
//		    System.out.println("Enter the age range for averge salary(Age must be between 18 to 98) \n Lower limit of range: ");
//		    age1=in.nextInt();
//		    System.out.println("Upper limit of range");
//		    age2=in.nextInt();
//		    if(age2<age1)
//		    {
//		    	System.out.println("Upper limit must be larger than lower limit");
//		    }
//		    else
//		    {
//		    	int calls=age2-age1;
//		    	int age3=age1;
//		    	int count=0,salary=0;
//		    	for(int m=0;m<(calls+1);m++){
//		    		age3=age1+m;
//		    		//pos =secondLevelIndex[age3-18];
//		    		int result[]=AvgSal(age3,secondLevelIndex[age3-18],fileName);
//		    		count=count+result[0];
//		    		salary=salary+result[1];
//		    	}
//		    	float avgSalary=(float)salary/(float)count;
//		    	System.out.println("Average Salary of age group "+age1+" and "+age2+" = $"+avgSalary);
//		    }
//			System.out.println("Time for retreiving records using secondary index  "+secondartIndexDuration/1000000+" ms");
//		    
//		    //showRecords(records);
//		    
//		    System.out.println("Press e to Exit, anything else to continue");
//		    if(in.next().equals("e"))
//		    	break;
//		    age=0;
//	    }while(true);
//	    
//		in.close();
//		raFile.close();
//	    
	}
	/**
	 * 
	 * method to retrieve the average salary from the main file using secondary index 
	 * @param age
	 * @param pos as position of the records
	 * @param file name
	 * @throws IOException
	 */
	static int[] AvgSal(int age,long pos,String fileName) throws IOException{
		ArrayList<String> records = new ArrayList<String>();
	    RandomAccessFile raSecIn = new RandomAccessFile("SecondaryIndex.txt", "r");
	    RandomAccessFile raIndex = new RandomAccessFile(fileName, "r");
	    byte[] numRecsArr = new byte[11];
	    
	    raSecIn.seek(pos);
	    raSecIn.readFully(numRecsArr);
	    
	    String numRecsStr = new String(numRecsArr);
	    int numOfRecords = Integer.parseInt(numRecsStr.trim());
	    byte[] recsPtsArr = new byte[numOfRecords*13];
	    
	    raSecIn.readFully(recsPtsArr);
	    String recsPtsStr = new String(recsPtsArr);
	    byte[] RecArr=new byte[10];
	    long ptr;
	    int addSal=0;
	    for (int i = 0; i < Integer.parseInt(numRecsStr.trim()); i++)
	    {
	    	ptr = Long.parseLong(recsPtsStr.substring(i*13,(i+1)*13).trim());
	    	ptr=ptr+41;
	    	raIndex.seek(ptr);
	    	raIndex.readFully(RecArr);
	    	String Rec = new String(RecArr);
	    	addSal=Integer.parseInt(Rec.trim())+addSal;
	    }
		
		raSecIn.close();
	    raIndex.close();
	    return new int[] {numOfRecords, addSal};

	}
	   


	
	static void showRecords(ArrayList<String> recs)
	{
		System.out.println("Number of records is:" + recs.size());
		
		int count=0,salary=0; 
		
		for(int i=0;i<137;i++) // upper boundary
		System.out.print("_");
		System.out.println();
		
		System.out.print("|"+"Sin Number  |   First Name        |   Last Name         |   Age   |   Annual Income|   Address");
		System.out.println("\t \t \t \t \t \t"+" |");
		
		System.out.print("|");// table devision line
		for(int i=0;i<136;i++)
		System.out.print("-");
		System.out.print("|");
		System.out.println();
		
		NumberFormat nf = new java.text.DecimalFormat();// formating the digits
		
		for (String rec : recs) {
			System.out.print("|"+rec.substring(0,9) );
			System.out.print("   |   "+rec.substring(9,24) );
			System.out.print("   |   "+rec.substring(24,39) );
			System.out.print("   |   "+rec.substring(39,41) );
			int sal=Integer.parseInt(rec.substring(41,51));

			int length = (int) Math.log10(sal) + 1;
			
			if(length==5)
			System.out.print("    |      $"+nf.format(sal));
			else if(length==6)
			System.out.print("    |     $"+nf.format(sal));
			
			System.out.println("   |   "+rec.substring(51,100)+"|" );
			
			salary=Integer.parseInt(rec.substring(41, 51)) + salary;
			count++;
		}
			for(int i=0;i<137;i++)
				System.out.print("_");
				
				System.out.println();
				float averageSalary = (float)salary/(float)count;
				System.out.println("Average Salary= $"+nf.format(averageSalary));
				
		
		

	}

}