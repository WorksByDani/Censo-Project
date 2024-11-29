/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Interfaces;

import dba.Mysql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author ARMAGEDON
 */
public class JefeFamilia extends javax.swing.JFrame {
    Connection conn;
    Statement sent;
    public static String codVivienda;
    
    public JefeFamilia() {
        initComponents();
        conn=Mysql.getConnection();
        
        txtNombre.setText(InicioSesion.nombreUrs);
        setCodVivienda();
        
    }
    @SuppressWarnings("unchecked")
    
    private void setCodVivienda(){
        try {
                String sql = "SELECT codVivienda FROM censo.usuario Where codUsr = "+InicioSesion.codUsuario;
                sent = conn.createStatement();
                ResultSet rs = sent.executeQuery(sql);
                rs.next(); // SI O SI PONER PARA LO RESULTADOS
                codVivienda = rs.getString("codVivienda");
                txtCodigoViv.setText(codVivienda);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelFondo = new javax.swing.JPanel();
        PanelNavegador = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        BotonReporte = new javax.swing.JButton();
        BotonEstadoEnc = new javax.swing.JButton();
        txtCodigoViv = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        BotonAtras = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        PanelReporte = new javax.swing.JPanel();
        BotonAtras2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PanelFondo.setBackground(new java.awt.Color(255, 255, 255));
        PanelFondo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelNavegador.setBackground(new java.awt.Color(4, 22, 47));
        PanelNavegador.setForeground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Nombre:");

        jLabel9.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("JEFE DE FAMILIA");

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/JefeFamilia.jpg"))); // NOI18N

        txtNombre.setEditable(false);
        txtNombre.setBackground(new java.awt.Color(255, 255, 255));
        txtNombre.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtNombre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNombre.setBorder(null);
        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });

