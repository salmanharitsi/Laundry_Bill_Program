
package laundri;

import java.awt.Component;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class data_pelanggan {
    String jdbcDriver = "com.mysql.cj.jdbc.Driver";
    String dbUrl = "jdbc:mysql://localhost/data_pelanggan";
    String user = "root";
    String password = "";
    
    Connection con;
    Statement st;
    ResultSet rs;
    PreparedStatement ps;
    private Component parentComponent;
    
    boolean respons;
    
    //metod untuk mengecek driver database dan koneksi ke database
    public data_pelanggan(){
        try {
            Class.forName(jdbcDriver);
            JOptionPane.showMessageDialog(parentComponent, "Driver Load");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(parentComponent, "Driver tidak ditemukan");
            Logger.getLogger(data_pelanggan.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            con = DriverManager.getConnection(dbUrl, user, password);
            JOptionPane.showMessageDialog(parentComponent, "Database Terhubung");
        } catch (SQLException ex) {  
            Logger.getLogger(data_pelanggan.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(parentComponent, "Database Tidak Terhubung, periksa config mysql!");   
        }
    }
    
    //metod memasukkan data pelanggan yang akan diproses ke dalam tabel database  
    public boolean insertplgn(String nohp, String nama, String tanggal, String total){
        String query = "insert into pelanggan (no_hp, nama, tanggal, total_tagihan) values (?, ?, ?, ?)";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, nohp);
            ps.setString(2, nama);
            ps.setString(3, tanggal);
            ps.setString(4, total);
            ps.executeUpdate();
            respons = true;
        } catch (SQLException ex) {
            respons = false;
            Logger.getLogger(data_pelanggan.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(parentComponent, "insert failed");
        }
        return respons;
    }
    
    //metod memasukkan data pelanggan yang sudah selesai ke dalam tabel database
    public boolean insertselesai(String nohp, String nama, String tanggal, String total){
        String query = "insert into selesai (no_hp, nama, tanggal, total_tagihan) values (?, ?, ?, ?)";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, nohp);
            ps.setString(2, nama);
            ps.setString(3, tanggal);
            ps.setString(4, total);
            ps.executeUpdate();
            respons = true;
        } catch (SQLException ex) {
            respons = false;
            Logger.getLogger(data_pelanggan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respons;
    }
    
    //metod mengambil data yang ada pada tabel database
    public ResultSet getPlgn(){
        String query = "select * from pelanggan";
        try {
            st = con.createStatement();
            rs = st.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(data_pelanggan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
   
    //metod mengambil data yang ada pada tabel database
    public ResultSet getPlgnSelesai(){
        String query = "select * from selesai";
        try {
            st = con.createStatement();
            rs = st.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(data_pelanggan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    //metod untuk menghapus data pada tabel database
    public void hapusplgn(String noHP){
        String query = "delete from pelanggan where no_hp = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, noHP);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(data_pelanggan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
