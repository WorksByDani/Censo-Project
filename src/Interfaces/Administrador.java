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
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author GRUPO MTTP
 */
public class Administrador extends javax.swing.JFrame {
    Connection conn;
    DefaultTableModel model;
    Statement sent;
    
    public Administrador() {
        initComponents();
        
        conn=Mysql.getConnection();
        
        DeshabilitarTablas();
        LlenarTablas();
        setNombreAdm();
    }
    
    private void DeshabilitarTablas(){
        DeshabilitarTablaReporte();
        DeshabilitarTablaJefeZona();
        DeshabilitarTablaEncuestadores();
        DeshabilitarTablaJefFam();
    }
    
    private void LlenarTablas(){
        llenarTabla("Jefe de Zona", tablaJefeZona);
        llenarTabla("Encuestador", tablaEncuestador);
        llenarTabla("Jefe de Familia", TablaJefeFamilia);
        
        llenarTablaCenso();
        llenarTablaReporte();
        
        setNumViviendas();
        setNumJefesZ();
        setNumJefFam();
        setNumEnc();
    }
    
    private void guardar(javax.swing.JTextField  CI, String TipoUsr, javax.swing.JTextField nombre, javax.swing.JTextField dir, 
        javax.swing.JTextField telef, javax.swing.JTextField correo, 
        javax.swing.JTextField zona, javax.swing.JTextField contra, javax.swing.JTable tabla){
        // BOTON GUARDAR 
        try {
            String sql = "INSERT into usuario (codUsr, nombreUsr,dirUsr,telfUsr,correoUsr,tipoUsr, contrUsr,ZonaCensal)"+
            "values(?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareCall(sql);
            ps.setString(1, CI.getText());
            ps.setString(2, nombre.getText());
            ps.setString(3, dir.getText());
            ps.setString(4, telef.getText());
            ps.setString(5, correo.getText());
            ps.setString(6, TipoUsr); // usuario a gurdar
            ps.setString(7, zona.getText());
            ps.setString(8, contra.getText());

            int n = ps.executeUpdate();
            if(n>0){
                JOptionPane.showMessageDialog(null, "Registro guardado correctamente");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error"+ e.getMessage());
        }
        llenarTabla(TipoUsr, tabla);
        LimpiarTablas();
    }
  
    private void cerrar(){
        int r =JOptionPane.showOptionDialog(this, "¿Está seguro de cerrar la sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] {"Si", "No"}, "No");
        if (r == 0){
            this.setVisible(false);
            new InicioSesion().setVisible(true);
        }
    }
    
    
    private void llenarTabla(String usr, javax.swing.JTable tabla){
        try {
            conn = Mysql.getConnection();
            String[]titulos = {"CI", "nombreUsr", "dirUsr", "telfUsr", "correoUsr", "ZonaCensal","Contraseña"}; // colocar mismos nombres de la tabla
            String sql = "select * from usuario " // Consulta
                    + "where tipoUsr = '" + usr + "'";
            model = new DefaultTableModel(null, titulos);
            sent = conn.createStatement(); //sent = estado para ejecutar sql
            ResultSet rs = sent.executeQuery(sql); //  resultados
            
            String[]fila = new String[7];
            while(rs.next()){
                fila[0]=rs.getString("codUsr"); //No colocar repetidos
                fila[1]=rs.getString("nombreUsr");
                fila[2]=rs.getString("dirUsr");
                fila[3]=rs.getString("telfUsr");
                fila[4]=rs.getString("correoUsr");
                fila[5]=rs.getString("ZonaCensal");
                fila[6]=rs.getString("contrUsr");
                model.addRow(fila);
            }
            tabla.setModel(model);// setea los datos recuperados a la tablaJesfes

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setNumViviendas(){
        String total = "" ;
        try {
                String sql = "SELECT COUNT(codVivienda) as cantVivienda FROM cap_a";
                sent = conn.createStatement();
                ResultSet rs = sent.executeQuery(sql);
                rs.next(); // SI O SI PONER PARA LO RESULTADOS
                total = rs.getString("cantVivienda");

        } catch (Exception e) {
            e.printStackTrace();
        }
        textNumViviendas.setText(total);
        textNumViviendas.setEditable(false);
    }
    
    private void setNumEnc(){
        String total = setNumUsr("Encuestador");
        textNumEnc.setText(total);
        textNumEnc.setEditable(false);
    }
    
    private void setNumJefesZ(){
        String total = setNumUsr("Jefe de Zona");
        textNumJefesZona.setText(total);
        textNumJefesZona.setEditable(false);
    }
    
    private void setNumJefFam(){
        String total = setNumUsr("Jefe de Familia");
        txtNumJefeFam.setText(total);
        txtNumJefeFam.setEditable(false);
    }
    
    public String setNumUsr(String tipoUsr){
        String total = "" ;
        try {
                String sql = "SELECT COUNT(codUsr) as cant FROM censo.usuario Where tipoUsr = " + "'"+tipoUsr+"'";
                sent = conn.createStatement();
                ResultSet rs = sent.executeQuery(sql);
                rs.next(); // SI O SI PONER PARA LO RESULTADOS
                total = rs.getString("cant");

            } catch (Exception e) {
                e.printStackTrace();
            }
        return total;
    }
    
    private void setNombreAdm(){
        textNombre.setText(InicioSesion.nombreUrs); // RECUPERA EL NOMBRE DEL ADM DESDE EL FRAME INICIO SESION
        textNombre.setEditable(false);
    }
    
    private void llenarTablaCenso(){
        try {
            
            conn = Mysql.getConnection();
            String[]titulos = {"codVivienda", "comunidad", "localidad", "barrio", "calle", "numPuerta", "totalPersonas"}; // colocar mismos nombres de la tabla
            String sql = "select codVivienda, comunidad, localidad, barrio, calle, numPuerta, totalPersonas from censo.cap_a";
            model = new DefaultTableModel(null, titulos);
            sent = conn.createStatement(); //sent = estado para ejecutar sql
            ResultSet rs = sent.executeQuery(sql); // 
            
            String[]fila = new String[7];
            while(rs.next()){
                fila[0]=rs.getString("codVivienda"); //No colocar repetidos
                fila[1]=rs.getString("comunidad");
                fila[2]=rs.getString("localidad");
                fila[3]=rs.getString("barrio");
                fila[4]=rs.getString("calle");
                fila[5]=rs.getString("numPuerta");
                fila[6]=rs.getString("totalPersonas");
                model.addRow(fila);
            }
            tablaCenso.setModel(model);
            tablaCenso.setEnabled(false); // para que no pueda ser modificado, solo lectura
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void llenarTablaReporte(){
        try {
            conn = Mysql.getConnection();
            String[]titulos = {"codUsr", "nombreJefeFamilia", "reporte"}; // colocar mismos nombres de la tabla
            String sql = "select reporte.codUsr, nombreUsr, reporte from reporte, "
                    + "usuario WHERE usuario.codUsr = reporte.codUsr";
                    
            model = new DefaultTableModel(null, titulos);
            sent = conn.createStatement(); //sent = estado para ejecutar sql
            ResultSet rs = sent.executeQuery(sql); // 
            
            String[]fila = new String[3];
            while(rs.next()){
                fila[0]=rs.getString("codUsr"); //No colocar repetidos
                fila[1]=rs.getString("nombreUsr");
                fila[2]=rs.getString("reporte");
                
                model.addRow(fila);
            }
            TablaReporte.setModel(model);//la tabla sql adoptará el modelo "model", establecido anteriormente
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    
    private void LimpiarTablas(){
        // JEFE ZONA
        txtNombreJZ.setText("");
        txtDirJZ.setText("");
        txtTelfJZ.setText("");
        txtCorreoJZ.setText("");
        txtZonaJZ.setText("");
        txtContraJZ.setText("");
        txtCIJZ.setText("");
        
        // JEFE FAMILIA
        txtNombreJF.setText("");
        txtDirJF.setText("");
        txtTelfJF.setText("");
        txtCorreoJF.setText("");
        txtZonaJF.setText("");
        txtContraJF.setText("");
        txtCIJFam.setText("");
        
        // ECUESTADORES
        txtNombreEnc.setText("");
        txtDirEnc.setText("");
        txtTelfEnc.setText("");
        txtCorreoEnc.setText("");
        txtZonaEnc.setText("");
        txtContrEnc.setText("");
        txtCIEnc.setText("");
    }
    
    void DeshabilitarTablaJefeZona(){
       txtNombreJZ.setEditable(false);
       txtDirJZ.setEditable(false);
       txtTelfJZ.setEditable(false);
       txtCorreoJZ.setEditable(false);
       txtZonaJZ.setEditable(false);
       txtContraJZ.setEditable(false);
       txtCIJZ.setEditable(false);
    }
    void DeshabilitarTablaEncuestadores(){
       txtNombreEnc.setEditable(false);
       txtDirEnc.setEditable(false);
       txtTelfEnc.setEditable(false);
       txtCorreoEnc.setEditable(false);
       txtZonaEnc.setEditable(false);
       txtContrEnc.setEditable(false);
       txtCIEnc.setEditable(false);
    }
    void DeshabilitarTablaReporte(){
       jTextArea1.setEditable(false);
       txtCodUsr.setEditable(false);
       
    }
    void DeshabilitarTablaJefFam(){
       txtNombreJF.setEditable(false);
       txtDirJF.setEditable(false);
       txtTelfJF.setEditable(false);
       txtCorreoJF.setEditable(false);
       txtZonaJF.setEditable(false);
       txtContraJF.setEditable(false);
       txtCIJFam.setEditable(false);
    }
    void HabilitarTablaJefeZona(boolean modContra){
       txtNombreJZ.setEditable(true);
       txtDirJZ.setEditable(true);
       txtTelfJZ.setEditable(true);
       txtCorreoJZ.setEditable(true);
       txtZonaJZ.setEditable(true);
       txtCIJZ.setEditable(true);
       if (modContra) txtContraJZ.setEditable(true);
       txtNombreJZ.requestFocus();
    }
    void HabilitarTablaJefFam(boolean modContra){
       txtNombreJF.setEditable(true);
       txtDirJF.setEditable(true);
       txtTelfJF.setEditable(true);
       txtCorreoJF.setEditable(true);
       txtZonaJF.setEditable(true);
       txtContraJF.setEditable(true);
       txtCIJFam.setEditable(true);
       if (modContra) txtContraJZ.setEditable(true);
       txtNombreJF.requestFocus();
    }
    void HabilitarTablaEncuestadores(boolean modPassword){
       txtNombreEnc.setEditable(true);
       txtDirEnc.setEditable(true);
       txtTelfEnc.setEditable(true);
       txtCorreoEnc.setEditable(true);
       txtZonaEnc.setEditable(true);
       txtCIEnc.setEditable(true);
       if (modPassword) txtContrEnc.setEditable(true);
       txtNombreEnc.requestFocus();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        PanelNavegador = new javax.swing.JPanel();
        BotonEncuestador = new javax.swing.JButton();
        BotonCenso = new javax.swing.JButton();
        BotonJefes = new javax.swing.JButton();
        BotonAtras = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        textNombre = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jButton21 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelJefe = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtNombreJZ = new javax.swing.JTextField();
        txtDirJZ = new javax.swing.JTextField();
        txtTelfJZ = new javax.swing.JTextField();
        txtCorreoJZ = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaJefeZona = new javax.swing.JTable();
        textNumJefesZona = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtZonaJZ = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtContraJZ = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txtCIJZ = new javax.swing.JTextField();
        panelEncuestador = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        textNumEnc = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtNombreEnc = new javax.swing.JTextField();
        txtDirEnc = new javax.swing.JTextField();
        txtTelfEnc = new javax.swing.JTextField();
        txtCorreoEnc = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaEncuestador = new javax.swing.JTable();
        jLabel24 = new javax.swing.JLabel();
        txtZonaEnc = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txtContrEnc = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        txtCIEnc = new javax.swing.JTextField();
        PanelJefesFam = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtNombreJF = new javax.swing.JTextField();
        txtDirJF = new javax.swing.JTextField();
        txtTelfJF = new javax.swing.JTextField();
        txtCorreoJF = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaJefeFamilia = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        txtZonaJF = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtContraJF = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        txtNumJefeFam = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        txtCIJFam = new javax.swing.JTextField();
        PanelViviendas = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaCenso = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        textNumViviendas = new javax.swing.JTextField();
        PanelReportes = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TablaReporte = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        txtCodUsr = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelNavegador.setBackground(new java.awt.Color(4, 22, 47));
        PanelNavegador.setForeground(new java.awt.Color(51, 102, 255));
        PanelNavegador.setPreferredSize(new java.awt.Dimension(210, 750));
        PanelNavegador.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BotonEncuestador.setBackground(new java.awt.Color(204, 255, 255));
        BotonEncuestador.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BotonEncuestador.setText("ENCUENTADORES");
        BotonEncuestador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonEncuestadorActionPerformed(evt);
            }
        });
        PanelNavegador.add(BotonEncuestador, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 170, 30));

        BotonCenso.setBackground(new java.awt.Color(204, 255, 255));
        BotonCenso.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BotonCenso.setText("VIVIENDAS CENSADAS");
        BotonCenso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonCensoActionPerformed(evt);
            }
        });
        PanelNavegador.add(BotonCenso, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 530, 170, 30));

        BotonJefes.setBackground(new java.awt.Color(204, 255, 255));
        BotonJefes.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BotonJefes.setText("JEFES DE ZONA");
        BotonJefes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonJefesActionPerformed(evt);
            }
        });
        PanelNavegador.add(BotonJefes, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 170, 30));

        BotonAtras.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BotonAtras.setForeground(new java.awt.Color(4, 22, 47));
        BotonAtras.setText("Atras");
        BotonAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonAtrasActionPerformed(evt);
            }
        });
        PanelNavegador.add(BotonAtras, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 690, -1, -1));

        jButton4.setBackground(new java.awt.Color(204, 255, 255));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setText("VER ESTADISTICOS");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        PanelNavegador.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 630, 170, 30));

        jButton5.setBackground(new java.awt.Color(204, 255, 255));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setText("JEFES DE FAMILIA");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        PanelNavegador.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, 170, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("NOMBRE ADMINISTRADOR:");
        PanelNavegador.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 170, -1));

        textNombre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textNombre.setBorder(null);
        PanelNavegador.add(textNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 170, 28));

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ADMINISTRADOR");
        PanelNavegador.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 150, -1));

        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Administrador.jpg"))); // NOI18N
        jLabel37.setText("\n\n");
        PanelNavegador.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 170, 160));

        jButton21.setBackground(new java.awt.Color(204, 255, 255));
        jButton21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton21.setText("VER REPORTES");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        PanelNavegador.add(jButton21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 580, 170, 30));

        jPanel1.add(PanelNavegador, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 750));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTabbedPane1.setToolTipText("");

        panelJefe.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setBackground(new java.awt.Color(4, 22, 47));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Nuevo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(4, 22, 47));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Guardar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(4, 22, 47));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Actualizar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(4, 22, 47));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Eliminar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(4, 22, 47));
        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Cerrar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Nombre");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Dirección");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Teléfono");

        txtNombreJZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreJZActionPerformed(evt);
            }
        });

        txtDirJZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDirJZActionPerformed(evt);
            }
        });

        txtCorreoJZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorreoJZActionPerformed(evt);
            }
        });

        tablaJefeZona.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaJefeZona.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tablaJefeZona.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaJefeZonaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaJefeZona);

        textNumJefesZona.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        textNumJefesZona.setBorder(null);
        textNumJefesZona.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textNumJefesZonaActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setText("Correo");

        txtZonaJZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtZonaJZActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setText("Zona Censal");

        txtContraJZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContraJZActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel26.setText("Contraseña");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel28.setText("TOTAL JEFES DE ZONA:");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel30.setText("JEFES DE ZONA");

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel31.setText("DATOS USUARIO");

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel33.setText("Carnet de ID");

        javax.swing.GroupLayout panelJefeLayout = new javax.swing.GroupLayout(panelJefe);
        panelJefe.setLayout(panelJefeLayout);
        panelJefeLayout.setHorizontalGroup(
            panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelJefeLayout.createSequentialGroup()
                .addGroup(panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelJefeLayout.createSequentialGroup()
                        .addGap(179, 179, 179)
                        .addComponent(jLabel28)
                        .addGap(35, 35, 35)
                        .addComponent(textNumJefesZona, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelJefeLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel31)
                        .addGap(236, 236, 236)
                        .addComponent(jLabel30))
                    .addGroup(panelJefeLayout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addGroup(panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelJefeLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton3)
                                .addGap(6, 6, 6)
                                .addComponent(jButton6)
                                .addGap(10, 10, 10)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelJefeLayout.createSequentialGroup()
                                .addGroup(panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel33)
                                    .addComponent(jLabel6))
                                .addGap(18, 18, 18)
                                .addGroup(panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNombreJZ, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                                    .addComponent(txtCIJZ)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelJefeLayout.createSequentialGroup()
                                .addGroup(panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel20))
                                .addGap(37, 37, 37)
                                .addGroup(panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCorreoJZ)
                                    .addComponent(txtDirJZ)
                                    .addComponent(txtTelfJZ)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelJefeLayout.createSequentialGroup()
                                .addGroup(panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel26))
                                .addGap(18, 18, 18)
                                .addGroup(panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtZonaJZ)
                                    .addComponent(txtContraJZ)))))
                    .addGroup(panelJefeLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 883, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        panelJefeLayout.setVerticalGroup(
            panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelJefeLayout.createSequentialGroup()
                .addGroup(panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelJefeLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel30))
                    .addGroup(panelJefeLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel31)))
                .addGap(16, 16, 16)
                .addGroup(panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCIJZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreJZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(14, 14, 14)
                .addGroup(panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(txtDirJZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTelfJZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(21, 21, 21)
                .addGroup(panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCorreoJZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addGap(21, 21, 21)
                .addGroup(panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtZonaJZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addGap(21, 21, 21)
                .addGroup(panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtContraJZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton6)
                    .addComponent(jButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelJefeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28)
                    .addComponent(textNumJefesZona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("JEFE ZONA", panelJefe);

        panelEncuestador.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setText("ENCUESTADORES");

        textNumEnc.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        textNumEnc.setBorder(null);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("DATOS USUARIO");

        jButton13.setBackground(new java.awt.Color(4, 22, 47));
        jButton13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("Nuevo");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setBackground(new java.awt.Color(4, 22, 47));
        jButton14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton14.setForeground(new java.awt.Color(255, 255, 255));
        jButton14.setText("Guardar");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(4, 22, 47));
        jButton15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton15.setForeground(new java.awt.Color(255, 255, 255));
        jButton15.setText("Actualizar");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setBackground(new java.awt.Color(4, 22, 47));
        jButton16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton16.setForeground(new java.awt.Color(255, 255, 255));
        jButton16.setText("Eliminar");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.setBackground(new java.awt.Color(4, 22, 47));
        jButton17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton17.setForeground(new java.awt.Color(255, 255, 255));
        jButton17.setText("Cerrar");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Nombre");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setText("Dirección");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("Teléfono");

        txtCorreoEnc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorreoEncActionPerformed(evt);
            }
        });

        tablaEncuestador.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaEncuestador.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tablaEncuestador.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaEncuestadorMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tablaEncuestador);

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel24.setText("Correo");

        txtZonaEnc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtZonaEncActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel25.setText("Zona Censal");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel27.setText("Contraseña");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel29.setText("TOTAL ENCUESTADORES:");

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel35.setText("Carnet de ID");

        javax.swing.GroupLayout panelEncuestadorLayout = new javax.swing.GroupLayout(panelEncuestador);
        panelEncuestador.setLayout(panelEncuestadorLayout);
        panelEncuestadorLayout.setHorizontalGroup(
            panelEncuestadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEncuestadorLayout.createSequentialGroup()
                .addGroup(panelEncuestadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEncuestadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelEncuestadorLayout.createSequentialGroup()
                            .addGap(16, 16, 16)
                            .addComponent(jLabel13)
                            .addGap(225, 225, 225)
                            .addComponent(jLabel5))
                        .addGroup(panelEncuestadorLayout.createSequentialGroup()
                            .addGap(210, 210, 210)
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(20, 20, 20)
                            .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(10, 10, 10)
                            .addComponent(jButton15)
                            .addGap(16, 16, 16)
                            .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(20, 20, 20)
                            .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelEncuestadorLayout.createSequentialGroup()
                            .addGap(160, 160, 160)
                            .addComponent(jLabel29)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(textNumEnc, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelEncuestadorLayout.createSequentialGroup()
                            .addGap(160, 160, 160)
                            .addGroup(panelEncuestadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(panelEncuestadorLayout.createSequentialGroup()
                                    .addComponent(jLabel35)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtCIEnc))
                                .addGroup(panelEncuestadorLayout.createSequentialGroup()
                                    .addComponent(jLabel14)
                                    .addGap(44, 44, 44)
                                    .addComponent(txtNombreEnc, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(panelEncuestadorLayout.createSequentialGroup()
                            .addGap(160, 160, 160)
                            .addGroup(panelEncuestadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(panelEncuestadorLayout.createSequentialGroup()
                                    .addComponent(jLabel24)
                                    .addGap(56, 56, 56)
                                    .addComponent(txtCorreoEnc))
                                .addGroup(panelEncuestadorLayout.createSequentialGroup()
                                    .addComponent(jLabel16)
                                    .addGap(41, 41, 41)
                                    .addComponent(txtTelfEnc))
                                .addGroup(panelEncuestadorLayout.createSequentialGroup()
                                    .addComponent(jLabel25)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtZonaEnc, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEncuestadorLayout.createSequentialGroup()
                            .addGap(156, 156, 156)
                            .addGroup(panelEncuestadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEncuestadorLayout.createSequentialGroup()
                                    .addComponent(jLabel15)
                                    .addGap(37, 37, 37)
                                    .addComponent(txtDirEnc, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEncuestadorLayout.createSequentialGroup()
                                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtContrEnc, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(panelEncuestadorLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 900, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(52, 52, 52))
        );
        panelEncuestadorLayout.setVerticalGroup(
            panelEncuestadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEncuestadorLayout.createSequentialGroup()
                .addGroup(panelEncuestadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEncuestadorLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel5))
                    .addGroup(panelEncuestadorLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel13)))
                .addGap(11, 11, 11)
                .addGroup(panelEncuestadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35)
                    .addComponent(txtCIEnc, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelEncuestadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(txtNombreEnc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(panelEncuestadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(txtDirEnc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(panelEncuestadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(txtTelfEnc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(panelEncuestadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCorreoEnc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addGap(21, 21, 21)
                .addGroup(panelEncuestadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtZonaEnc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addGap(16, 16, 16)
                .addGroup(panelEncuestadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtContrEnc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(panelEncuestadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton13)
                    .addComponent(jButton14)
                    .addComponent(jButton15)
                    .addComponent(jButton16)
                    .addComponent(jButton17))
                .addGap(17, 17, 17)
                .addGroup(panelEncuestadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(textNumEnc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11))
        );

        jTabbedPane1.addTab("ENCUESTADOR", panelEncuestador);

        PanelJefesFam.setBackground(new java.awt.Color(255, 255, 255));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel17.setText("JEFES DE FAMILIA");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("DATOS USUARIO");

        jButton8.setBackground(new java.awt.Color(4, 22, 47));
        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Nuevo");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(4, 22, 47));
        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Guardar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(4, 22, 47));
        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("Actualizar");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setBackground(new java.awt.Color(4, 22, 47));
        jButton11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Eliminar");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(4, 22, 47));
        jButton12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("Cerrar");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Nombre");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Direccion");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Telefono");

        txtNombreJF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreJFActionPerformed(evt);
            }
        });

        txtCorreoJF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorreoJFActionPerformed(evt);
            }
        });

        TablaJefeFamilia.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TablaJefeFamilia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "codUsr", "Title 2", "Title 3", "Title 4"
            }
        ));
        TablaJefeFamilia.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TablaJefeFamilia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaJefeFamiliaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TablaJefeFamilia);

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel22.setText("Correo");

        txtZonaJF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtZonaJFActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel23.setText("Zona Censal");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setText("Contraseña");

        txtContraJF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContraJFActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel32.setText("TOTAL JEFES DE FAMILIA :");

        txtNumJefeFam.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtNumJefeFam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumJefeFamActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel34.setText("Carnet de ID");

        javax.swing.GroupLayout PanelJefesFamLayout = new javax.swing.GroupLayout(PanelJefesFam);
        PanelJefesFam.setLayout(PanelJefesFamLayout);
        PanelJefesFamLayout.setHorizontalGroup(
            PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(PanelJefesFamLayout.createSequentialGroup()
                .addGroup(PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelJefesFamLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel9)
                        .addGap(235, 235, 235)
                        .addComponent(jLabel17))
                    .addGroup(PanelJefesFamLayout.createSequentialGroup()
                        .addGap(162, 162, 162)
                        .addGroup(PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelJefesFamLayout.createSequentialGroup()
                                .addGroup(PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel18))
                                .addGap(18, 18, 18)
                                .addGroup(PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtZonaJF)
                                    .addComponent(txtContraJF, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(PanelJefesFamLayout.createSequentialGroup()
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(35, 35, 35)
                                    .addComponent(txtCorreoJF, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(PanelJefesFamLayout.createSequentialGroup()
                                    .addGroup(PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(PanelJefesFamLayout.createSequentialGroup()
                                            .addComponent(jButton8)
                                            .addGap(20, 20, 20))
                                        .addGroup(PanelJefesFamLayout.createSequentialGroup()
                                            .addComponent(jLabel32)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                    .addGroup(PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(PanelJefesFamLayout.createSequentialGroup()
                                            .addComponent(jButton9)
                                            .addGap(9, 9, 9)
                                            .addComponent(jButton10)
                                            .addGap(26, 26, 26)
                                            .addComponent(jButton11)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(PanelJefesFamLayout.createSequentialGroup()
                                            .addGap(6, 6, 6)
                                            .addComponent(txtNumJefeFam, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelJefesFamLayout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtTelfJF, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelJefesFamLayout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtDirJF, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelJefesFamLayout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtNombreJF, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelJefesFamLayout.createSequentialGroup()
                                        .addComponent(jLabel34)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtCIJFam, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(185, Short.MAX_VALUE))
            .addGroup(PanelJefesFamLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 907, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelJefesFamLayout.setVerticalGroup(
            PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelJefesFamLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelJefesFamLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(44, 44, 44))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelJefesFamLayout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(18, 18, 18)))
                .addGroup(PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCIJFam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34))
                .addGap(10, 10, 10)
                .addGroup(PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreJF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(10, 10, 10)
                .addGroup(PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDirJF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(10, 10, 10)
                .addGroup(PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTelfJF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(10, 10, 10)
                .addGroup(PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCorreoJF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addGap(10, 10, 10)
                .addGroup(PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtZonaJF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addGap(10, 10, 10)
                .addGroup(PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtContraJF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton8)
                    .addComponent(jButton9)
                    .addComponent(jButton10)
                    .addGroup(PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton11)
                        .addComponent(jButton12)))
                .addGap(6, 6, 6)
                .addGroup(PanelJefesFamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(txtNumJefeFam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(107, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("JEFE FAMILIA", PanelJefesFam);

        PanelViviendas.setBackground(new java.awt.Color(255, 255, 255));
        PanelViviendas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaCenso.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "codVivienda", "comunidad", "localidad", "barrio", "calle", "numPuerta", "totalPersonas"
            }
        ));
        jScrollPane3.setViewportView(tablaCenso);

        PanelViviendas.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 840, 570));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("VIVIENDAS CENSADAS:");
        PanelViviendas.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 160, -1));

        textNumViviendas.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        textNumViviendas.setBorder(null);
        textNumViviendas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textNumViviendasActionPerformed(evt);
            }
        });
        PanelViviendas.add(textNumViviendas, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 132, 20));

        jTabbedPane1.addTab("VIVIENDA", PanelViviendas);

        PanelReportes.setBackground(new java.awt.Color(255, 255, 255));
        PanelReportes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel19.setText("REPORTES");
        PanelReportes.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, 120, 50));

        TablaReporte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TablaReporte.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaReporteMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(TablaReporte);

        PanelReportes.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 350, 790, 260));

        jTextArea1.setBackground(new java.awt.Color(204, 204, 204));
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane6.setViewportView(jTextArea1);

        PanelReportes.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 790, 260));

        txtCodUsr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodUsrActionPerformed(evt);
            }
        });
        PanelReportes.add(txtCodUsr, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, 130, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Carnet de Usr:");
        PanelReportes.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, -1, -1));

        jTabbedPane1.addTab("REPORTE", PanelReportes);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jButton18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton18.setText("POBLACIÓN GENERAL");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton19.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton19.setText("CARATERISTICAS DE VIVIENDAS");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton20.setText("DIFICULTADES, MORTALIDAD, EXTERIOR");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(0, 51, 153));
        jLabel38.setText("ESTADISTICOS DE CENSO BOLIVIA");
        jLabel38.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Estadistico.jpg"))); // NOI18N

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel40.setText("Si quiere ver los graficos estadisticos pulse los siguientes botones.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(jButton18)
                        .addGap(36, 36, 36)
                        .addComponent(jButton20)
                        .addGap(32, 32, 32)
                        .addComponent(jButton19))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(332, 332, 332)
                        .addComponent(jLabel38))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(314, 314, 314)
                        .addComponent(jLabel39))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jLabel40)))
                .addContainerGap(79, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel38)
                .addGap(18, 18, 18)
                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel40)
                .addGap(46, 46, 46)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton18)
                    .addComponent(jButton20)
                    .addComponent(jButton19))
                .addGap(261, 261, 261))
        );

        jTabbedPane1.addTab("ESTADISTICAS", jPanel2);

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 80, 970, 670));

        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Sin título-2.jpg"))); // NOI18N
        jPanel1.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 970, 81));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void BotonEncuestadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonEncuestadorActionPerformed
        jTabbedPane1.setSelectedIndex(1);
        jTabbedPane1.getSelectedIndex();
    }//GEN-LAST:event_BotonEncuestadorActionPerformed

    private void BotonAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonAtrasActionPerformed
        //Boton atras
           new InicioSesion().setVisible(true);
           this.setVisible(false);
    }//GEN-LAST:event_BotonAtrasActionPerformed

    private void BotonCensoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonCensoActionPerformed
        jTabbedPane1.setSelectedIndex(3);
        jTabbedPane1.getSelectedIndex();
    }//GEN-LAST:event_BotonCensoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // BOTON NUEVO JEFE ZONA
        LimpiarTablas();
        HabilitarTablaJefeZona(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // BOTON GUARDAR JEFE DE ZONA
        guardar(txtCIJZ,"Jefe de Zona", txtNombreJZ, txtDirJZ, txtTelfJZ, txtCorreoJZ,
                txtZonaJZ, txtContraJZ, tablaJefeZona);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // BOTON ACTUALIZAR JEFE DE ZONA
        try {
            String sql = "UPDATE usuario set codUsr=?, nombreUsr=?, dirUsr=?, telfUsr=?, correoUsr=?, ZonaCensal=?"
            + "where codUsr=?";
            int fila = tablaJefeZona.getSelectedRow();
            String id = (String)tablaJefeZona.getValueAt(fila, 0);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtCIJZ.getText());
            ps.setString(2, txtNombreJZ.getText());
            ps.setString(3, txtDirJZ.getText());
            ps.setString(4, txtTelfJZ.getText());
            ps.setString(5, txtCorreoJZ.getText());
            ps.setString(6, txtZonaJZ.getText());
            ps.setString(7, id);
            int n = ps.executeUpdate();
            if(n>0){ // Si se ejecuto
                JOptionPane.showMessageDialog(null,"Registro modificado");
                LimpiarTablas();
                LlenarTablas();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error"+ e.getMessage());
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // BOTON ELIMINAR JEFE DE ZONA
        try {
            int fila = tablaJefeZona.getSelectedRow();
            String sql = "DELETE from usuario where codUsr="+tablaJefeZona.getValueAt(fila, 0);
            sent=conn.createStatement();
            int n = sent.executeUpdate(sql);
            if(n>0){
                JOptionPane.showMessageDialog(null, "Registro Eliminado");
                //llenarTablaJefeZona();
                LlenarTablas();
                LimpiarTablas();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error"+ e.getMessage());
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // BOTON CERRAR SESIÓN JEFE DE ZONA
        cerrar();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void tablaJefeZonaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaJefeZonaMouseClicked
        // MOUSE CLICKED DE TABLA JEFE DE ZONA
        if (evt.getButton()==1){
            int fila = tablaJefeZona.getSelectedRow();
            try {
                HabilitarTablaJefeZona(false);
                String sql = "SELECT codUsr,nombreUsr,dirUsr,telfUsr,correoUsr,contrUsr,ZonaCensal from usuario where codUsr="+tablaJefeZona.getValueAt(fila,0);
                sent = conn.createStatement();
                ResultSet rs = sent.executeQuery(sql);
                rs.next(); // SI O SI PONER PARA LO RESULTADOS
                txtCIJZ.setText(rs.getString("CodUsr"));
                txtNombreJZ.setText(rs.getString("nombreUsr"));
                txtDirJZ.setText(rs.getString("dirUsr"));
                txtTelfJZ.setText(rs.getString("telfUsr"));
                txtCorreoJZ.setText(rs.getString("correoUsr"));
                txtZonaJZ.setText(rs.getString("ZonaCensal"));
                txtContraJZ.setText(rs.getString("contrUsr"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_tablaJefeZonaMouseClicked

    private void txtCorreoJZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorreoJZActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoJZActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // BOTON NUEVO ENCUESTADOR
        LimpiarTablas();
        HabilitarTablaEncuestadores(true);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // BOTON GUARDAR ENCUESTADOR
        guardar(txtCIEnc, "Encuestador", txtNombreEnc, txtDirEnc, txtTelfEnc, txtCorreoEnc,
                txtZonaEnc, txtContrEnc, tablaEncuestador);
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // BOTON ACTUALIZAR ENCUESTADOR
        try {
            String sql = "UPDATE usuario set codUsr=?, nombreUsr=?, dirUsr=?, telfUsr=?, correoUsr=?, ZonaCensal=?"
            + "where codUsr=?";
            int fila = tablaEncuestador.getSelectedRow();
            String id = (String)tablaEncuestador.getValueAt(fila, 0);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtCIEnc.getText());
            ps.setString(2, txtNombreEnc.getText());
            ps.setString(3, txtDirEnc.getText());
            ps.setString(4, txtTelfEnc.getText());
            ps.setString(5, txtCorreoEnc.getText());
            ps.setString(6, txtZonaEnc.getText());
            ps.setString(7, id);
            int n = ps.executeUpdate();
            if(n>0){ // Si se ejecuto
                JOptionPane.showMessageDialog(null,"Registro modificado");
                LimpiarTablas();
                LlenarTablas();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error"+ e.getMessage());
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        //BOTÓN ELIMINAR ENCUESTADOR
        try {
            int fila = tablaEncuestador.getSelectedRow();
            String sql = "DELETE from usuario where codUsr="+tablaEncuestador.getValueAt(fila, 0);
            sent=conn.createStatement();
            int n = sent.executeUpdate(sql);
            if(n>0){
                JOptionPane.showMessageDialog(null, "Registro Eliminado");
                LlenarTablas();
                LimpiarTablas();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error"+ e.getMessage());
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // BOTON CERRAR SESIÓN ENCUESTADOR
        cerrar();
    }//GEN-LAST:event_jButton17ActionPerformed

    private void txtCorreoEncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorreoEncActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoEncActionPerformed

    private void tablaEncuestadorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaEncuestadorMouseClicked
       // MOUSE CLICKED DE TABLA ENCUESTADOR
        if (evt.getButton()==1){
            int fila = tablaEncuestador.getSelectedRow();
            try {
                HabilitarTablaEncuestadores(false);
                String sql = "SELECT codUsr,nombreUsr,dirUsr,telfUsr,correoUsr,contrUsr,ZonaCensal from usuario where codUsr="+tablaEncuestador.getValueAt(fila,0);
                sent = conn.createStatement();
                ResultSet rs = sent.executeQuery(sql);
                rs.next(); // SI O SI PONER PARA LO RESULTADOS
                txtCIEnc.setText(rs.getString("CodUsr"));
                txtNombreEnc.setText(rs.getString("nombreUsr"));
                txtDirEnc.setText(rs.getString("dirUsr"));
                txtTelfEnc.setText(rs.getString("telfUsr"));
                txtCorreoEnc.setText(rs.getString("correoUsr"));
                txtZonaEnc.setText(rs.getString("ZonaCensal"));
                txtContrEnc.setText(rs.getString("contrUsr"));
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_tablaEncuestadorMouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jTabbedPane1.setSelectedIndex(5);
        jTabbedPane1.getSelectedIndex();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void BotonJefesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonJefesActionPerformed
        jTabbedPane1.setSelectedIndex(0);
        jTabbedPane1.getSelectedIndex();
    }//GEN-LAST:event_BotonJefesActionPerformed

    private void textNumJefesZonaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textNumJefesZonaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textNumJefesZonaActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jTabbedPane1.setSelectedIndex(2);
        jTabbedPane1.getSelectedIndex();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void TablaJefeFamiliaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaJefeFamiliaMouseClicked
if (evt.getButton()==1){
            int fila = TablaJefeFamilia.getSelectedRow();
            try {
                HabilitarTablaJefFam(false);
                String sql = "SELECT codUsr,nombreUsr,dirUsr,telfUsr,correoUsr,contrUsr,ZonaCensal from usuario where  codUsr="+TablaJefeFamilia.getValueAt(fila,0);
                sent = conn.createStatement();
                ResultSet rs = sent.executeQuery(sql);
                rs.next(); // SI O SI PONER PARA LO RESULTADOS
                txtCIJFam.setText(rs.getString("CodUsr"));
                txtNombreJF.setText(rs.getString("nombreUsr"));
                txtDirJF.setText(rs.getString("dirUsr"));
                txtTelfJF.setText(rs.getString("telfUsr"));
                txtCorreoJF.setText(rs.getString("correoUsr"));
                txtZonaJF.setText(rs.getString("ZonaCensal"));
                txtContraJF.setText(rs.getString("contrUsr"));
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_TablaJefeFamiliaMouseClicked

    private void txtCorreoJFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorreoJFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoJFActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // CERRAR:
        cerrar();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // // ELIMINAR JEFE FAMILIA
        try {
            int fila = TablaJefeFamilia.getSelectedRow();
            String sql = "DELETE from usuario where codUsr="+TablaJefeFamilia.getValueAt(fila, 0);
            sent=conn.createStatement();
            int n = sent.executeUpdate(sql);
            if(n>0){
                JOptionPane.showMessageDialog(null, "Registro Eliminado");
                LlenarTablas();
                LimpiarTablas();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error"+ e.getMessage());
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // ACTUALIZAR JEFE FAMILIA
        try {
            String sql = "UPDATE usuario set codUsr=?, nombreUsr=?, dirUsr=?, telfUsr=?, correoUsr=?, ZonaCensal=? ,contrUsr=?"
            + "where codUsr=?";
            int fila = TablaJefeFamilia.getSelectedRow();
            String id = (String)TablaJefeFamilia.getValueAt(fila, 0);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtCIJFam.getText());
            ps.setString(2, txtNombreJF.getText());
            ps.setString(3, txtDirJF.getText());
            ps.setString(4, txtTelfJF.getText());
            ps.setString(5, txtCorreoJF.getText());
            ps.setString(6, txtZonaJF.getText());
            ps.setString(7, txtContraJF.getText());
            ps.setString(8, id);
            int n = ps.executeUpdate();
            if(n>0){ // Si se ejecuto
                JOptionPane.showMessageDialog(null,"Registro modificado");
                LimpiarTablas();
                LlenarTablas();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error"+ e.getMessage());
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // BOTON GUARDAR JEFE FAMILIA
        guardar(txtCIJFam,"Jefe de Familia", txtNombreJF, txtDirJF, txtTelfJF, txtCorreoJF,
                txtZonaJF, txtContraJF, TablaJefeFamilia);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // BOTON NUEVO JEFE DE FAMILIA
        LimpiarTablas();
        HabilitarTablaJefFam(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void txtZonaEncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtZonaEncActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtZonaEncActionPerformed

    private void TablaReporteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaReporteMouseClicked
 
        if (evt.getButton()==1){
            int fila = TablaReporte.getSelectedRow();
            try {
               
                String sql = "SELECT codUsr,reporte from reporte where codUsr ="+TablaReporte.getValueAt(fila,0);
                sent = conn.createStatement();
                ResultSet rs = sent.executeQuery(sql);
                rs.next(); // SI O SI PONER PARA LO RESULTADOS
                jTextArea1.setText(rs.getString("reporte"));
                txtCodUsr.setText(rs.getString ("codUsr"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
                                                     // TODO add your handling code here:
    }//GEN-LAST:event_TablaReporteMouseClicked

    private void txtCodUsrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodUsrActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodUsrActionPerformed

    private void txtNombreJZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreJZActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreJZActionPerformed

    private void txtNombreJFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreJFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreJFActionPerformed

    private void txtContraJFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContraJFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContraJFActionPerformed

    private void txtDirJZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDirJZActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDirJZActionPerformed

    private void txtContraJZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContraJZActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContraJZActionPerformed

    private void txtZonaJFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtZonaJFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtZonaJFActionPerformed

    private void txtNumJefeFamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumJefeFamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumJefeFamActionPerformed

    private void textNumViviendasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textNumViviendasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textNumViviendasActionPerformed

    private void txtZonaJZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtZonaJZActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtZonaJZActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // VER ESTADISTICOS DE POBLACION GENERAL
        new EstPoblacionGeneral().setVisible(true);
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
        new EstadisticasCapB().setVisible(true);
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
        new Estadistica_capC().setVisible(true);
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(4);
        jTabbedPane1.getSelectedIndex();
    }//GEN-LAST:event_jButton21ActionPerformed

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
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Administrador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonAtras;
    private javax.swing.JButton BotonCenso;
    private javax.swing.JButton BotonEncuestador;
    private javax.swing.JButton BotonJefes;
    private javax.swing.JPanel PanelJefesFam;
    private javax.swing.JPanel PanelNavegador;
    private javax.swing.JPanel PanelReportes;
    private javax.swing.JPanel PanelViviendas;
    private javax.swing.JTable TablaJefeFamilia;
    private javax.swing.JTable TablaReporte;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JPanel panelEncuestador;
    private javax.swing.JPanel panelJefe;
    private javax.swing.JTable tablaCenso;
    private javax.swing.JTable tablaEncuestador;
    private javax.swing.JTable tablaJefeZona;
    private javax.swing.JTextField textNombre;
    private javax.swing.JTextField textNumEnc;
    private javax.swing.JTextField textNumJefesZona;
    private javax.swing.JTextField textNumViviendas;
    private javax.swing.JTextField txtCIEnc;
    private javax.swing.JTextField txtCIJFam;
    private javax.swing.JTextField txtCIJZ;
    private javax.swing.JTextField txtCodUsr;
    private javax.swing.JTextField txtContrEnc;
    private javax.swing.JTextField txtContraJF;
    private javax.swing.JTextField txtContraJZ;
    private javax.swing.JTextField txtCorreoEnc;
    private javax.swing.JTextField txtCorreoJF;
    private javax.swing.JTextField txtCorreoJZ;
    private javax.swing.JTextField txtDirEnc;
    private javax.swing.JTextField txtDirJF;
    private javax.swing.JTextField txtDirJZ;
    private javax.swing.JTextField txtNombreEnc;
    private javax.swing.JTextField txtNombreJF;
    private javax.swing.JTextField txtNombreJZ;
    private javax.swing.JTextField txtNumJefeFam;
    private javax.swing.JTextField txtTelfEnc;
    private javax.swing.JTextField txtTelfJF;
    private javax.swing.JTextField txtTelfJZ;
    private javax.swing.JTextField txtZonaEnc;
    private javax.swing.JTextField txtZonaJF;
    private javax.swing.JTextField txtZonaJZ;
    // End of variables declaration//GEN-END:variables
}
