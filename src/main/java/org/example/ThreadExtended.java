package org.example;

import org.example.connectionPool.domain.Connection;
import org.example.connectionPool.implementation.ConnectionPool;
import org.example.connectionPool.interfaces.IConnectionPool;

public class ThreadExtended extends Thread{
    IConnectionPool connectionPool;
    @Override
    public void run() {
        try {
                connectionPool = ConnectionPool.getInstance();
                boolean keepInserting;
                do{
                    keepInserting = connectionPool.addConnection();
                    System.out.println("Thread : "+ Thread.currentThread().getName()+" created a connection");
                }while(keepInserting);

            while (true){
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
