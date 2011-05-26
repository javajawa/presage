
//import java.awt.*;
package presage.util;
import presage.Input;
import presage.Message;

import java.util.*;
//import java.io.*;
//import java.math.*;
//import javax.swing.*;
//import java.awt.event.*;
//import java.lang.*;
//import java.text.*;


public class InputQueue implements java.io.Serializable {
	
	protected LinkedList<Input> queueList;
	protected String queueName;
	
	public InputQueue(String name){
		queueList = new LinkedList<Input>();
		queueName = name;
	}
	
	public synchronized void enqueue(Input object){
		queueList.addLast(object);
	}
	
	public synchronized Input dequeue() {
			return queueList.removeFirst();
		}
	
	public synchronized boolean isEmpty(){
		return queueList.isEmpty();
	}
	
	public synchronized void clear(){
		queueList.clear();		
	}
	
} // ends class Queue
