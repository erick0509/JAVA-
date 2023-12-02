/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktop;

import java.awt.Color;
import java.awt.Font;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author N&N
 */
public class fenetre extends javax.swing.JFrame {

    /**
     * Creates new form fenetre
     */
    private String databaseName;
    public fenetre(String databaseName) throws UnknownHostException, SQLException {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.databaseName=databaseName;
        liste();
    }
    public void formTable(){
        
        table.getTableHeader().setOpaque(false);
        JTableHeader header = table.getTableHeader();
        //header.setBackground(Color.BLUE); // Couleur de fond de l'en-tête
        header.setForeground(Color.GRAY); // Couleur du texte de l'en-tête
        header.setFont(new Font("SansSerif", Font.BOLD, 18));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        //renderer.setBackground(new Color(150,150,150));
        //renderer.setForeground(Color.white);
        //table.getColumnModel().getColumn(0).setCellRenderer(renderer);
        //table.getColumnModel().getColumn(1).setCellRenderer(renderer);
        //table.getColumnModel().getColumn(2).setCellRenderer(renderer);
        //table.getColumnModel().getColumn(3).setCellRenderer(renderer);
        //table.getColumnModel().getColumn(4).setCellRenderer(renderer);
        //-------------//
        //renderer = new DefaultTableCellRenderer();
        //renderer.setBackground(new Color(55,155,255));
        //renderer.setForeground(Color.white);
        //table.getColumnModel().getColumn(5).setCellRenderer(renderer);
        //------------//
        TableColumn column0 = table.getColumnModel().getColumn(0);
        TableColumn column1 = table.getColumnModel().getColumn(1);
        TableColumn column2 = table.getColumnModel().getColumn(2);
        TableColumn column3 = table.getColumnModel().getColumn(3);
        TableColumn column4 = table.getColumnModel().getColumn(4);
        TableColumn column5 = table.getColumnModel().getColumn(5);
        column0.setPreferredWidth(200); 
        column1.setPreferredWidth(200);
        column2.setPreferredWidth(200); 
        column3.setPreferredWidth(200);
        column4.setPreferredWidth(200); 
        column4.setPreferredWidth(500); 
        column5.setPreferredWidth(200);
    }
    public void chercher() throws UnknownHostException{
        InetAddress localHost = InetAddress.getLocalHost();
        String machineName = localHost.getHostName();
        String ARTICLE="F_ARTICLE";
        String DEPOT="F_DEPOT";
        String ARTICLE_STOCK="F_ARTSTOCK";
        
        String connectionString = "jdbc:sqlserver://"+machineName+";databaseName="+databaseName+";integratedSecurity=true;encrypt=false;";
        Statement st;
        ResultSet rs;
        int rowCount=0;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }catch(ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Erreur de pilotage jdbc!", "Avertissement", JOptionPane.WARNING_MESSAGE);
	}
        if(!jTextField1.getText().isEmpty()){
            try{
           
            Connection connection=DriverManager.getConnection(connectionString);
                String sql="SELECT\n" +
                            "    A.AR_Ref AS RÉFÉRENCE,\n" +
                            "	A.AR_Design AS DÉSIGNATION ,\n" +
                            "	REPLACE(REPLACE(FORMAT(CONVERT(DECIMAL(10, 2), A.AR_PrixAch), 'N2'),',',' '),'.',',') AS 'PRIX D''ACHAT',\n" +
                            "   REPLACE(REPLACE(FORMAT(CONVERT(DECIMAL(10, 2), A.AR_PrixVen), 'N2'),',',' '),'.',',') AS 'PRIX DE VENTE',\n" +
                            "    STRING_AGG(CONVERT(VARCHAR(10),E.DE_Intitule)+'='+CONVERT(VARCHAR(10),CONVERT(DECIMAL(10, 2), S.AS_QteSto)),' || ') AS DEPOT,\n" +
                            "    SUM(CONVERT(DECIMAL(10, 2), S.AS_QteSto)) AS TOTAL\n" +
                            "FROM\n" +
                            "    F_ARTICLE A\n" +
                            "INNER JOIN F_ARTSTOCK S ON A.AR_Ref = S.AR_Ref\n" +
                            "INNER JOIN F_DEPOT E ON S.DE_No = E.DE_No\n" +
                        "   WHERE A.AR_Design LIKE '%"+jTextField1.getText()+"%' \n" +
                            "GROUP BY A.AR_Design, A.AR_Ref,A.AR_PrixAch,A.AR_PrixVen;";
                      
                st=connection.createStatement();
                rs=st.executeQuery(sql);
                DefaultTableModel model = new DefaultTableModel();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    model.addColumn(metaData.getColumnName(i));
                }
                while (rs.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = rs.getObject(i);
                    }
                    model.addRow(row);
                    rowCount++;
                }
                table.setModel(model);
                if(rowCount==0){
                    JOptionPane.showMessageDialog(null,"L'article que vous cherchez n'existe pas", "Avertissement", JOptionPane.WARNING_MESSAGE);
                    liste();
                    jTextField1.setText("");
                }
                rowCount=0;
            // Fermeture des ressources JDBC
                rs.close();
                st.close();
                connection.close();
            
            }catch(SQLException e){
                e.printStackTrace(); // Ajoutez cette ligne pour afficher les détails de l'exception
                JOptionPane.showMessageDialog(null,e, "Avertissement", JOptionPane.WARNING_MESSAGE);
            }
        }
        else{
            try{
           
            Connection connection=DriverManager.getConnection(connectionString);
                String sql="SELECT\n" +
                            "    A.AR_Ref AS RÉFÉRENCE,\n" +
                            "	A.AR_Design AS DÉSIGNATION ,\n" +
                            "	REPLACE(REPLACE(FORMAT(CONVERT(DECIMAL(10, 2), A.AR_PrixAch), 'N2'),',',' '),'.',',') AS 'PRIX D''ACHAT',\n" +
                            "   REPLACE(REPLACE(FORMAT(CONVERT(DECIMAL(10, 2), A.AR_PrixVen), 'N2'),',',' '),'.',',') AS 'PRIX DE VENTE',\n" +
                            "    STRING_AGG(CONVERT(VARCHAR(10),E.DE_Intitule)+'='+CONVERT(VARCHAR(10),CONVERT(DECIMAL(10, 2), S.AS_QteSto)),' || ') AS DEPOT,\n" +
                            "    SUM(CONVERT(DECIMAL(10, 2), S.AS_QteSto)) AS TOTAL\n" +
                            "FROM\n" +
                            "    F_ARTICLE A\n" +
                            "INNER JOIN F_ARTSTOCK S ON A.AR_Ref = S.AR_Ref\n" +
                            "INNER JOIN F_DEPOT E ON S.DE_No = E.DE_No\n" +
                        "   WHERE A.AR_Ref LIKE '%"+jTextField2.getText()+"%' \n" +
                            "GROUP BY A.AR_Design, A.AR_Ref,A.AR_PrixAch,A.AR_PrixVen;";
                      
                st=connection.createStatement();
                rs=st.executeQuery(sql);
                DefaultTableModel model = new DefaultTableModel();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    model.addColumn(metaData.getColumnName(i));
                }
                while (rs.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = rs.getObject(i);
                    }
                    model.addRow(row);
                    rowCount++;
                }
                table.setModel(model);
                if(rowCount==0){
                    JOptionPane.showMessageDialog(null,"L'article que vous cherchez n'existe pas", "Avertissement", JOptionPane.WARNING_MESSAGE);
                    liste();
                    jTextField2.setText("");
                }

            // Fermeture des ressources JDBC
                rs.close();
                st.close();
                connection.close();
            
            }catch(SQLException e){
                e.printStackTrace(); // Ajoutez cette ligne pour afficher les détails de l'exception
                JOptionPane.showMessageDialog(null,e, "Avertissement", JOptionPane.WARNING_MESSAGE);
            }
        }
        
        formTable();
    }
    public void liste() throws UnknownHostException, SQLException{
        InetAddress localHost = InetAddress.getLocalHost();
        String machineName = localHost.getHostName();
        String ARTICLE="F_ARTICLE";
        String DEPOT="F_DEPOT";
        String ARTICLE_STOCK="F_ARTSTOCK";
       
        String connectionString = "jdbc:sqlserver://"+machineName+";databaseName="+databaseName+";integratedSecurity=true;encrypt=false;";
        Statement st;
        ResultSet rs;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }catch(ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Erreur de pilotage jdbc!", "Avertissement", JOptionPane.WARNING_MESSAGE);
	}
        try{
           
            Connection connection=DriverManager.getConnection(connectionString);
                String sql="SELECT\n" +
                            "    A.AR_Ref AS RÉFÉRENCE,\n" +
                            "	A.AR_Design AS DÉSIGNATION ,\n" +
                            "	REPLACE(REPLACE(FORMAT(CONVERT(DECIMAL(10, 2), A.AR_PrixAch), 'N2'),',',' '),'.',',') AS 'PRIX D''ACHAT',\n" +
                            "   REPLACE(REPLACE(FORMAT(CONVERT(DECIMAL(10, 2), A.AR_PrixVen), 'N2'),',',' '),'.',',') AS 'PRIX DE VENTE',\n" +
                            "    STRING_AGG(CONVERT(VARCHAR(10),E.DE_Intitule)+'='+CONVERT(VARCHAR(10),CONVERT(DECIMAL(10, 2), S.AS_QteSto)),' || ') AS DEPOT,\n" +
                            "    SUM(CONVERT(DECIMAL(10, 2), S.AS_QteSto)) AS TOTAL\n" +
                            "FROM\n" +
                            "    F_ARTICLE A\n" +
                            "INNER JOIN F_ARTSTOCK S ON A.AR_Ref = S.AR_Ref\n" +
                            "INNER JOIN F_DEPOT E ON S.DE_No = E.DE_No\n" +
                            "GROUP BY A.AR_Design, A.AR_Ref,A.AR_PrixAch,A.AR_PrixVen;";
                      
                st=connection.createStatement();
                rs=st.executeQuery(sql);
                DefaultTableModel model = new DefaultTableModel();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    model.addColumn(metaData.getColumnName(i));
                }
                while (rs.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = rs.getObject(i);
                    }
                    model.addRow(row);
                }
                table.setModel(model);
                formTable();
            // Fermeture des ressources JDBC
            rs.close();
            st.close();
            connection.close();
            
        }catch(SQLException e){
            // Ajoutez cette ligne pour afficher les détails de l'exception
             JOptionPane.showMessageDialog(null,e, "Avertissement", JOptionPane.WARNING_MESSAGE);
        }
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        actualiser = new javax.swing.JButton();
        chercher = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MONITOR");
        setBackground(new java.awt.Color(102, 102, 102));

        jPanel1.setBackground(java.awt.SystemColor.activeCaption);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane2.setToolTipText("");
        jScrollPane2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jScrollPane2.setOpaque(false);

        table.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        table.setForeground(new java.awt.Color(51, 51, 51));
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        table.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        table.setRowHeight(40);
        table.setRowMargin(5);
        jScrollPane2.setViewportView(table);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("LISTE DES ARTICLES");

        actualiser.setBackground(new java.awt.Color(0, 102, 204));
        actualiser.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        actualiser.setForeground(new java.awt.Color(255, 255, 255));
        actualiser.setText("actualiser");
        actualiser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualiserActionPerformed(evt);
            }
        });

        chercher.setBackground(new java.awt.Color(0, 153, 51));
        chercher.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        chercher.setForeground(new java.awt.Color(255, 255, 255));
        chercher.setText("chercher");
        chercher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chercherActionPerformed(evt);
            }
        });

        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField2.setToolTipText("");
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField2FocusLost(evt);
            }
        });
        jTextField2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField2MouseClicked(evt);
            }
        });
        jTextField2.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jTextField2CaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField2InputMethodTextChanged(evt);
            }
        });
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTextField2PropertyChange(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Référence  de l'article:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Désignation  de l'article:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 369, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chercher, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(actualiser, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(jLabel3))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(actualiser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chercher, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void actualiserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualiserActionPerformed
        try {
            liste();
        } catch (UnknownHostException ex) {
            Logger.getLogger(fenetre.class.getName()).log(Level.SEVERE, null, ex);
 
        } catch (SQLException ex) {
            Logger.getLogger(fenetre.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTextField1.setText("");
        jTextField2.setText("");
    }//GEN-LAST:event_actualiserActionPerformed

    private void chercherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chercherActionPerformed
        if(!jTextField1.getText().isEmpty() || !jTextField2.getText().isEmpty()){
            try {
                chercher();
            } catch (UnknownHostException ex) {
                Logger.getLogger(fenetre.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            try {
                liste();
            } catch (UnknownHostException ex) {
                Logger.getLogger(fenetre.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(fenetre.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }//GEN-LAST:event_chercherActionPerformed

    private void jTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusGained
       
    }//GEN-LAST:event_jTextField2FocusGained

    private void jTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusLost
       
    }//GEN-LAST:event_jTextField2FocusLost

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        
    }//GEN-LAST:event_jTextField1FocusGained

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        
    }//GEN-LAST:event_jTextField1FocusLost

    private void jTextField2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField2MouseClicked
       jTextField1.setText("");
    }//GEN-LAST:event_jTextField2MouseClicked

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        jTextField2.setText("");
    }//GEN-LAST:event_jTextField1MouseClicked

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        chercherActionPerformed(evt);
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTextField2PropertyChange
        
    }//GEN-LAST:event_jTextField2PropertyChange

    private void jTextField2CaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField2CaretPositionChanged
        
    }//GEN-LAST:event_jTextField2CaretPositionChanged

    private void jTextField2InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField2InputMethodTextChanged
        
    }//GEN-LAST:event_jTextField2InputMethodTextChanged

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        chercherActionPerformed(evt);
    }//GEN-LAST:event_jTextField1ActionPerformed

    /**
     * @param args the command line arguments
     */
/*
   public static void main(String args[]) {
        /*Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
          For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
    
/*        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(fenetre.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(fenetre.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(fenetre.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(fenetre.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        LocalDate currentDate = LocalDate.now();
        LocalDate specificDate = LocalDate.of(2023, 10, 19);
        
        if (currentDate.isAfter(specificDate)) {
            JOptionPane.showMessageDialog(null, "Acheter maintenant le logiciel!", "Avertissement", JOptionPane.WARNING_MESSAGE);
        } else {         
            java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new fenetre().setVisible(true);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(fenetre.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(fenetre.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        }
        /* Create and display the form */
        
    //}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton actualiser;
    private javax.swing.JButton chercher;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
