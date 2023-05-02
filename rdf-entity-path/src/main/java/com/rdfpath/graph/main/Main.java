/**
 * 
 */
package com.rdfpath.graph.main;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Callable<String> stringCallable = () -> {
            //Thread.sleep(3000);
            while (!Thread.interrupted()) {
            }
            System.out.println("Interrumpido");
            int x = -999999999;
            while(x < 999999999) {
            	x++;
            }
            System.out.println("Interrumpido2");
            return "hello edpresso";
        };

        Future<String> stringFuture = executorService.submit(stringCallable);

        long timeOut = 9;

        TimeUnit timeUnit = TimeUnit.SECONDS;

        String result = "null";
        try {
        	result = stringFuture.get(timeOut, timeUnit);
        }
        catch (Exception e) {
            //e.printStackTrace();
        	stringFuture.cancel(true); //this method will stop the running underlying task

        }
        System.out.println("Retrieved result from the task - " + result);
        //Thread.sleep(1000);
        System.out.println("End- " + result);
        executorService.shutdownNow();
        System.out.println("End2- " + result);
    }
}