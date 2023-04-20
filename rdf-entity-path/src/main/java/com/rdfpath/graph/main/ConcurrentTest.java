/**
 * 
 */
package com.rdfpath.graph.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.GraphComp;
import com.rdfpath.graph.model.GraphCompDense;
import com.rdfpath.graph.model.GraphFullNative;
import com.rdfpath.graph.model.GraphNative;
import com.rdfpath.graph.model.GraphNativeFullDense;
import com.rdfpath.graph.model.GraphWrapper3;
import com.rdfpath.graph.model.IGraph;
import com.rdfpath.graph.utils.Utils;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class ConcurrentTest {

	  public static void main(String[] args) throws Exception {
		  if (System.getProperty("subset") == null || System.getProperty("graph") == null) {
				System.out.println("-Dsubset -Dgraph");
				return;
			}		  
		  
		  	String path = "subsets/";
			String[] files = {"subset100000", "subset1000000", "subset10000000", "subset100000000","latest-truthy_small"};//{"subset10000000"};//{"subset100000", "subset1000000", "subset10000000"};
			String end = ".nt.gz";
			String endComp = "_compressed.gz";
			String endNative = "_native.gz";
			
			int [] nodesSize = {92654, 829794, 8159611, 87110322, 99609308};
			int [] maxNodeId = {100000, 1000000, 10000000, 99999996, 117288116};
			int [] edgesSize = {600493, 5977585, 42682387, 652319619, 715906922};
			

			String actFile = System.getProperty("subset");
			String graphName = System.getProperty("graph");
			
			//String actFile = (System.getProperty("graph-data") != null) ?
			//			System.getProperty("graph-data") :
			//			"subset1000000";
			
			String filename = path + actFile;
			
			int index = 0;
			while (index < files.length) {
				if (files[index].equals(actFile)) {
					break;
				}
				index ++;
			};
			
			if (index >= files.length) {
				System.out.println("End, file "+filename+" not found");
				System.exit(0);
				return;
			}
			
			/*
			System.out.println("==    Cargando Grafo   ==\n"+
					(filename + end) + "\n" +
					(filename + endComp) + "\n" +
					(filename + endNative)); 
			*/
			
			IGraph graph;
			
			if (graphName.equals("graphGt")) {
				graph = new Graph(path + files[index] + end, true);
			}
			else if (graphName.equals("graphNative")) {
				graph = new GraphNative(path + files[index] + end, true, edgesSize[index]);
			}
			else if (graphName.equals("graphCompDense")) {
				graph = new GraphCompDense(path + files[index] + endComp, true, nodesSize[index]);
			}
			else if (graphName.equals("graphComp")) {
				graph = new GraphComp(path + files[index] + endComp, true, maxNodeId[index]);
			}
			else if (graphName.equals("graphNativeFull")) {
				graph = new GraphFullNative(path + files[index] + end, path + files[index] + endNative, true, true, edgesSize[index], maxNodeId[index]);
			}
			else if (graphName.equals("graphNativeFullDense")) {
				graph = new GraphNativeFullDense(path + files[index] + end, path + files[index] + endNative, true, true, edgesSize[index], nodesSize[index]);
			}
			else {
				System.out.println("Fail");
				return;
			}

			BufferedReader fileBuff = Utils.readFile(path+files[index]+"_random_pairs.csv",false);
			String line = "";
	        String[] tempArr;
	        line = fileBuff.readLine();
	        tempArr = line.split(";");
	        int [][] ids = new int[100][2];
	        String[] tempArr1;
	        for (int j = 0; j < tempArr.length; j++) {
	        	tempArr1 = tempArr[j].split(",");
	        	ids[j][0] = Integer.parseInt(tempArr1[0]);
	        	ids[j][1] = Integer.parseInt(tempArr1[1]);
	        }
	        
			MemoryMXBean memBean = ManagementFactory.getMemoryMXBean() ;
			System.gc();
			
			ExecutorService executor = Executors.newSingleThreadExecutor();
			Callable<Object> task;
			Future<Object> future;
			
			//WeakReference refExecutor;
			WeakReference refTask;
			WeakReference refFuture;
			MemoryUsage heapMemoryUsage = memBean.getHeapMemoryUsage();
			
			System.out.println("ID;Estructura;Aristas;Memoria;Tiempo;Timeout");
			
			int i = 0;
			while (i < 10) {
				int[] a = ids[i];
				int r = i;
				task = new Callable<Object>() {
				   public Object call() {
					   long memBefore = memBean.getHeapMemoryUsage().getUsed();
					   long startTime = System.currentTimeMillis();
					   GraphWrapper3 gr = new GraphWrapper3(graph);
					   String hasTimeout = "si";
					   try {
						   gr.search(a, 3);
						   hasTimeout = "no";
						} catch (IOException e) {
							//e.printStackTrace();
						} catch (InterruptedException e) {
							//e.printStackTrace();
						}
					   System.out.println(
							   r+";"+
							   graph.getStructName()+";"+
							   gr.totalEdges+";"+
							   (memBean.getHeapMemoryUsage().getUsed() - memBefore)+";"+
							   (System.currentTimeMillis() - startTime)+";"+
							   hasTimeout
							   );
					   return null;
				   }
				};
				future = executor.submit(task);
				try {
				   Object result = future.get(2, TimeUnit.SECONDS);
				} catch (TimeoutException ex) {
				   // handle the timeout
				} catch (InterruptedException e) {
				   // handle the interrupts
				} catch (ExecutionException e) {
				   // handle other exceptions
				} finally {
					//future.cancel(true);
				}
				
				
				System.out.println(
						future.isDone() ? "Fdone2" : "Fnot done2");
				//System.out.println("Inicia Limpieza : " + memBean.getHeapMemoryUsage().getUsed());
				while(!future.isDone()) {
				    //System.out.println("Calculating...");
				    Thread.sleep(300);
				}
				
				refTask = new WeakReference<Object>(task);
				refFuture = new WeakReference<Object>(future);
				System.out.println(
						future.isDone() ? "Fdone3" : "Fnot done3");
				task = null;
				future = null;
				while(refTask.get() != null || refFuture.get() != null) {
					System.gc();
				}
				
				
				//System.out.println("Termina Limpieza: " + memBean.getHeapMemoryUsage().getUsed());
				System.out.println("i++");
				i++;
			}
		  }

		  private static void log(long startTime, String msg) {
		    long elapsedSeconds = (System.currentTimeMillis() - startTime);
		    System.out.format("%1$5sms [%2$16s] %3$s\n", elapsedSeconds, Thread.currentThread().getName(), msg);
		  }
}
