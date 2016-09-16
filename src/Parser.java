
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.HashSet;
import java.util.Map;

/**
 * Parser
 * Purpose: Parse the age and positions associated with it into a Hash Map container
 *
 * @author Ahmad
 */
	public class Parser 
	{
		
		byte[] ageArr = new byte[2];
		Map<Short, HashSet<Long>> container = new HashMap<Short, HashSet<Long>>();
		IndexBuilder indxBldr = new IndexBuilder();
		Short age;
		int ageX = 39, ageY = 40; 
		long rPos = 0;
		
		
	/**
	 * Parses the age and positions associated with it into a Hash Map container
	 *
	 * @return A Map relation of Age as key and associated records as value
	 * @param data: raw data to get age and records from
	 * @throws IOException 
	 */
		void parse(byte[] bytes) throws IOException
		{
			Map<Short, HashSet<Long>> container = new HashMap<Short, HashSet<Long>>();
			
			for(long i =0 ; i+80 <bytes.length ; i+=100)
			{
				
				
				ageArr[0] = bytes[ageX];
				ageArr[1] = bytes[ageY];
				age= Short.valueOf(new String(ageArr));
				//add(container, age, rPos);
//				if(container.get(age)==null)
//				{
//				
//				container.put(age, new HashSet<Long>());
//				}
//				HashSet<Long> myArray = container.get(age);
//				myArray.add(rPos);
				add(container,age, rPos);
				ageX += 100;
				ageY += 100;
				rPos += 100;
			}
			
			indxBldr.write2Disk(container);
			
			
			age=0;
			ageX = 39; ageY = 40; 
			container.clear();
			container=null;
		}
	
	
	/**
	 * secondary method to add more than one value (record) to an Age key 
	 * @param A Map relation of (age) as key and associated records as value, new key, new value
	 */
	void add(Map<Short, HashSet<Long>> container, Short key, long value) 
	{
			HashSet<Long> values = container.get(key);
			if (values == null) {
				values = new HashSet<Long>();
			}
			values.add(value);
			container.put(key, values );
			
		}
	
	/**
	 * Convert long to bytes array 
	 * @param A Map relation of (age) as key and associated records as value, new key, new value
	 */
	
	public byte[] longToBytes( final long number ) {
	    ByteBuffer bb = ByteBuffer.allocate(10); 
	    bb.putLong(number); 
//	    for(int i = String.valueOf(number).length(); i<10; i++)		//padding the byte with white spaces to be 10 bytes
//	    	bb.putChar(' ');
	    return bb.array();
	} 
	
		
	}
