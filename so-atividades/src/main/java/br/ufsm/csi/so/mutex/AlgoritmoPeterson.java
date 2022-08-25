package br.ufsm.csi.so.mutex;

import lombok.SneakyThrows;

public class AlgoritmoPeterson {

    private char vez = 'A';
    private boolean CA = false;
    private boolean CB = false;
    private long varGlobal;
    private AlgoritmoPeterson.ProcessoA processoA;
    private AlgoritmoPeterson.ProcessoB processoB;

    public static void main(String[] args) {
        new AlgoritmoPeterson();
    }

    public AlgoritmoPeterson() {
        this.processoA = new ProcessoA();
        this.processoB = new ProcessoB();
        new Thread(processoA).start();
        new Thread(processoB).start();
        //new Thread(new ConfereConsistencia()).start();
    }

    private class ProcessoA implements Runnable {

        private long varLocal;
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                //acesso a regiao critica
                AlgoritmoPeterson.this.CA = true;
                AlgoritmoPeterson.this.vez = 'B';
                while (AlgoritmoPeterson.this.CB && AlgoritmoPeterson.this.vez == 'B') { /* faz nada (espera ocupada) */ }
                //regiao critica
                System.out.println("Processo A acessou a região crítica.");
                varGlobal++;
                varLocal++;
                //sai da regiao critica
                AlgoritmoPeterson.this.CA = false;
                System.out.println("Processo A saiu da região crítica.");
                Thread.sleep(100);
            }
        }
    }

    private class ProcessoB implements Runnable {

        private long varLocal;
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                //acesso a regiao critica
                AlgoritmoPeterson.this.CB = true;
                AlgoritmoPeterson.this.vez = 'A';
                while (AlgoritmoPeterson.this.CA
                        && AlgoritmoPeterson.this.vez == 'A') { /* faz nada (espera ocupada) */ }
                //regiao critica
                System.out.println("Processo B acessou a região crítica.");
                varGlobal++;
                varLocal++;
                //sai da regiao critica
                AlgoritmoPeterson.this.CB = false;
                System.out.println("Processo B saiu da região crítica.");
                Thread.sleep(100);
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
                    System.out.println("Consistente: varGlobal=" + varGlobal);
                }
            }
        }
    }


}
