/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ACER
 */
public class LaporanForm extends javax.swing.JFrame implements Printable {
    private DefaultTableModel modelPegawai;
    private DefaultTableModel modelKehadiran;
    private String laporanTipe; // Menyimpan tipe laporan yang akan dicetak (Pegawai atau Kehadiran)

    /**
     * Creates new form LaporanForm
     */
    public LaporanForm() {
        initComponents();
        getContentPane().setBackground(new java.awt.Color(0, 212, 199)); 
        modelPegawai = (DefaultTableModel) tblPegawai.getModel();
        modelKehadiran = (DefaultTableModel) tblKehadiran.getModel();
        loadPegawai();
        loadKehadiran();
    }
    
    // 🔹 Load Data Pegawai dengan JOIN ke Jabatan
    private void loadPegawai() {
    modelPegawai.setRowCount(0);
    try (Connection con = Koneksi.getKoneksi()) {
        String sql = "SELECT p.id_pegawai, p.nama, j.nama_jabatan, j.gaji " +
                     "FROM pegawai p " +
                     "JOIN jabatan j ON p.id_jabatan = j.id_jabatan";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            modelPegawai.addRow(new Object[]{
                rs.getInt("id_pegawai"),
                rs.getString("nama"),
                rs.getString("nama_jabatan"),
                rs.getDouble("gaji")
            });
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}


    // 🔹 Load Data Kehadiran dengan JOIN ke Pegawai
    private void loadKehadiran() {
        modelKehadiran.setRowCount(0);
        try (Connection con = Koneksi.getKoneksi()) {
            String sql = "SELECT kehadiran.id_kehadiran, pegawai.nama, kehadiran.tanggal, kehadiran.status " +
                         "FROM kehadiran JOIN pegawai ON kehadiran.id_pegawai = pegawai.id_pegawai";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                modelKehadiran.addRow(new Object[]{
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

    // 🔹 Filter Kehadiran Berdasarkan Tanggal
    private void filterKehadiran() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = sdf.format(txtTanggal.getDate());

        modelKehadiran.setRowCount(0);
        try (Connection con = Koneksi.getKoneksi()) {
            String sql = "SELECT kehadiran.id_kehadiran, pegawai.nama, kehadiran.tanggal, kehadiran.status " +
                         "FROM kehadiran JOIN pegawai ON kehadiran.id_pegawai = pegawai.id_pegawai " +
                         "WHERE kehadiran.tanggal = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tanggal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelKehadiran.addRow(new Object[]{
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
// 🔹 Fungsi Cetak Laporan (Pegawai atau Kehadiran)
    private void cetakLaporan(String tipe) {
        laporanTipe = tipe; // Tentukan jenis laporan yang akan dicetak
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Laporan " + tipe);

        job.setPrintable(this);
        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        int y = 100; // Posisi awal cetak

        if (laporanTipe.equals("Pegawai")) {
            g.drawString("=== LAPORAN DATA PEGAWAI ===", 100, y);
            y += 20;
            g.drawString("ID     Nama     Jabatan     Gaji", 100, y);
            g.drawLine(100, y + 5, 400, y + 5);
            y += 20;

            for (int i = 0; i < modelPegawai.getRowCount(); i++) {
                g.drawString(
                    modelPegawai.getValueAt(i, 0) + "  " +
                    modelPegawai.getValueAt(i, 1) + "  " +
                    modelPegawai.getValueAt(i, 2) + "  " +
                    modelPegawai.getValueAt(i, 3), 100, y);
                y += 20;
            }
        } else if (laporanTipe.equals("Kehadiran")) {
            g.drawString("=== LAPORAN KEHADIRAN ===", 100, y);
            y += 20;
            g.drawString("ID     Nama     Tanggal     Status", 100, y);
            g.drawLine(100, y + 5, 400, y + 5);
            y += 20;

            for (int i = 0; i < modelKehadiran.getRowCount(); i++) {
                g.drawString(
                    modelKehadiran.getValueAt(i, 0) + "  " +
                    modelKehadiran.getValueAt(i, 1) + "  " +
                    modelKehadiran.getValueAt(i, 2) + "  " +
                    modelKehadiran.getValueAt(i, 3), 100, y);
                y += 20;
            }
        }

        return PAGE_EXISTS;
    }
    /* 🔹 Cetak Laporan Pegawai
    private void cetakLaporanPegawai() {
        StringBuilder laporan = new StringBuilder();
        laporan.append("=== LAPORAN DATA PEGAWAI ===\n");
        laporan.append("ID\tNama\tJabatan\tGaji\n");
        laporan.append("---------------------------------\n");

        for (int i = 0; i < modelPegawai.getRowCount(); i++) {
            laporan.append(modelPegawai.getValueAt(i, 0)).append("\t")
                   .append(modelPegawai.getValueAt(i, 1)).append("\t")
                   .append(modelPegawai.getValueAt(i, 2)).append("\t")
                   .append(modelPegawai.getValueAt(i, 3)).append("\n");
        }

        JOptionPane.showMessageDialog(this, laporan.toString(), "Laporan Pegawai", JOptionPane.INFORMATION_MESSAGE);
    }

    // 🔹 Cetak Laporan Kehadiran
    private void cetakLaporanKehadiran() {
        StringBuilder laporan = new StringBuilder();
        laporan.append("=== LAPORAN KEHADIRAN ===\n");
        laporan.append("ID\tNama\tTanggal\tStatus\n");
        laporan.append("---------------------------------\n");

        for (int i = 0; i < modelKehadiran.getRowCount(); i++) {
            laporan.append(modelKehadiran.getValueAt(i, 0)).append("\t")
                   .append(modelKehadiran.getValueAt(i, 1)).append("\t")
                   .append(modelKehadiran.getValueAt(i, 2)).append("\t")
                   .append(modelKehadiran.getValueAt(i, 3)).append("\n");
        }

        JOptionPane.showMessageDialog(this, laporan.toString(), "Laporan Kehadiran", JOptionPane.INFORMATION_MESSAGE);
    }*/
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPegawai = new javax.swing.JTable();
        txtTanggal = new com.toedter.calendar.JDateChooser();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblKehadiran = new javax.swing.JTable();
        btnFilter = new javax.swing.JButton();
        btnCetakPegawai = new javax.swing.JButton();
        btnCetakKehadiran = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 212, 199));
        setForeground(javax.swing.UIManager.getDefaults().getColor("InternalFrame.activeTitleGradient"));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Daftar Pegawai");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Laporan Kehadiran");

        jLabel3.setText("Filter Tanggal");

        tblPegawai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "NAMA", "JABATAN", "GAJI"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblPegawai);

        tblKehadiran.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "NAMA", "TANGGAL", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblKehadiran);

        btnFilter.setText("FILTER");
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });

        btnCetakPegawai.setText("CetakPegawai");
        btnCetakPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakPegawaiActionPerformed(evt);
            }
        });

        btnCetakKehadiran.setText("CetakKehadiran");
        btnCetakKehadiran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakKehadiranActionPerformed(evt);
            }
        });

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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnFilter))
                                    .addComponent(btnCetakKehadiran)))
                            .addComponent(btnCetakPegawai))
                        .addGap(0, 24, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(21, 21, 21))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCetakPegawai)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(btnFilter)
                    .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(btnCetakKehadiran)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        // TODO add your handling code here:
        filterKehadiran();
    }//GEN-LAST:event_btnFilterActionPerformed

    private void btnCetakPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakPegawaiActionPerformed
        // TODO add your handling code here:
        cetakLaporan("Pegawai");
    }//GEN-LAST:event_btnCetakPegawaiActionPerformed

    private void btnCetakKehadiranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakKehadiranActionPerformed
        // TODO add your handling code here:
        cetakLaporan("Kehadiran");
    }//GEN-LAST:event_btnCetakKehadiranActionPerformed

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
            java.util.logging.Logger.getLogger(LaporanForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LaporanForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LaporanForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LaporanForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LaporanForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetakKehadiran;
    private javax.swing.JButton btnCetakPegawai;
    private javax.swing.JButton btnFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblKehadiran;
    private javax.swing.JTable tblPegawai;
    private com.toedter.calendar.JDateChooser txtTanggal;
    // End of variables declaration//GEN-END:variables
}
