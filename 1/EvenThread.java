import javax.swing.plaf.TableHeaderUI;

public class EvenThread extends Thread{
    @Override
    public void run(){
        try{
            for (int i = 2; i <= 10; i += 2){
                System.out.println(Thread.currentThread().getName() + ": " + i);
                Thread.sleep(500);
            }
        } catch (InterruptedException e){
            System.out.println("EventThread был прерван");
        }
    }
}
