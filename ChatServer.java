import java.io.*;
import java.net.*;
import java.util.*;
 
public class ChatServer
{ 
   static ArrayList<PrintWriter> sockOuts;
 
   public static void main(String[] args )
   { 
      sockOuts=new ArrayList<PrintWriter>();
      try
      { 
         ServerSocket s = new ServerSocket(4444);
 
         while (true)
         { 
            Socket incoming = s.accept();
            Runnable r = new Handler(incoming, sockOuts);
            Thread t = new Thread(r);
            t.start();
         }
      }
      catch (IOException e)
      { 
         e.printStackTrace();
      }
   }
}
 
class Handler implements Runnable
{
   private Socket incoming;
   ArrayList<PrintWriter> sockOuts;
   public Handler(Socket i, ArrayList<PrintWriter> sockOuts)
   {
      incoming = i;
      this.sockOuts=sockOuts;
   }
 
   public void run()
   { 
      try
      { 
         try
         {           
            Scanner sockIn = new Scanner(incoming.getInputStream());         ;
            sockOuts.add(new PrintWriter(incoming.getOutputStream(), true /* autoFlush */));
             
            while (sockIn.hasNextLine())
            { 
               String line = sockIn.nextLine();   
               for(PrintWriter sockOut:sockOuts)       
                  sockOut.println(line);           
            }
         }
         finally
         {
            incoming.close();
         }
      }
      catch (IOException e)
      { 
         e.printStackTrace();
      }
   }
}
