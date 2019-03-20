import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RunnableDemo {

    public static void main(String[] args) {
        System.out.println("Running one Counter as a thread");
        
        Runnable task1 = new BlockingCounter();
        Runnable task2 = new NonBlockingCounter();
        
        // Executors typically manage a pool of threads, so we don't have to 
        // create new threads manually. In this example we use an executor with
        // a thread pool of size one.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(task1);
        
        try {
            executor.shutdown();
            executor.awaitTermination(3, TimeUnit.SECONDS);
        }
        catch (InterruptedException ex){
            Logger.getLogger("ThreadDemo").log(Level.SEVERE, null, ex);
        }
        finally {
            if (!executor.isTerminated()){
                executor.shutdownNow();
            }
        }
                
        
    }
}

class BlockingCounter implements Runnable {

    private int i = 0;

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("ThreadId: "
                        + Thread.currentThread().getId() + " Count: " + i++);
                TimeUnit.MILLISECONDS.sleep(100);
            }
        // A thread that can block must handle InterruptedException.
        } catch (InterruptedException ex) {
            System.out.println("Thread " + Thread.currentThread().getId() +  
                    " was stopped.");
        } finally {
            // cleanup code
        }
    }
}

class NonBlockingCounter implements Runnable {

    private int i = 0;

    @Override
    public void run() {
        // A non-blocking thread can check if it has been interrupted by
        // calling isInterrupted().
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("ThreadId: "
                    + Thread.currentThread().getId() + " Count: " + i++);
        }        
    }
}

