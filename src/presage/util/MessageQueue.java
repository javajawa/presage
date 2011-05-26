
//import java.awt.*;
package presage.util;

import presage.Message;

import java.util.*;
//import java.io.*;
//import java.math.*;
//import javax.swing.*;
//import java.awt.event.*;
//import java.lang.*;
//import java.text.*;


public class MessageQueue implements java.io.Serializable {
	
	protected LinkedList<Message> queueList;
	protected String queueName;
	
	public MessageQueue(String name){
		queueList = new LinkedList<Message>();
		queueName = name;
	}
	
	public synchronized void enqueue(Message object){
		queueList.addLast(object);
	}
	
	public synchronized Message dequeue() {
			return queueList.removeFirst();
		}
	
	public synchronized boolean isEmpty(){
		return queueList.isEmpty();
	}
	
	public synchronized void clear(){
		queueList.clear();		
	}
	
} // ends class Queue
