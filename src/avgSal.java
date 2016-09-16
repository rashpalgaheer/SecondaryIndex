import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class avgSal {
	static long avg_sal=0;
	static int count=0;
	public static void retrieve_avg_sal(int age1, int age2, String fileName) throws IOException {
		//ArrayList<String> records = new ArrayList<String>();
	    char[] record = new char[100];
	    byte[] bytePos=new byte[10];
	    byte[] fullRecord = new byte[100];
	    long pos;
	    
	    int calls=age2-age1;
    	//int[] result={0,0};
    	//int count=result[0];
    	//int salary=result[0];
    	for(int m=0;m<(calls+1) && age1<=age2 ;m++,age1++)
    	{
    		File fl = new File("index\\"+age1+".txt");

    		File mainFl = new File(fileName);
    	    long numRecs = fl.length()/10;
    	    
    	    int readLoad1 = (int)(Math.floor((0.5*Runtime.getRuntime().freeMemory())/100)*100);   //0.5 i.e. allocate 50% memory to readLoad 
    		
    		int readLoad = ((int)readLoad1 + 50) / 100 * 100;   //round to 100
    	    
    		long numIORead =   (fl.length()/readLoad);
    		 RandomAccessFile raFile = new RandomAccessFile(fl,"r");
    		 RandomAccessFile raMailFile=new RandomAccessFile(mainFl, "r");
    		 Long ptr;
    		    byte[] bytes = new byte[readLoad];
    		    
    		    for(int j=0;j<(long) numIORead;j++)
    		    {
    		    	raFile.readFully(bytes);
    		    	//showRecords(bytes, fileName, age1);
    		    	avg_sal_show(bytes, fileName, age1);
    		    	//result[0]=count;
    		    	//result[1]=salary;
    		    	//count=result[0];
    		    	//salary=result[1];
    		    }
    		    
    		    if(raFile.getFilePointer()<fl.length())
    			{
    				bytes = new byte[(int) (fl.length()-raFile.getFilePointer())];
    				raFile.readFully(bytes);
    				//showRecords(bytes, fileName, age1);
    				avg_sal_show(bytes, fileName, age1);
    				
    			}
    	}
    	
    	int avgs=(int)(avg_sal/count);
    	System.out.println("Average Salary:$ "+avgs);
	}
    private static void avg_sal_show(byte[] bytes, String fileName, int age1) throws IOException
    {
    	File fl = new File("index\\"+age1+".txt");

		File mainFl = new File(fileName);
		RandomAccessFile raFile = new RandomAccessFile(fl,"r");
	    RandomAccessFile raMailFile=new RandomAccessFile(mainFl, "r");
	    
	    byte[] bytePos=new byte[10];
	    byte[] fullRecord = new byte[100];
	    long pos;
	    int k=41;
	    int o=51;
		for(long i =0; i <bytes.length ; i+=10)
		{
			
			raFile.readFully(bytePos);
			String recsPtsStr = new String(bytePos);
			pos=Long.parseLong(new String(bytePos).trim());
			//System.out.println(pos);
			//pos = Long.parseLong(recsPtsStr.substring(i*13,(i+1)*13).trim());
	    	//pos=pos+39;
			raMailFile.seek(pos);
			raMailFile.read(fullRecord);
			count++;
			String rec1=new String(fullRecord);
			long avgsal2=Long.parseLong(rec1.substring(k,o));
			avg_sal+=avgsal2;
		}
		
		raFile.close();
	    raMailFile.close();	
   	}
  
}

	
