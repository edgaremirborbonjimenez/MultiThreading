package org.example.connectionPool.interfaces;

import org.example.connectionPool.domain.Connection;

public interface IConnectionPool {
    Connection getConnection()throws InterruptedException;
    void releaseConnection(Connection connection) throws InterruptedException;
    boolean addConnection();
    void asignSize(int poolSize);
    void printCollection();
}
