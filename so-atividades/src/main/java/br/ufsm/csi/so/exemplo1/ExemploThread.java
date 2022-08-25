package br.ufsm.csi.so.exemplo1;

import lombok.SneakyThrows;

public class ExemploThread {

    private long variavel;
    private AlteraVariavel t1;
    private AlteraVariavel t2;
    private LeVariavel t3;

    public static void main(String[] args) {
        new ExemploThread();
    }

    public ExemploThread() {
        this.t1 = new AlteraVariavel();
        this.t2 = new AlteraVariavel();
        this.t3 = new LeVariavel();
        new Thread(t1).start();
        new Thread(t2).start();
        new Thread(t3).start();
    }

    private class AlteraVariavel implements Runnable {

        private long varLocal;
        @Override
        public void run() {
            while (true) {
                variavel++;
                varLocal++;
            }
        }
    }

    private class LeVariavel implements Runnable {

        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                if (variavel != (t1.varLocal + t2.varLocal)) {
                    long dif = variavel - (t1.varLocal + t2.varLocal);
                    System.out.println("var=" + variavel + " dif=" + dif);
                }
                Thread.sleep(1000);
            }
        }
    }

}
