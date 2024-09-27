package org.example;

import org.example.connectionPool.domain.Connection;
import org.example.connectionPool.implementation.ConnectionPool;
import org.example.connectionPool.interfaces.IConnectionPool;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main extends Thread{
    public static void main(String[] args) {
try(ExecutorService executorService = Executors.newFixedThreadPool(4);) {
    IConnectionPool connectionPool = ConnectionPool.getInstance();
    connectionPool.asignSize(5);
    Main th = new Main();
    th.setName("Class Main Thread");
    ThreadExtended te = new ThreadExtended();
    te.setName("Extended Thread");
    Thread ti = new Thread( new ThreadImplemented(),"Implemented Thread");


    Future<Connection> future = executorService.submit(()->{
            connectionPool.addConnection();
            System.out.println("Thread into Future : "+ Thread.currentThread().getName()+" created First Connection");
            Thread.sleep(10000);
            return connectionPool.getConnection();
    });

    Connection firstConnection = future.get();
    System.out.println(firstConnection);
    connectionPool.releaseConnection(firstConnection);

    th.start();
    te.start();
    ti.start();

    while (true) {
        executorService.submit(new ThreadExtended());
        executorService.submit(new ThreadImplemented());
        CompletionStage<Connection> completableFuture = CompletableFuture.supplyAsync(()->{
            try {
                return connectionPool.getConnection();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        completableFuture.thenAccept((connection)->{
            try {
                System.out.println("Thread : "+ Thread.currentThread().getName()+ " Using "+connection);
                Thread.sleep(3000);
                System.out.println(" Thread : "+ Thread.currentThread().getName()+ " Return "+connection);
                connectionPool.releaseConnection(connection);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread.sleep(5000);
    }


}catch (Exception e){
    System.out.println(e.getMessage());
}
    }

    @Override
    public void run() {
        try {
            IConnectionPool connectionPool = ConnectionPool.getInstance();
            boolean keepInserting;
            do{
                keepInserting = connectionPool.addConnection();
                System.out.println("Thread : "+ Thread.currentThread().getName()+" created a connection");
            }while(keepInserting);

            while (true){
                connectionPool.printCollection();
                Connection connection = connectionPool.getConnection();

                System.out.println("Thread : "+ Thread.currentThread().getName()+ " Using "+connection);
                Thread.sleep(1000);
                connectionPool.releaseConnection(connection);
                System.out.println(" Thread : "+ Thread.currentThread().getName()+ " Return "+connection);

            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
