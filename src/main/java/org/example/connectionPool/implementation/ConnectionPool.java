package org.example.connectionPool.implementation;

import org.example.connectionPool.domain.Connection;
import org.example.connectionPool.interfaces.IConnectionPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPool implements IConnectionPool {
    private BlockingQueue<Connection> pool;
    private static IConnectionPool connectionPoolInstance;
    //private final AtomicInteger num = new AtomicInteger(0);
    private volatile int num = 0;
    private volatile boolean keepInserting = true;

    private ConnectionPool(){
    }

    public static IConnectionPool getInstance(){
        if(connectionPoolInstance==null){
            connectionPoolInstance = new ConnectionPool();
        }
        return connectionPoolInstance;
    }

    @Override
    public boolean addConnection(){
        if(keepInserting){
            //int id = this.num.incrementAndGet();
            int id = this.num++;
            keepInserting = this.pool.offer(new Connection(id));
            if(keepInserting){
                System.out.println("Connetion: "+ id + " inserted");
            }
        }
        return keepInserting;
    }

    @Override
    public void asignSize(int poolSize){
        this.pool = new ArrayBlockingQueue<>(poolSize);
    }

    @Override
    public void printCollection() {
        pool.forEach(data-> System.out.println(data));
    }


    @Override
    public Connection getConnection() throws InterruptedException {
        return this.pool.take();
    }

    @Override
    public void releaseConnection(Connection connection) throws InterruptedException {
         this.pool.put(connection);
    }


}
