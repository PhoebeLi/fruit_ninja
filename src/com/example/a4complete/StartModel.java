package com.example.a4complete;

import java.util.Observable;
import java.util.Observer;

public class StartModel extends Observable{
	
	
	public StartModel() {
		// TODO Auto-generated constructor stub
		
	}


	public void addObserver(Observer observer) {
        super.addObserver(observer);
    }
	
	public void initObservers() {
        setChanged();
        notifyObservers();
    }
	
	@Override
	public synchronized void deleteObserver(Observer observer) {
		// TODO Auto-generated method stub
		super.deleteObserver(observer);
		setChanged();
		notifyObservers();
	}
}
