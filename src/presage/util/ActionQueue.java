
//import java.awt.*;
package presage.util;

import presage.Action;

import java.util.*;
//import java.io.*;
//import java.math.*;
//import javax.swing.*;
//import java.awt.event.*;
//import java.lang.*;
//import java.text.*;


public class ActionQueue {
	
	protected LinkedList<Action> queueList;
	protected String queueName;
	
	public ActionQueue(String name){
		queueList = new LinkedList<Action>();
		queueName = name;
	}
	
	public synchronized void enqueue(Action object){
		queueList.addLast(object);
	}
	
	public synchronized Action dequeue() {
			return queueList.removeFirst();
		}
	
	public synchronized boolean isEmpty(){
		return queueList.isEmpty();
	}
	
	public synchronized void clear(){
		queueList.clear();		
	}
	
} // ends class Queue
