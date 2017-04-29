/**
 * Created by Corey on 4/23/17.
 * Christopher Groppe
 */
public class Writer extends Thread implements Runnable{

    @Override
    public synchronized void run(){
        writer();
    }

    public void writer(){
        do{
            Driver.wmut.acquireUninterruptibly();//get mutex for wcount
            Driver.wcount++;
            System.out.println("Writer want data.");
            Driver.wmut.release();

            if(Driver.wcount > 1 || Driver.rcount > 0){
                Driver.want.acquireUninterruptibly();
            }

            System.out.println("Writer done did the write.");
            //preform write here
            Driver.wmut.acquireUninterruptibly();
            System.out.println("Writer done with data.");
            Driver.wcount--;//writer is done

            if(Driver.wcount > 0){
                Driver.want.release();
            }else{
                Driver.rmut.acquireUninterruptibly();
                while(Driver.rcount > 0){
                    Driver.want.release();//let readers in
                    Driver.rcount--;
                }//end while
            }//end if

            Driver.wmut.release();//reader time
            Driver.rmut.release();//clean up and leave

            Driver.nap();
        }while(true);//end do
    }

}