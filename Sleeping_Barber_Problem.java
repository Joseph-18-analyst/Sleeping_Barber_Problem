package Sleeping_Barber2;
//import Utilities.*;
import java.util.Random;
//import Synchronization.*;
import java.io.*;
import java.util.*; 
import java.util.concurrent.*;
//import java.util.concurrent.Semaphore;
import java.lang.Object;

 abstract class Semaphore{
	  protected int value = 0;
	  //protected Semaphore() {value = 0;} //constructors
	  protected Semaphore(int initial) {value = initial;}
	  
	  
	}
/*class Getopt extends Object
{
	 int c;
	 String arg;
	 Long[] longopts = new Long[3];
	 // 
	 StringBuffer sb = new StringBuffer();
	 longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
	 longopts[1] = new LongOpt("outputdir", LongOpt.REQUIRED_ARGUMENT, sb, 'o'); 
	 longopts[2] = new LongOpt("maximum", LongOpt.OPTIONAL_ARGUMENT, null, 2);
	//Getopt g = new Getopt("testprog", argv, "ab:c::d");
	 Getopt g = new Getopt("testprog", argv, "-:bc::d:hW;", longopts);
	 g.setOpterr(false); 
	
	 while ((c = g.getopt()) != -1)
	   {
	     switch(c)
	       {
	          case 'a':
	          case 'd':
	            System.out.print("You picked " + (char)c + "\n");
	            break;
	            //
	          case 'b':
	          case 'c':
	            arg = g.getOptarg();
	            System.out.print("You picked " + (char)c +  " with an argument of " +((arg != null) ? arg : "null") + "\n");
	            break;
	            //
	          case '?':
	            break; // getopt() already printed an error
	            //
	          default:
	            System.out.print("getopt() returned " + c + "\n");
	       }
	   }	}*/
 /*abstract class K{
	  protected int value = 0;
	 // protected Semaphore() {value = 0;} //constructors
	 // protected Semaphore(int initial) {value = initial;}
	  public synchronized void P() {
	    value--;
	    if (value < 0)
	      try {wait(); } catch (InterruptedException e) {}
	  }

	  public synchronized void V() {
	    value++; if (value>=0) notify();
	  }
	}*/
class MyObject implements Comparable<MyObject>
{
private String name;
private int id;

public String getName() {
    return name;
}

public MyObject(String name, int id) {
    super();
    this.name = name;
    this.id = id;
}
@Override
public int compareTo(MyObject o) {
    return name.compareTo(o.getName());
}   
@Override
public String toString() {
    return name+"\t"+id+"\n";
}
}
class Barber extends Object implements Runnable {

   private Salon salon = null;

   public Barber(String name, Salon salon) {
	   super();
      this.salon = salon;
      new Thread(this).start();
   }

   public void run() {
      while (true) {
         salon.wantToCut();
      }
   }
}

class Customer extends MyObject implements Runnable
{
	Random randomno = new Random();
	int val = (int) Math.round(randomno.nextGaussian() * 15);
   private int id = 0;
   private Salon salon = null;
   private int cutTime = 0;
 private int growTime = 0;
    private  int napping;
   public Customer(String name, int id, int cutTime, int growTime, Salon salon) 
   {
	   super(name, id);
      this.id = id;
      this.salon = salon;
      this.cutTime = cutTime;
      this.growTime = growTime;
      new Thread(this).start();
   }

