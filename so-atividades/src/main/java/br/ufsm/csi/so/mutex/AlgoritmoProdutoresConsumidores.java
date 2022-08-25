package br.ufsm.csi.so.mutex;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class AlgoritmoProdutoresConsumidores {

    private Semaphore mutex = new Semaphore(1);
    private final static int TAM_BUFFER = 100;
    private final List<Integer> buffer = new ArrayList<>(TAM_BUFFER);
    private AlgoritmoProdutoresConsumidores.Produtor produtor;
    private AlgoritmoProdutoresConsumidores.Consumidor consumidor;

    public static void main(String[] args) {
        new AlgoritmoProdutoresConsumidores();
    }

    public AlgoritmoProdutoresConsumidores() {
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
                mutex.acquire();
                System.out.println("Produtor: produziu " + num);
                buffer.add(num);
                mutex.release();
            }
        }
    }

    private class Consumidor implements Runnable {

        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                //acesso a regiao critica
                mutex.acquire();
                int num = buffer.remove(0);
                System.out.println("Consumidor: consumiu " + num);
                mutex.release();

            }
        }
    }


}
