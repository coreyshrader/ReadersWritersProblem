/**
 * Created by Corey on 4/23/17.
 * Christopher Groppe
 */
public class Reader extends Thread implements Runnable{


    @Override
    public synchronized void run(){
        reader();
    }

    public void reader(){

        do{
            Driver.rmut.acquireUninterruptibly();//wait
            Driver.rcount++;
            System.out.println("Reader wants data.");
            Driver.rmut.release(); //signal

            Driver.wmut.acquireUninterruptibly();//wait

            if(Driver.wcount > 0){//writer active or waiting
                Driver.wmut.release(); // release mut excl
                Driver.wWait.acquireUninterruptibly();//wait
            }else{
                Driver.wmut.release(); // release mut excl
            }//end if

            System.out.println("Reader done did the read.");

            //preform read here
            Driver.rmut.acquireUninterruptibly();//get mutex to update rcount
            Driver.wmut.acquireUninterruptibly();//need to access both counters
            Driver.rcount--; //-1 reader

            System.out.println("Reader leave data.");

            if(Driver.rcount == 0 && Driver.wcount > 0){ //last reader to leave
                Driver.wWait.release();//let waiting writer in and release mutex
                Driver.rmut.release();
                Driver.wmut.release();
            }else{//not last active reader or no writers want in
                Driver.rmut.release();
                Driver.wmut.release();
            }//end if

            Driver.nap();
        }while(true);//end do
    }
}