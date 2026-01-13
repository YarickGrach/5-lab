public class OddRunnable implements Runnable{
    @Override
    public void run(){
        try{
            for (int i = 1; i<= 9; i += 2){
                System.out.println(Thread.currentThread().getName() + ": " + i);
                Thread.sleep(500);
            }
        } catch (InterruptedException e){
            System.out.println("OddRunnable был прерван");
        }
    }
}