   public void run() {
     
      while (true) {
         napping = (int) (1 + randomno.nextGaussian() * 15);
         if(napping>0)
         { if(val>0)
         System.out.println("age=" + val  + ", " + getName() + " growing hair " + napping + "ms");
         }
         try {
			Thread.sleep(3000);
		} 
         catch (InterruptedException e)
         {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         System.out.println("age=" + val + ", " + getName() + " needs a haircut");
         try {
			salon.wantHairCut(id, cutTime);
		} 
         catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
   }
}


class Salon extends Object {
	Random random1 = new Random();
  
	Semaphore waiting_room_mutex = null;
	Semaphore barber_room_mutex = null;
	Semaphore barber_chair_free = null;
	Semaphore sleepy_barbers = null;
	
	//int num_waiting_chairs_free = N;
	private int numChairs = 0;
   private Semaphore customers = null;
   private Semaphore barber = null;
   private Semaphore mutex = null;
   private Semaphore cutting = null;
   private int waiting = 0;
 //private  int value=0;
 Queue <Process> q; 
 Process p;

 
 
	   public  Salon(String name, int numChairs) {
      super();
      this.numChairs = numChairs;
    /*  customers = new Semaphore(0);
      barber = new Semaphore(0);
      mutex = new Semaphore(1);
      cutting = new Semaphore(0);
      waiting_room_mutex = new Semaphore(1);
      barber_room_mutex = new Semaphore(1);
      
      
      sleepy_barbers = new Semaphore(0);*/
      
   }
   protected int value = 0;
   
	 // protected Semaphore() {value = 0;} //constructors
	 // protected Semaphore(int initial) {value = initial;}
	  
  public synchronized void P(Semaphore customers) {
	    value--;
	    if (value < 0)
	      try {wait(); } catch (InterruptedException e) {}
	  }
	  public synchronized void V(Semaphore barber) {
	    value++; if (value>=0) notify();
	  }
   public void wantToCut() {
	   //Random random1 = new Random();
	   System.out.println("age=" + random1.nextGaussian() * 15 + ", Barber free, waiting for a customer");
	  // wait(waiting_room_mutex);
	 while(true)
	   {
		 
		 P(customers);
      P(mutex);
      waiting--;
      V(barber);
      System.out.println("age=" + random1.nextGaussian() * 15+ ", Barber has customer, waiting=" + waiting);
      V(mutex);
      System.out.println("age=" + random1.nextGaussian() * 15+ ", Barber cutting hair");
      P(cutting);
   }}
   

  

public void wantHairCut(int i, int cutTime) throws InterruptedException
{
	
	
      P(mutex);
      if (waiting < numChairs) {
         waiting++;
         System.out.println("age=" + random1.nextGaussian() * 15+ ", Customer " + i + " in room, waiting=" + waiting);
         V(customers);
         V(mutex);
         P(barber);
        // napping = 1 + (int)random(cutTime);
         System.out.println("age=" + random1.nextGaussian() * 15+ ", Customer "+ i + " getting haircut for " + random1.nextGaussian() * 15+ " ms");
         
       
         Thread.sleep(4000);
         System.out.println("age=" +random1.nextGaussian() * 15+ ", Customer " + i + " finished haircut");
         V(cutting);
      } else {
         System.out.println("age=" + random1.nextGaussian() * 15 + ", Customer "+ i + " room full, waiting=" + waiting);
         V(mutex);
      }
   }
}


class SleepingBarber extends Object {

	static Random random2 = new Random();
   public static void main(String[] args) 
   {
	
	   
   
      // parse command line options, if any, to override defaults
      //GetOpt go = new GetOpt(args, "Us:C:c:g:R:");
      //go.optErr = true;
     // String usage = "Usage: -s numChairs -C numCustomers" + " -c cutTime -g growTime -R runTime";
      //int ch = -1;
      int numChairs = 5;
      int numCustomers = 10;
      int cutTime = 2;    // defaults
      int growTime = 4;   // in
      int runTime = 60;   // seconds
     /* while ((ch = go.getopt()) != go.optEOF) {
         if      ((char)ch == 'U') {
            System.out.println(usage);  System.exit(0);
         }
         else if ((char)ch == 's') 
        	 numChairs = go.processArg(go.optArgGet(), numChairs);
         else if ((char)ch == 'C') 
        	 numCustomers = go.processArg(go.optArgGet(), numCustomers);
         else if ((char)ch == 'c') 
        	 cutTime = go.processArg(go.optArgGet(), cutTime);
         else if ((char)ch == 'g') 
        	 growTime = go.processArg(go.optArgGet(), growTime);
         else if ((char)ch == 'R')
            runTime = go.processArg(go.optArgGet(), runTime);
         else {
            System.err.println(usage);  
            System.exit(1);
         }
      }*/
      System.out.println("SleepingBarber: numChairs=" + numChairs+ ", numCustomers=" + numCustomers + ", cutTime=" + cutTime+ ", growTime=" + growTime + ", runTime=" + runTime);

      // create the Salon object
      Salon salon = new Salon("Salon", numChairs);

      new Barber("Barber", salon); // create the Barber

      // create the Customers (self-starting thread)
      for (int i = 0; i < numCustomers; i++)
         new Customer("Customer", i, cutTime*1000, growTime*1000, salon);
      System.out.println("All Customer threads started");

      new Thread();
	// let the Customers run for a while
      try {
		Thread.sleep(1000);
	} 
      catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      System.out.println("age()=" + random2.nextGaussian() * 15+ ", time to stop the Customers and exit");
      System.exit(0);
   }
}
