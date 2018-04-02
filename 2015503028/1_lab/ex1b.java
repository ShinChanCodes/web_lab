    import java.io.*;  
    import java.net.*;  
    public class ex1b{  
		public static void main(String[] args){  
			try{  
				InetAddress ip=InetAddress.getByName("www.google.com");  
				InetAddress Myip=InetAddress.getLocalHost();    
				System.out.println("Host Name: "+ip.getHostName());  
				System.out.println("IP Address: "+ip.getHostAddress());
				System.out.println("My IP Address: "+Myip);  
			}catch(Exception e){System.out.println(e);}  
		}  
    }  
