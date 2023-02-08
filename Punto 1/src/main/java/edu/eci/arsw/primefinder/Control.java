/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 300000;
    private final static int TMILISECONDS = 10;

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
    public void run() {
        startThreads();

        while(pft[pft.length-1].isAlive()) {
            timeout();
            stopThreads();
            showPrimes();

            String entrada = scannEnter();
            if (entrada.equals("")) {
                restartThreads();
            }
        }
    }

    public void startThreads(){
        for (int i = 0; i < NTHREADS; i++) {
            pft[i].start();
        }
    }

    public void stopThreads(){
        for (int i = 0; i < NTHREADS; i++) {
            pft[i].stopThread(true);
        }
    }

    public void showPrimes(){
        System.out.println(primes);
        System.out.println();
    }

    public String scannEnter(){
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public void restartThreads(){
        for (int i = 0; i < NTHREADS; i++) {
            pft[i].restartThread();
        }
    }
    
}