package br.ufsm.csi.so.mutex;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class AlgoritmoProdutoresConsumidoresMonitor {

    private final static int TAM_BUFFER = 100;
    private final List<Integer> buffer = new ArrayList<>(TAM_BUFFER);
    private AlgoritmoProdutoresConsumidoresMonitor.Produtor produtor;
    private AlgoritmoProdutoresConsumidoresMonitor.Consumidor consumidor;

    public static void main(String[] args) {
        new AlgoritmoProdutoresConsumidoresMonitor();
    }

    public AlgoritmoProdutoresConsumidoresMonitor() {
        this.produtor = new Produtor();
        this.consumidor = new Consumidor();
        new Thread(produtor).start();
        new Thread(consumidor).start();
    }

    private class Produtor implements Runnable {

        private Random rnd = new Random();

        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                //produz
                int num = Math.abs(rnd.nextInt());
                synchronized (buffer) {
                    if (buffer.size() == TAM_BUFFER) {
                        buffer.wait();
                    }
                    System.out.println("Produtor: produziu " + num);
                    buffer.add(num);
                    if (buffer.size() == 1) {
                        buffer.notify();
                    }
                }
            }
        }
    }

    private class Consumidor implements Runnable {

        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                //acesso a regiao critica
                synchronized (buffer) {
                    if (buffer.isEmpty()) {
                        buffer.wait();
                    }
                    int num = buffer.remove(0);
                    System.out.println("Consumidor: consumiu " + num);
                    if (buffer.size() == (TAM_BUFFER - 1)) {
                        buffer.notify();
                    }
                }

            }
        }
    }


}
