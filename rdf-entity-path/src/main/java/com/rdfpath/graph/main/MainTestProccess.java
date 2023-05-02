package com.rdfpath.graph.main;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class MainTestProccess {
	public static void main (String[] args) throws InterruptedException {
		System.out.println("init, sleep 60 s");
		Thread.sleep(60000);
		System.out.println("End First sleep");
		System.out.println("sleep 12 s");
		Thread.sleep(12000);
		System.out.println("End");
	}
}
