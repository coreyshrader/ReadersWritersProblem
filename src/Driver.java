/**
 * Created by Corey Shrader & Christopher Groppe on 4/23/17.
 */
import java.util.concurrent.Semaphore;
import java.lang.*;

import java.util.concurrent.ThreadLocalRandom;
public class Driver{
    public  static int rcount, wcount = 0;
    public  static Semaphore rmut = new Semaphore(1);
    public  static Semaphore wmut = new Semaphore(1);
    public  static Semaphore want = new Semaphore(1);
    public  static Semaphore wWait = new Semaphore(0);


    public static void main(String[] args) throws java.lang.Exception{
        for(int i = 0; i < 50; i++){
            int r = ThreadLocalRandom.current().nextInt(1, 4);//gen ran # between 1 and 3
            if(r == 1 || r == 2){//randomly gen a reader or writer, higher chance of reader
                new Thread(new Reader()).start();
            }else{
                new Thread(new Writer()).start();
            }

            if(i == 49){// for loop never makes it to 50, so when i = 49, start a time to run simulation
                long start = System.currentTimeMillis();
                long end = start + 120*1000;
                while(System.currentTimeMillis() < end){}
                System.exit(0); //end simulation
            }
        }

    }
    public static void nap(){//to reduce redudant code
        int st = ThreadLocalRandom.current().nextInt(1,10);//gen ran # between 1 and 10
        try{
            Thread.sleep(st*2000);
        }catch (InterruptedException e){}
    }

}
