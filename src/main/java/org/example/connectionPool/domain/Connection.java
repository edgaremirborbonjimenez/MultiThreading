package org.example.connectionPool.domain;

import java.util.Objects;

public class Connection {
    int numConnection;
    public Connection(int numConnection){
        this.numConnection = numConnection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return numConnection == that.numConnection;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(numConnection);
    }

    @Override
    public String toString() {
        return "Connection: #"+numConnection;
    }
}
