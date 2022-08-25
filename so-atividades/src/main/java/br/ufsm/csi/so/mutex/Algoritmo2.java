package br.ufsm.csi.so.mutex;

import lombok.SneakyThrows;

public class Algoritmo2 {

    private char vez = 'A';
    private long varGlobal;
    private ProcessoA processoA;
    private ProcessoB processoB;

    public static void main(String[] args) {
        new Algoritmo2();
    }

    public Algoritmo2() {
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
                
                while (vez == 'B') { /* faz nada (espera ocupada) */ }
                //regiao critica
                varGlobal++;
                varLocal++;
                //sai da regiao critica
                vez = 'B';
            }
        }
    }

    private class ProcessoB implements Runnable {

        private long varLocal;
        @Override
        public void run() {
            while (true) {
                //acesso a regiao critica

                while (vez == 'A') { /* faz nada (espera ocupada) */ }
                //regiao critica
                varGlobal++;
                varLocal++;
                //sai da regiao critica
                vez = 'A';
            }
        }
    }

    private class ConfereConsistencia implements Runnable {

        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                Thread.sleep(1000);
                if (varGlobal != (processoA.varLocal + processoB.varLocal)) {
                    System.out.println("Inconssistente: " + Math.abs(varGlobal - (processoA.varLocal + processoB.varLocal)));
                } else {
                    System.out.println("Consistente");
                }
            }
        }
    }

}