        BotonReporte.setBackground(new java.awt.Color(204, 255, 255));
        BotonReporte.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BotonReporte.setText("ENVIAR REPORTE");
        BotonReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonReporteActionPerformed(evt);
            }
        });

        BotonEstadoEnc.setBackground(new java.awt.Color(204, 255, 255));
        BotonEstadoEnc.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BotonEstadoEnc.setText("VER ESTADO DE ENCUESTA");
        BotonEstadoEnc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonEstadoEncActionPerformed(evt);
            }
        });

        txtCodigoViv.setEditable(false);
        txtCodigoViv.setBackground(new java.awt.Color(255, 255, 255));
        txtCodigoViv.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtCodigoViv.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCodigoViv.setBorder(null);
        txtCodigoViv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoVivActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Codigo Vivienda");

        BotonAtras.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BotonAtras.setForeground(new java.awt.Color(4, 22, 47));
        BotonAtras.setText("Atras");
        BotonAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonAtrasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelNavegadorLayout = new javax.swing.GroupLayout(PanelNavegador);
        PanelNavegador.setLayout(PanelNavegadorLayout);
        PanelNavegadorLayout.setHorizontalGroup(
            PanelNavegadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelNavegadorLayout.createSequentialGroup()
                .addGroup(PanelNavegadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelNavegadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(PanelNavegadorLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(BotonEstadoEnc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(PanelNavegadorLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(BotonReporte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(PanelNavegadorLayout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addGroup(PanelNavegadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtCodigoViv, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(PanelNavegadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtNombre)))))
                    .addGroup(PanelNavegadorLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(BotonAtras)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        PanelNavegadorLayout.setVerticalGroup(
            PanelNavegadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelNavegadorLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel7)
                .addGap(17, 17, 17)
                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(txtCodigoViv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(BotonEstadoEnc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(BotonReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76)
                .addComponent(BotonAtras)
                .addContainerGap(93, Short.MAX_VALUE))
        );

        PanelFondo.add(PanelNavegador, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setToolTipText("");

        PanelReporte.setBackground(new java.awt.Color(255, 255, 255));
        PanelReporte.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BotonAtras2.setText("Atras");
        BotonAtras2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonAtras2ActionPerformed(evt);
            }
        });
        PanelReporte.add(BotonAtras2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 690, -1, -1));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setBorder(javax.swing.BorderFactory.createTitledBorder("Escriba Aquí"));
        jScrollPane1.setViewportView(jTextArea1);

        PanelReporte.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 280, 720, 300));

        jButton1.setText("ENVIAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        PanelReporte.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 620, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel6.setText("REPORTE");
        PanelReporte.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 30, 170, 60));
        PanelReporte.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 80, 240, 10));

        jTextArea3.setEditable(false);
        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jTextArea3.setText("AVISO: \t\nEscriba la descripción del problema que encontro en su encuesta\nDicho reporte se le enviará al un adminitrador");
        jTextArea3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane3.setViewportView(jTextArea3);

        PanelReporte.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 90, 470, 130));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jLabel1.setText("(tenga en cuenta que solo puede ingresar un maximo de 100 carecteres)");
        PanelReporte.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, -1, -1));

        jTabbedPane1.addTab("Reporte", PanelReporte);

        PanelFondo.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 960, 730));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelFondo, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    private boolean realizoEnc(){
        boolean realizo = false;
        try {
        String sql = "SELECT codVivienda FROM cap_a WHERE codVivienda = '" + txtCodigoViv.getText()+"'";        
            sent = conn.createStatement(); // para procesar la sentencia sql y obtener los resultados
            ResultSet rs = sent.executeQuery(sql); //obiene los resultados de la consulta sql
            
            if(rs.next()){ 
                realizo = true;
            }else{
                realizo = false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error"+ e.getMessage());
        }
        return realizo;
    }
    private void BotonEstadoEncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonEstadoEncActionPerformed
        // VER ESTADO DE ENCUESTA
        if(realizoEnc()){
            new Encuesta().setVisible(true);
        }else{
            JOptionPane.showMessageDialog(null, "Aun no realizó ninguna encuesta");
        }
        
    }//GEN-LAST:event_BotonEstadoEncActionPerformed

    private void BotonReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonReporteActionPerformed
        // ENVIAR REPORTE AL ADMINISTRADOR
        jTextArea1.setText("");
        jTabbedPane1.setSelectedIndex(0);
        jTabbedPane1.getSelectedIndex();
    }//GEN-LAST:event_BotonReporteActionPerformed

    private void BotonAtras2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonAtras2ActionPerformed
        // BOTON ATRAS
        this.setVisible(false);
        new InicioSesion().setVisible(true);
    }//GEN-LAST:event_BotonAtras2ActionPerformed

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // BOTON ENVIAR REPORTE
        int tamTexto = jTextArea1.getText().length();
        
        if(tamTexto > 100){
           JOptionPane.showMessageDialog(null, "El tamaño de su reporte es muy grande: "
                   + tamTexto + ". Por favor sea mas breve"); 
        }else{
            try {
                String sql = "insert into censo.reporte values(?,?)";
                PreparedStatement ps = conn.prepareCall(sql);
                ps.setString(1,InicioSesion.codUsuario);
                ps.setString(2,jTextArea1.getText());

                int n = ps.executeUpdate();
                if(n>0){
                    JOptionPane.showMessageDialog(null, "Se envio correctamente su mensaje al adminitrador");
                }
            } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,"Error"+ e.getMessage());
            }
            jTextArea1.setText("");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtCodigoVivActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoVivActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoVivActionPerformed

    private void BotonAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonAtrasActionPerformed
        //Boton atras
        new InicioSesion().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_BotonAtrasActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JefeFamilia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JefeFamilia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JefeFamilia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JefeFamilia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JefeFamilia().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonAtras;
    private javax.swing.JButton BotonAtras2;
    private javax.swing.JButton BotonEstadoEnc;
    private javax.swing.JButton BotonReporte;
    private javax.swing.JPanel PanelFondo;
    private javax.swing.JPanel PanelNavegador;
    private javax.swing.JPanel PanelReporte;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextField txtCodigoViv;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
