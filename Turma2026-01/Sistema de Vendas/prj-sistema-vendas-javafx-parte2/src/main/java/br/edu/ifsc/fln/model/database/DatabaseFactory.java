package br.edu.ifsc.fln.model.database;

public class DatabaseFactory {
    public static Database getDatabase(String nome){
        if(nome.equals("postgresql")){
            return new DatabasePostgreSQL();
        }else if(nome.equals("mysql")){
            return new DatabaseMySQL();
        } else if (nome.equals("mariadb")) {
            return new DatabaseMariaDB();
        }
        return null;
    }
}
