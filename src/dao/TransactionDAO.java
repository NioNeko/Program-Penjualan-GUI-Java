/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.CartItem;
/**
 *
 * @author Soap
 */
public class TransactionDAO {
    public boolean saveTransaction(double total,double payment,double change,ArrayList<CartItem> cart) {
        String sqlTransaction = "INSERT INTO transactions (total, payment, change_money) VALUES (?, ?, ?)";
        String sqlDetails ="INSERT INTO transaction_details (transaction_id, product_id, price, qty, subtotal) VALUES (?, ?, ?, ?, ?)";        
        java.sql.Connection conn = null;
        java.sql.PreparedStatement stmtTx = null;
        java.sql.PreparedStatement stmtDetail = null;
        

        try {
            // Grab the connection from your database layer utility
            conn = database.DBConnection.getConnection();
            
            // Turn off auto-commit to ensure both inserts pass or fail together as a unit
            conn.setAutoCommit(false);

            stmtTx = conn.prepareStatement(
                sqlTransaction,
                Statement.RETURN_GENERATED_KEYS
            );

            stmtTx.setDouble(1, total);
            stmtTx.setDouble(2, payment);
            stmtTx.setDouble(3, change);
            
            stmtTx.executeUpdate();
            
            ResultSet rs = stmtTx.getGeneratedKeys();

            if (!rs.next()) {
                throw new Exception("Failed to get transaction ID");
            }

            int transactionId = rs.getInt(1);
            
            stmtDetail = conn.prepareStatement(sqlDetails);
            for (CartItem item : cart) {

                stmtDetail.setInt(1, transactionId);
                stmtDetail.setInt(2, item.getId());
                stmtDetail.setDouble(3, item.getPrice());
                stmtDetail.setInt(4, item.getQty());
                stmtDetail.setDouble(5, item.getSubtotal());

                stmtDetail.executeUpdate();

                PreparedStatement stock = conn.prepareStatement(
                    "UPDATE products SET stock = stock - ? WHERE id = ?"
                );

                stock.setInt(1, item.getQty());
                stock.setInt(2, item.getId());

                stock.executeUpdate();
                stock.close();
            }

            // Commit transaction together if both steps succeed flawlessly
            conn.commit();
            return true;

        } catch (Exception e) {
            // Rollback any database edits if an error happens (e.g. lost connection midway)
            if (conn != null) {
                try { conn.rollback(); } catch (java.sql.SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            System.out.println(e.getMessage());
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());            
            return false;
        } finally {
            // Resource cleanup routine to prevent memory or connection leaks
            try { if (stmtTx != null) stmtTx.close(); } catch (Exception e) {}
            try { if (stmtDetail != null) stmtDetail.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

    }

}