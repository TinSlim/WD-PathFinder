package com.rdfpath.graph.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import com.rdfpath.graph.utils.Utils;

public abstract class AbstractGraph implements IGraph {
	
	long startTime = System.currentTimeMillis();
	long actualTime = System.currentTimeMillis();
	long timeA = System.currentTimeMillis();
	int minute = 0;
	
	public void printMemory () {
		double maxHeapSize = Runtime.getRuntime().maxMemory();
		double kbSize = maxHeapSize / 1024;
		double mbSize = kbSize / 1024;
		double gbSize = mbSize / 1024;
		
		System.out.println("HeapSize:" + maxHeapSize);
		System.out.println("HeapSize kB:" + kbSize);
		System.out.println("HeapSize mB:" + mbSize);
		System.out.println("HeapSize gB:" + gbSize);
	}
	
	public BufferedReader readFile (String filename, Boolean isGz) throws IOException {
		if (isGz) {
			FileInputStream stream = new FileInputStream(filename);
			GZIPInputStream gzip = new GZIPInputStream(stream);
			return new BufferedReader(new InputStreamReader(gzip));
		}
		FileInputStream stream = new FileInputStream(filename);
		return new BufferedReader(new InputStreamReader(stream));
	}
	
	public void sendNotificationTime (int perMinutes, String data) {
		if ((((timeA - actualTime)/1000) / 60) >= perMinutes) {
			actualTime = timeA;
			minute += perMinutes;
			sendNotification (data);
		}
	}

	public void sendNotification (String data) {
		System.out.println("Minutos: " + minute);
		System.out.println(data);
		if (System.getProperty("tg-token") != null && System.getProperty("tg-user") != null) {
			try {
				Utils.peticionHttpGet("https://api.telegram.org/bot"+System.getProperty("tg-token") + "/sendMessage?chat_id=\"+System.getProperty(\"tg-user\")+\"&text=Minutos:"+ minute + " " + data);
			
			} catch (Exception e) {
			}
		}
	}
}