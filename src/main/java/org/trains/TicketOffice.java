package org.trains;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TicketOffice {
  // parametri di connessione al db
  private final static String DB_URL = "jdbc:mysql://localhost:3306/db_trains";
  private final static String DB_USER = "root";
  private final static String DB_PASSWORD = "rootpassword";
  // query da esguire
  private final static String SQL_TRAVELS = "SELECT id, departure, arrival FROM travels;";
  private final static String SQL_KM = "SELECT km FROM travels WHERE id = ?;";


  public static void main(String[] args) {
    // istanzio lo scanner
    Scanner scanner = new Scanner(System.in);

    int numberOfkm = 0;

    // mostrare a video tutte le tratte disponibili
    // mi connetto al database
    try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)){
      // qui dentro ho a disposizione la connessione aperta
      // preparo il prepared statement con la query che ho salvato nella costante SQL_TRAVELS
      try(PreparedStatement ps = connection.prepareStatement(SQL_TRAVELS)){
        // qui dentro ho a disposizione il prepared statement
        try(ResultSet rs = ps.executeQuery()){
          // qui ho il result set con il risultato della query
          // itero sul resutl set
          while(rs.next()){ // ad ogni iterazione chiamo next e testo il risultato (true o false)
            // ad ogni iterazione del while posso leggere i dati di una riga del risultato
            int id = rs.getInt("id");
            String departure = rs.getString("departure");
            String arrival = rs.getString("arrival");
            System.out.print(id + ") ");
            System.out.print(departure);
            System.out.print("-->");
            System.out.println(arrival);
          }
        }
      }
      // l'utente sceglie una tratta
      System.out.print("Choose a travel: ");
      int choice = Integer.parseInt(scanner.nextLine());
      System.out.println("You choose " + choice);
      // preparo un altro statement con l'sql dei km
      try(PreparedStatement ps = connection.prepareStatement(SQL_KM)){
        // questa query è parametrica quindi devo fare il binding dei parametri
        ps.setInt(1, choice);
        // eseguo la query
        try(ResultSet rs = ps.executeQuery()){
          // se ho almeno un risultato
          if(rs.next()){
            // mostro il numero di km
            numberOfkm = rs.getInt("km");
            System.out.println("Number of km: " + numberOfkm);
          } else {
            // altrimenti mostro un messaggio
            System.out.println("No travels with id " + choice);
          }
        }
      }


    }catch(SQLException exception){
      System.out.println("An error occurred");
    }


    // creo il biglietto con il numero di km scelto dall'utente
    System.out.println("Create ticket with " + numberOfkm + " km");


    scanner.close();
  }
}
