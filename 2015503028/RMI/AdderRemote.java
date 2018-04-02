    import java.rmi.*;  
    import java.rmi.server.*;  
    public class AdderRemote extends UnicastRemoteObject implements mathInterface{  
    public AdderRemote() throws RemoteException
	{
		System.out.println("Initializing Server");
 
    }
    public int add(int a,int b)
    {
		return(a+b);
	}
	public int subt(int a,int b)
	    {
			return(a-b);
	}
	public int mult(int a,int b)
	    {
			return(a*b);
	}
	public int div(int a,int b)
	    {
			return(a/b);
	}  
    }  