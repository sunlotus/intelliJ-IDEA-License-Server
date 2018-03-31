package legend.licenseServer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("resource")
public class Logger {

	static FileWriter fileWriter;
	
	static SimpleDateFormat format;
	static
	{
		
		format = new SimpleDateFormat("yyyy-MM-dd H:m:s");
//		System.out.println(format.format(new Date()));
		
		String logName = format.format(new Date());
		
        //再创建路径下的文件
		File file = new File(logName);
		
		fileWriter = null;
		
        try {
			fileWriter = new FileWriter(file, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
           
		PrintStream printStream = new PrintStream(new OutputStream()
		{
			@Override
			public void write(byte[] data)
			{
				try {
					fileWriter.write(format.format(new Date()) + ":\t" + new String(data));
					fileWriter.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
		    }
			
			@Override
		    public void write(byte[] data, int off, int len)
			{
				try {
					fileWriter.write((len == 1 && data[0] == 10)? "\n": format.format(new Date()) + "\t" + new String(data, off, len));
					fileWriter.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
			
			@Override
			public void write(int arg0) throws IOException {
				
			}
		});
		
//		try {  
//		    out = new PrintStream(System.out, true, "UTF-8");  
//		} catch (UnsupportedEncodingException e) {  
//		    // TODO Auto-generated catch block  
//		    e.printStackTrace();  
//		}  
		System.setOut(printStream);
		System.setErr(printStream);
	}
}
