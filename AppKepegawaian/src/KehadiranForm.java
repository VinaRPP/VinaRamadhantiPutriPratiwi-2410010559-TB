/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author ACER
 */
public class KehadiranForm extends javax.swing.JFrame {
    private DefaultTableModel model;

    /**
     * Creates new form KehadiranForm
     */
    public KehadiranForm() {
        initComponents();
        getContentPane().setBackground(new java.awt.Color(0, 212, 199));
        model = (DefaultTableModel) tblKehadiran.getModel();
        loadPegawai();
        loadStatus();
        loadData();
    }
// 🔹 Load Data Pegawai ke JComboBox
    private void loadPegawai() {
        try (Connection con = Koneksi.getKoneksi()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_pegawai, nama FROM pegawai");
            cmbPegawai.removeAllItems();
            while (rs.next()) {
                cmbPegawai.addItem(rs.getInt("id_pegawai") + " - " + rs.getString("nama"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
     // 🔹 Load Data Status ke JComboBox
    private void loadStatus() {
        cmbStatus.removeAllItems();
        cmbStatus.addItem("Hadir");
        cmbStatus.addItem("Izin");
        cmbStatus.addItem("Sakit");
        cmbStatus.addItem("Alpha");
    }

    // 🔹 Load Data Kehadiran ke JTable
    private void loadData() {
        model.setRowCount(0);
        try (Connection con = Koneksi.getKoneksi()) {
            String sql = "SELECT kehadiran.id_kehadiran, pegawai.nama, kehadiran.tanggal, kehadiran.status " +
                         "FROM kehadiran JOIN pegawai ON kehadiran.id_pegawai = pegawai.id_pegawai";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_kehadiran"),
                    rs.getString("nama"),
                    rs.getString("tanggal"),
                    rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Tambah Kehadiran
    private void tambahData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = sdf.format(txtTanggal.getDate());
        String selectedPegawai = cmbPegawai.getSelectedItem().toString();
        String idPegawai = selectedPegawai.split(" - ")[0];
        String status = cmbStatus.getSelectedItem().toString();

        try (Connection con = Koneksi.getKoneksi()) {
            String sql = "INSERT INTO kehadiran (id_pegawai, tanggal, status) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(idPegawai));
            ps.setString(2, tanggal);
            ps.setString(3, status);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Kehadiran Berhasil Ditambahkan!");
            loadData();
            resetForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Edit Kehadiran
    private void editData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = sdf.format(txtTanggal.getDate());
        String selectedPegawai = cmbPegawai.getSelectedItem().toString();
        String idPegawai = selectedPegawai.split(" - ")[0];
        String status = cmbStatus.getSelectedItem().toString();

        try (Connection con = Koneksi.getKoneksi()) {
            String sql = "UPDATE kehadiran SET id_pegawai=?, tanggal=?, status=? WHERE id_kehadiran=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(idPegawai));
            ps.setString(2, tanggal);
            ps.setString(3, status);
            ps.setInt(4, Integer.parseInt(txtIdKehadiran.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Kehadiran Berhasil Diperbarui!");
            loadData();
            resetForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Hapus Kehadiran
    private void hapusData() {
        try (Connection con = Koneksi.getKoneksi()) {
            String sql = "DELETE FROM kehadiran WHERE id_kehadiran=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(txtIdKehadiran.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Kehadiran Berhasil Dihapus!");
            loadData();
            resetForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Reset Form
    private void resetForm() {
        txtIdKehadiran.setText("");
        cmbPegawai.setSelectedIndex(0);
        txtTanggal.setDate(null);
        cmbStatus.setSelectedIndex(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtTanggal = new com.toedter.calendar.JDateChooser();
        cmbPegawai = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtIdKehadiran = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKehadiran = new javax.swing.JTable();
        cmbStatus = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtTanggal.setToolTipText("Pilih Tanggal");

        cmbPegawai.setBackground(java.awt.SystemColor.controlShadow);
        cmbPegawai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbPegawai.setToolTipText("Pilih Nama");

        jLabel1.setText("ID");

        jLabel3.setText("Nama");

        jLabel4.setText("Tanggal");

        txtIdKehadiran.setEditable(false);
        txtIdKehadiran.setText("ID");
        txtIdKehadiran.setToolTipText("");

        tblKehadiran.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nama", "Tanggal", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKehadiran.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKehadiranMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblKehadiran);

        cmbStatus.setBackground(java.awt.SystemColor.controlShadow);
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbStatus.setToolTipText("Pilih Status ");

        jLabel5.setText("Status");

        btnEdit.setBackground(java.awt.SystemColor.controlHighlight);
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnHapus.setBackground(java.awt.SystemColor.controlHighlight);
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnReset.setBackground(java.awt.SystemColor.controlHighlight);
        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        btnTambah.setBackground(java.awt.SystemColor.activeCaptionBorder);
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Trajan Pro", 1, 14)); // NOI18N
        jLabel6.setText("DATA KEHADIRAN");

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel5))
                                .addGap(55, 55, 55)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtIdKehadiran, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(0, 13, Short.MAX_VALUE)
                                                .addComponent(jLabel6)
                                                .addGap(73, 73, 73)))
                                        .addComponent(jLabel7))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(btnTambah)
                                .addGap(18, 18, 18)
                                .addComponent(btnEdit)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnHapus)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnReset)))
                        .addGap(11, 11, 11)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtIdKehadiran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambah)
                    .addComponent(btnEdit)
                    .addComponent(btnHapus)
                    .addComponent(btnReset))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblKehadiranMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKehadiranMouseClicked
        // TODO add your handling code here
        int row = tblKehadiran.getSelectedRow();
        txtIdKehadiran.setText(tblKehadiran.getValueAt(row, 0).toString());
        
        String namaPegawai = tblKehadiran.getValueAt(row, 1).toString();
        for (int i = 0; i < cmbPegawai.getItemCount(); i++) {
            if (cmbPegawai.getItemAt(i).contains(" - " + namaPegawai)) {
                cmbPegawai.setSelectedIndex(i);
                break;
            }
        }

        try {
            java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tblKehadiran.getValueAt(row, 2).toString());
            txtTanggal.setDate(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cmbStatus.setSelectedItem(tblKehadiran.getValueAt(row, 3).toString());
    }//GEN-LAST:event_tblKehadiranMouseClicked

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        editData();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        hapusData();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        resetForm();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        tambahData();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        // TODO add your handling code here:
        this.setVisible(false);  // Sembunyikan Menu Utama
        MenuUtama mu = new MenuUtama();
        mu.setVisible(true);
    }//GEN-LAST:event_jLabel7MouseClicked

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
            java.util.logging.Logger.getLogger(KehadiranForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(KehadiranForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(KehadiranForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(KehadiranForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new KehadiranForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnTambah;
    private javax.swing.JComboBox<String> cmbPegawai;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblKehadiran;
    private javax.swing.JTextField txtIdKehadiran;
    private com.toedter.calendar.JDateChooser txtTanggal;
    // End of variables declaration//GEN-END:variables
}
