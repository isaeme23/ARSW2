package edu.eci.arsw.primefinder;

import java.util.List;

public class PrimeFinderThread extends Thread{

	int a,b;

	private List<Integer> primes;

	private boolean waiting;

	public PrimeFinderThread(int a, int b, List<Integer> primes) {
		super();
                this.primes = primes;
		this.a = a;
		this.b = b;
	}

        @Override
	public void run(){
            for (int i= a;i < b;i++){						
                if (isPrime(i)){
                    primes.add(i);
                    //System.out.println(i);
                }
				synchronized (this){
					while(waiting){
						try {
							this.wait();
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
					}
				}
            }
	}
	
	boolean isPrime(int n) {
	    boolean ans;
            if (n > 2) { 
                ans = n%2 != 0;
                for(int i = 3;ans && i*i <= n; i+=2 ) {
                    ans = n % i != 0;
                }
            } else {
                ans = n == 2;
            }
	    return ans;
	}

	public synchronized List<Integer> getPrimes() {
		return primes;
	}

	public void stopThread(boolean waiting){
		this.waiting = waiting;
	}

	synchronized void restartThread(){
		waiting = false;
		notify();
	}
}
