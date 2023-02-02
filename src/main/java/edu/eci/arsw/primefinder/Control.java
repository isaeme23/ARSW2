/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 3000;
    private final static int TMILISECONDS = 10000;

    private List<Integer> primes = new ArrayList<>();

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];
    
    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];

        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA, this.primes);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1, this.primes);
    }
    
    public static Control newControl() {
        return new Control();
    }

    public void timeout(){
        boolean a = Boolean.FALSE;
        long start  = System.currentTimeMillis();
        long fin;
        while (!a){
            fin = System.currentTimeMillis() - start;
            if (fin >= TMILISECONDS){
                a = Boolean.TRUE;
            }
        }
    }

    @Override
    public void run(){
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();
        }
        timeout();
        try {
            synchronized (pft[1].getPrimes()) {
                System.out.println(pft[1].getPrimes());
                pft[1].getPrimes().wait();
                pft[1].getPrimes().notifyAll();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
}