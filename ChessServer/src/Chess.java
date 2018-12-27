import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chess {
	static int[] str_to_vec(String str) {
	    int[] result = new int[90];
	    String[] s = str.split("\\.");
	    for(int i = 0; i <90; i++)
	    	result[i] = Integer.parseInt(s[i]);
	    return result;
	}
	
	 public static void doPost(String message,OutputStream op,byte [] buf) throws IOException{
	   AI m_AI = new AI();
		 
	   String str="";
	   Pattern p = Pattern.compile("\"[0-1]chess:[0-9]*.*\"");
	   Matcher m = p.matcher(message);
	   m.find();
	   str = m.group();
	   System.out.println("�յ����");
	   int[] board = str_to_vec(str.substring(8, str.length()-2));
	   m_AI.m_print(board);
	   node root = m_AI.a_b(board, Integer.parseInt(str.substring(1,2)));
	   int[] result = str_to_vec(root.choose).clone();
	   OutputStreamWriter osw = new OutputStreamWriter(op,"utf-8");
       osw.write("HTTP/1.1 200 OK\r\n");
       osw.write("Content-Type: text/plain;charset=UTF-8\r\n");
//       osw.write("Transfer-Encoding: chunked\r\n");
////       osw.write("Date: Tue, 19 May 2015 02:48:27 GMT\r\n");
       osw.write("\r\n");
//       osw.write("c9\r\n");
       System.out.println("���ؽ��");
       m_AI.m_print(result);
       osw.write(vec_to_str(result));
       osw.flush();
       osw.close();
       op.flush();
       op.close();
       
	 }
	    static String vec_to_str(int[] v) {
	        String result = "";
	        for(int i = 0; i < v.length; i++)
	            result = result + v[i] + ".";
	        return result;
	    }
    public static void main(String[] args)  throws IOException{
    	
//        int[] board = { // ���ڿ���
//                1, 3, 5, 7,16, 8, 6, 4, 2,
//                0, 0, 0, 0, 0, 0, 0, 0, 0,
//                0, 9, 0, 0, 0, 0, 0,10, 0,
//                11, 0,12, 0,13, 0,14, 0,15,
//                0, 0, 0, 0, 0, 0, 0, 0, 0,
//                0, 0, 0, 0, 0, 0, 0, 0, 0,
//                27, 0,28, 0,29, 0,30, 0,31,
//                0,0, 0, 0, 25, 0, 0,26, 0,
//                0, 0, 0, 0, 0, 0, 0, 0, 0,
//                17,19,21,23,32,24,22,20,18
//        };
        ServerSocket serverSocket = new ServerSocket(8099);  
        while(true){
	        Socket socket = serverSocket.accept(); 
	        InputStream is = socket.getInputStream();
	        OutputStream out = socket.getOutputStream();
	        int len = 0;
	        byte [] buf = new byte[1024];
	        String message = "";
	        while((len=is.read(buf))!=-1){
	            message += new String(buf,0,len);
	            break;
	        }
	        if(message.contains("POST")&& message.contains("chess")){
	            doPost(message,out,buf);
	        }
	        else if(message.contains("GET")) {
	        	 OutputStreamWriter osw = new OutputStreamWriter(out,"utf-8");
	             
	             
	             
	             osw.write("HTTP/1.1 200 OK\r\n");
	             osw.write("Server: Apache-Coyote/1.1\r\n");
	             //osw.write("Set-Cookie: JSESSIONID=03493794995CE31A0F131787B6C6CBB2; Path=/; HttpOnly\r\n");
	             osw.write("Content-Type: text/html;charset=UTF-8\r\n");
	             osw.write("Transfer-Encoding: chunked\r\n");
	             osw.write("Date: Tue, 19 May 2015 02:48:27 GMT\r\n");
	             osw.write("\r\n");
	             osw.write("c9\r\n");
	             osw.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\r\n");
	             osw.write("<HTML>\r\n");
	             osw.write("  <HEAD><TITLE>A Servlet</TITLE></HEAD>\r\n");
	             osw.write("  <BODY>\r\n");
	             
	             osw.write("  </BODY>\r\n");
	             osw.write("</HTML>\r\n");
	             osw.write("\r\n");
	             //osw.write("0");
	             osw.write("\r\n");
	             osw.write("\r\n");
	             osw.flush();
	             osw.close();
	             out.flush();
	             out.close();
	        }
	        is.close();
       }
    }
}
