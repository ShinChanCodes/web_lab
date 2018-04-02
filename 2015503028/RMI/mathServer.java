import java.rmi.*;
import java.rmi.Naming.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.net.*;
import java.util.*;

public class mathServer
{
	public static void main(String args[])
	{
		try
		{
		mathInterface ms=new AdderRemote();
		java.rmi.Naming.rebind("rmi://localhost:5000/aslam",ms);
		System.out.println("Server Ready");
	    }
	    catch(RemoteException RE)
	    {
			System.out.println("Remote Server Error:"+ RE.getMessage());
			System.exit(0);
		}
		catch(MalformedURLException ME)
		{
			System.out.println("Invalid URL!!");
		}
	}
}
