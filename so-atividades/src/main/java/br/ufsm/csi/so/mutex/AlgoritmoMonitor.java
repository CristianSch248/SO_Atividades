package br.ufsm.csi.so.mutex;

import lombok.SneakyThrows;

public class AlgoritmoMonitor {

    private long varGlobal;
    private Object monitor = new Object();
    private ProcessoA processoA;
    private ProcessoB processoB;

    public static void main(String[] args) {
        new AlgoritmoMonitor();
    }

    public AlgoritmoMonitor() {
        this.processoA = new ProcessoA();
        this.processoB = new ProcessoB();
        new Thread(processoA).start();
        new Thread(processoB).start();
        new Thread(new ConfereConsistencia()).start();
    }

    private class ProcessoA implements Runnable {

        private long varLocal;
        @Override
        public void run() {
            while (true) {
                //acesso a regiao critica
                synchronized (this) {
                    //regiao critica
                    varGlobal++;
                    varLocal++;
                    //sai da regiao critica
                }
            }
        }
    }

    private class ProcessoB implements Runnable {

        private long varLocal;
        @Override
        public void run() {
            while (true) {
                //acesso a regiao critica
                synchronized (this) {
                    varGlobal++;
                    varLocal++;
                }
            }
        }
    }

    private class ConfereConsistencia implements Runnable {

        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                Thread.sleep(1000);
                synchronized (monitor) {
                    if (varGlobal != (processoA.varLocal + processoB.varLocal)) {
                        System.out.println("Inconssistente: " + Math.abs(varGlobal - (processoA.varLocal + processoB.varLocal)));
                    } else {
                        System.out.println("Consistente. varGlobal=" + varGlobal);
                    }
                }
            }
        }
    }

}
