/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ermrspw;

import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariDokter;
import kepegawaian.DlgCariPegawai;
import raven.toast.Notifications;
import rekammedis.MasterCariTemplatePemeriksaan;
import rekammedis.RMCari5SOAPTerakhir;

/**
 *
 * @author GINANJAR
 */
public class ERMrspw extends javax.swing.JFrame {

    private final DefaultTableModel tabModePembelian, tabModeRiwayatSOAP;
    private Connection koneksi = koneksiDB.condb();
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private String aktifkanparsial = "no", kode_poli = "", kd_pj = "";
    public DlgCariPegawai pegawai = new DlgCariPegawai(null, false);
    private DlgCariDokter dokter = new DlgCariDokter(null, false);
    private RMCari5SOAPTerakhir soapterakhir = new RMCari5SOAPTerakhir(null, false);
    private int i = 0, index = 0;
    private PreparedStatement ps, ps2, ps3, ps4;
    private ResultSet rs, rs2, rs3, rs4;

    Logger logger = Logger.getLogger("Logging");

    public ERMrspw() {
        initComponents();

        Notifications.getInstance().setJFrame(this);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        tabModePembelian = new DefaultTableModel(null, new Object[]{
            "No.", "Kode Obat", "Nama Obat", "Jumlah", "Tanggal"}) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
                if (colIndex == 0) {
                    a = true;
                }
                return a;
            }
        };
        tbPembelian.setModel(tabModePembelian);
        tbPembelian.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbPembelian.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 5; i++) {
            TableColumn column = tbPembelian.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(40);
            } else if (i == 1) {
                column.setPreferredWidth(140);
            } else if (i == 2) {
                column.setPreferredWidth(170);
            } else if (i == 3) {
                column.setPreferredWidth(100);
            } else if (i == 4) {
                column.setPreferredWidth(140);
            }
        }

        tbPembelian.setDefaultRenderer(Object.class, new WarnaTable());

        tabModeRiwayatSOAP = new DefaultTableModel(null, new Object[]{
            "P","No. Rawat","Tgl Perawatan", "Jam Rawat", "Status", "S.O.A.P.I.E"}) {
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class, java.lang.Object.class, 
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };

        tbPemeriksaanSOAP.setModel(tabModeRiwayatSOAP);
        tbPemeriksaanSOAP.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbPemeriksaanSOAP.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 6; i++) {
            TableColumn column = tbPemeriksaanSOAP.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            } else if (i == 1) {
                column.setPreferredWidth(120);
            } else if (i == 2) {
                column.setPreferredWidth(80);
            }else if (i == 3) {
                column.setPreferredWidth(80);
            } else if (i == 4) {
                column.setPreferredWidth(80);
            }
            else if (i == 5) {
                column.setPreferredWidth(400);
            }
        }

        tbPemeriksaanSOAP.setDefaultRenderer(Object.class, new WarnaTable());

//       if(koneksiDB.CARICEPAT().equals("aktif")){
//            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
//                @Override
//                public void insertUpdate(DocumentEvent e) {
//                    if(TCari.getText().length()>2){
//                        TampilkanData();
//                    }
//                }
//                @Override
//                public void removeUpdate(DocumentEvent e) {
//                    if(TCari.getText().length()>2){
//                        TampilkanData();
//                    }
//                }
//                @Override
//                public void changedUpdate(DocumentEvent e) {
//                    if(TCari.getText().length()>2){
//                        TampilkanData();
//                    }
//                }
//            });
//        }  
        pegawai.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (akses.getform().equals("DlgRawatJalan")) {
                    if (pegawai.getTable().getSelectedRow() != -1) {
                        KdPeg.setText(pegawai.getTable().getValueAt(pegawai.getTable().getSelectedRow(), 0).toString());
                        TPegawai.setText(pegawai.getTable().getValueAt(pegawai.getTable().getSelectedRow(), 1).toString());
                        Jabatan.setText(pegawai.getTable().getValueAt(pegawai.getTable().getSelectedRow(), 3).toString());
                        KdPeg.requestFocus();
                    }
                }
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        soapterakhir.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (soapterakhir.getTable().getSelectedRow() != -1) {
                    TKeluhan.setText(soapterakhir.getTable().getValueAt(soapterakhir.getTable().getSelectedRow(), 2).toString());
                    TPemeriksaan.setText(soapterakhir.getTable().getValueAt(soapterakhir.getTable().getSelectedRow(), 3).toString());
                    TPenilaian.setText(soapterakhir.getTable().getValueAt(soapterakhir.getTable().getSelectedRow(), 4).toString());
                    TindakLanjut.setText(soapterakhir.getTable().getValueAt(soapterakhir.getTable().getSelectedRow(), 5).toString());
                    TInstruksi.setText(soapterakhir.getTable().getValueAt(soapterakhir.getTable().getSelectedRow(), 6).toString());
                    TEvaluasi.setText(soapterakhir.getTable().getValueAt(soapterakhir.getTable().getSelectedRow(), 7).toString());
                    TEvaluasi.requestFocus();
                }
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        internalFrame1 = new widget.InternalFrame();
        panelBiasa1 = new widget.PanelBiasa();
        FormInput = new widget.PanelBiasa();
        jLabel3 = new widget.Label();
        TNoRw = new widget.TextBox();
        TNoRM = new widget.TextBox();
        TPasien = new widget.TextBox();
        jLabel23 = new widget.Label();
        DTPTgl = new widget.Tanggal();
        cmbJam = new widget.ComboBox();
        cmbMnt = new widget.ComboBox();
        cmbDtk = new widget.ComboBox();
        ChkJln = new widget.CekBox();
        jLabel4 = new widget.Label();
        jLabel5 = new widget.Label();
        jLabel6 = new widget.Label();
        TJenisKelamin = new widget.TextBox();
        panelBiasa3 = new widget.PanelBiasa();
        internalFrame2 = new widget.InternalFrame();
        jPanel1 = new javax.swing.JPanel();
        ChkInput1 = new widget.CekBox();
        scrollPane1 = new widget.ScrollPane();
        tbPemeriksaanSOAP = new widget.Table();
        jPanel2 = new javax.swing.JPanel();
        ChkInput2 = new widget.CekBox();
        scrollPane2 = new widget.ScrollPane();
        tbPembelian = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        ChkInput3 = new widget.CekBox();
        scrollPane3 = new widget.ScrollPane();
        jPanel4 = new javax.swing.JPanel();
        ChkInput4 = new widget.CekBox();
        scrollPane4 = new widget.ScrollPane();
        panelBiasa4 = new widget.PanelBiasa();
        panelBiasa2 = new widget.PanelBiasa();
        internalFrame3 = new widget.InternalFrame();
        panelGlass1 = new widget.panelGlass();
        jLabel37 = new widget.Label();
        KdPeg = new widget.TextBox();
        TPegawai = new widget.TextBox();
        BtnSeekPegawai = new widget.Button();
        Jabatan = new widget.TextBox();
        jLabel41 = new widget.Label();
        Btn5Soap = new widget.Button();
        BtnTemplatePemeriksaan = new widget.Button();
        jLabel8 = new widget.Label();
        scrollPane5 = new widget.ScrollPane();
        TKeluhan = new widget.TextArea();
        scrollPane6 = new widget.ScrollPane();
        TPemeriksaan = new widget.TextArea();
        jLabel9 = new widget.Label();
        jLabel28 = new widget.Label();
        scrollPane7 = new widget.ScrollPane();
        TPenilaian = new widget.TextArea();
        jLabel26 = new widget.Label();
        scrollPane8 = new widget.ScrollPane();
        TindakLanjut = new widget.TextArea();
        jLabel53 = new widget.Label();
        scrollPane9 = new widget.ScrollPane();
        TInstruksi = new widget.TextArea();
        jLabel56 = new widget.Label();
        scrollPane10 = new widget.ScrollPane();
        TEvaluasi = new widget.TextArea();
        BtnSimpan = new widget.Button();
        jLabel7 = new widget.Label();
        TSuhu = new widget.TextBox();
        jLabel17 = new widget.Label();
        TTinggi = new widget.TextBox();
        jLabel16 = new widget.Label();
        TBerat = new widget.TextBox();
        jLabel10 = new widget.Label();
        TTensi = new widget.TextBox();
        jLabel20 = new widget.Label();
        TRespirasi = new widget.TextBox();
        jLabel18 = new widget.Label();
        TNadi = new widget.TextBox();
        jLabel54 = new widget.Label();
        SpO2 = new widget.TextBox();
        jLabel22 = new widget.Label();
        TGCS = new widget.TextBox();
        jLabel29 = new widget.Label();
        cmbKesadaran = new widget.ComboBox();
        LingkarPerut = new widget.TextBox();
        jLabel25 = new widget.Label();
        jLabel15 = new widget.Label();
        TAlergi = new widget.TextBox();
        BtnAll = new widget.Button();
        jLabel11 = new widget.Label();
        LCount = new widget.Label();
        BtnHapus = new widget.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder("ERM-Rumah Sakit Putra Waspada"));
        internalFrame1.setLayout(new java.awt.BorderLayout());

        panelBiasa1.setPreferredSize(new java.awt.Dimension(1355, 100));

        FormInput.setPreferredSize(new java.awt.Dimension(260, 43));
        FormInput.setLayout(null);

        jLabel3.setText("Jenis Kelamin : ");
        FormInput.add(jLabel3);
        jLabel3.setBounds(860, 0, 110, 23);

        TNoRw.setEditable(false);
        TNoRw.setHighlighter(null);
        TNoRw.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TNoRwMouseClicked(evt);
            }
        });
        TNoRw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TNoRwActionPerformed(evt);
            }
        });
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(580, 60, 270, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        FormInput.add(TNoRM);
        TNoRM.setBounds(580, 0, 270, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TPasienActionPerformed(evt);
            }
        });
        FormInput.add(TPasien);
        TPasien.setBounds(580, 30, 270, 23);

        jLabel23.setText("Tanggal :");
        FormInput.add(jLabel23);
        jLabel23.setBounds(1130, 0, 60, 23);

        DTPTgl.setForeground(new java.awt.Color(50, 70, 50));
        DTPTgl.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "27-11-2023" }));
        DTPTgl.setDisplayFormat("dd-MM-yyyy");
        DTPTgl.setOpaque(false);
        DTPTgl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPTglKeyPressed(evt);
            }
        });
        FormInput.add(DTPTgl);
        DTPTgl.setBounds(1190, 0, 90, 23);

        cmbJam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        cmbJam.setPreferredSize(new java.awt.Dimension(62, 28));
        cmbJam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbJamKeyPressed(evt);
            }
        });
        FormInput.add(cmbJam);
        cmbJam.setBounds(1290, 0, 62, 23);

        cmbMnt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbMnt.setPreferredSize(new java.awt.Dimension(62, 28));
        cmbMnt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbMntKeyPressed(evt);
            }
        });
        FormInput.add(cmbMnt);
        cmbMnt.setBounds(1350, 0, 62, 23);

        cmbDtk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbDtk.setPreferredSize(new java.awt.Dimension(62, 28));
        cmbDtk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbDtkKeyPressed(evt);
            }
        });
        FormInput.add(cmbDtk);
        cmbDtk.setBounds(1420, 0, 62, 23);

        ChkJln.setBorder(null);
        ChkJln.setSelected(true);
        ChkJln.setBorderPainted(true);
        ChkJln.setBorderPaintedFlat(true);
        ChkJln.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkJln.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkJln.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkJln.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkJlnActionPerformed(evt);
            }
        });
        FormInput.add(ChkJln);
        ChkJln.setBounds(1480, 0, 23, 23);

        jLabel4.setText("No.Rawat : ");
        FormInput.add(jLabel4);
        jLabel4.setBounds(510, 60, 70, 23);

        jLabel5.setText("Nama : ");
        FormInput.add(jLabel5);
        jLabel5.setBounds(510, 30, 70, 23);

        jLabel6.setText("No Rekam Medis : ");
        FormInput.add(jLabel6);
        jLabel6.setBounds(470, 0, 110, 23);

        TJenisKelamin.setEditable(false);
        TJenisKelamin.setHighlighter(null);
        FormInput.add(TJenisKelamin);
        TJenisKelamin.setBounds(970, 0, 80, 23);

        javax.swing.GroupLayout panelBiasa1Layout = new javax.swing.GroupLayout(panelBiasa1);
        panelBiasa1.setLayout(panelBiasa1Layout);
        panelBiasa1Layout.setHorizontalGroup(
            panelBiasa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(FormInput, javax.swing.GroupLayout.DEFAULT_SIZE, 1461, Short.MAX_VALUE)
        );
        panelBiasa1Layout.setVerticalGroup(
            panelBiasa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBiasa1Layout.createSequentialGroup()
                .addComponent(FormInput, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                .addContainerGap())
        );

        internalFrame1.add(panelBiasa1, java.awt.BorderLayout.PAGE_START);

        panelBiasa3.setBorder(javax.swing.BorderFactory.createTitledBorder("Riwayat Perawatan Pasien"));
        panelBiasa3.setPreferredSize(new java.awt.Dimension(600, 300));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        ChkInput1.setBackground(new java.awt.Color(51, 255, 255));
        ChkInput1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, null, null, new java.awt.Color(204, 204, 204)));
        ChkInput1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput1.setMnemonic('I');
        ChkInput1.setText(".: Riwayat S O A P I E");
        ChkInput1.setToolTipText("Alt+I");
        ChkInput1.setBorderPainted(true);
        ChkInput1.setBorderPaintedFlat(true);
        ChkInput1.setFocusable(false);
        ChkInput1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ChkInput1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput1.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput1.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInput1ActionPerformed(evt);
            }
        });

        tbPemeriksaanSOAP.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbPemeriksaanSOAP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbPemeriksaanSOAPMouseClicked(evt);
            }
        });
        tbPemeriksaanSOAP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbPemeriksaanSOAPKeyReleased(evt);
            }
        });
        scrollPane1.setViewportView(tbPemeriksaanSOAP);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ChkInput1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(ChkInput1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(scrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        ChkInput2.setBackground(new java.awt.Color(51, 255, 255));
        ChkInput2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, null, null, new java.awt.Color(204, 204, 204)));
        ChkInput2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput2.setMnemonic('I');
        ChkInput2.setText(".: Riwayat Obat");
        ChkInput2.setToolTipText("Alt+I");
        ChkInput2.setBorderPainted(true);
        ChkInput2.setBorderPaintedFlat(true);
        ChkInput2.setFocusable(false);
        ChkInput2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ChkInput2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput2.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput2.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput2.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInput2ActionPerformed(evt);
            }
        });

        tbPembelian.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbPembelian.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbPembelianMouseClicked(evt);
            }
        });
        tbPembelian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbPembelianKeyReleased(evt);
            }
        });
        scrollPane2.setViewportView(tbPembelian);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ChkInput2, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
            .addComponent(scrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(ChkInput2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(scrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        ChkInput3.setBackground(new java.awt.Color(51, 255, 255));
        ChkInput3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, null, null, new java.awt.Color(204, 204, 204)));
        ChkInput3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput3.setMnemonic('I');
        ChkInput3.setText(".: Riwayat Perawatan");
        ChkInput3.setToolTipText("Alt+I");
        ChkInput3.setBorderPainted(true);
        ChkInput3.setBorderPaintedFlat(true);
        ChkInput3.setFocusable(false);
        ChkInput3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ChkInput3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput3.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput3.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput3.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInput3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ChkInput3, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
            .addComponent(scrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(ChkInput3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(scrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        ChkInput4.setBackground(new java.awt.Color(51, 255, 255));
        ChkInput4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, null, null, new java.awt.Color(204, 204, 204)));
        ChkInput4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput4.setMnemonic('I');
        ChkInput4.setText(".: Riwayat S O A P I E");
        ChkInput4.setToolTipText("Alt+I");
        ChkInput4.setBorderPainted(true);
        ChkInput4.setBorderPaintedFlat(true);
        ChkInput4.setFocusable(false);
        ChkInput4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ChkInput4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput4.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput4.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput4.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInput4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ChkInput4, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
            .addComponent(scrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(ChkInput4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(scrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout internalFrame2Layout = new javax.swing.GroupLayout(internalFrame2);
        internalFrame2.setLayout(internalFrame2Layout);
        internalFrame2Layout.setHorizontalGroup(
            internalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        internalFrame2Layout.setVerticalGroup(
            internalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(internalFrame2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelBiasa3Layout = new javax.swing.GroupLayout(panelBiasa3);
        panelBiasa3.setLayout(panelBiasa3Layout);
        panelBiasa3Layout.setHorizontalGroup(
            panelBiasa3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(internalFrame2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelBiasa3Layout.setVerticalGroup(
            panelBiasa3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(internalFrame2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        internalFrame1.add(panelBiasa3, java.awt.BorderLayout.LINE_START);

        panelBiasa4.setBorder(javax.swing.BorderFactory.createTitledBorder("Hasil Asesmen Terkini"));
        panelBiasa4.setPreferredSize(new java.awt.Dimension(300, 785));

        javax.swing.GroupLayout panelBiasa4Layout = new javax.swing.GroupLayout(panelBiasa4);
        panelBiasa4.setLayout(panelBiasa4Layout);
        panelBiasa4Layout.setHorizontalGroup(
            panelBiasa4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 290, Short.MAX_VALUE)
        );
        panelBiasa4Layout.setVerticalGroup(
            panelBiasa4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1057, Short.MAX_VALUE)
        );

        internalFrame1.add(panelBiasa4, java.awt.BorderLayout.EAST);

        panelBiasa2.setBorder(javax.swing.BorderFactory.createTitledBorder("Form ERM"));

        internalFrame3.setLayout(new java.awt.BorderLayout());

        jLabel37.setText("Dilakukan :");

        KdPeg.setHighlighter(null);
        KdPeg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdPegKeyPressed(evt);
            }
        });

        TPegawai.setEditable(false);
        TPegawai.setHighlighter(null);

        BtnSeekPegawai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeekPegawai.setMnemonic('4');
        BtnSeekPegawai.setToolTipText("ALt+4");
        BtnSeekPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeekPegawaiActionPerformed(evt);
            }
        });

        Jabatan.setEditable(false);
        Jabatan.setHighlighter(null);

        jLabel41.setText("Profesi / Jabatan / Departemen :");

        Btn5Soap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        Btn5Soap.setMnemonic('4');
        Btn5Soap.setToolTipText("ALt+4");
        Btn5Soap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn5SoapActionPerformed(evt);
            }
        });

        BtnTemplatePemeriksaan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnTemplatePemeriksaan.setMnemonic('4');
        BtnTemplatePemeriksaan.setToolTipText("ALt+4");
        BtnTemplatePemeriksaan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTemplatePemeriksaanActionPerformed(evt);
            }
        });

        jLabel8.setText("Subjek :");

        scrollPane5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        TKeluhan.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TKeluhan.setColumns(20);
        TKeluhan.setRows(5);
        TKeluhan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TKeluhanKeyPressed(evt);
            }
        });
        scrollPane5.setViewportView(TKeluhan);

        scrollPane6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        TPemeriksaan.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TPemeriksaan.setColumns(20);
        TPemeriksaan.setRows(5);
        TPemeriksaan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TPemeriksaanKeyPressed(evt);
            }
        });
        scrollPane6.setViewportView(TPemeriksaan);

        jLabel9.setText("Objek :");

        jLabel28.setText("Asesmen :");

        scrollPane7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        TPenilaian.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TPenilaian.setColumns(20);
        TPenilaian.setRows(5);
        TPenilaian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TPenilaianKeyPressed(evt);
            }
        });
        scrollPane7.setViewportView(TPenilaian);

        jLabel26.setText("Plan :");

        scrollPane8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        TindakLanjut.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TindakLanjut.setColumns(20);
        TindakLanjut.setRows(5);
        TindakLanjut.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TindakLanjutKeyPressed(evt);
            }
        });
        scrollPane8.setViewportView(TindakLanjut);

        jLabel53.setText("Instruksi :");

        scrollPane9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        TInstruksi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TInstruksi.setColumns(20);
        TInstruksi.setRows(5);
        TInstruksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TInstruksiKeyPressed(evt);
            }
        });
        scrollPane9.setViewportView(TInstruksi);

        jLabel56.setText("Evaluasi :");

        scrollPane10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        TEvaluasi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TEvaluasi.setColumns(20);
        TEvaluasi.setRows(5);
        TEvaluasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TEvaluasiKeyPressed(evt);
            }
        });
        scrollPane10.setViewportView(TEvaluasi);

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanActionPerformed(evt);
            }
        });
        BtnSimpan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanKeyPressed(evt);
            }
        });

        jLabel7.setText("Suhu (°C) :");

        TSuhu.setFocusTraversalPolicyProvider(true);
        TSuhu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TSuhuKeyPressed(evt);
            }
        });

        jLabel17.setText("Tinggi Badan (Cm) :");

        TTinggi.setFocusTraversalPolicyProvider(true);
        TTinggi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TTinggiKeyPressed(evt);
            }
        });

        jLabel16.setText("Berat (Kg) :");

        TBerat.setHighlighter(null);
        TBerat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TBeratKeyPressed(evt);
            }
        });

        jLabel10.setText("Tensi :");

        TTensi.setHighlighter(null);
        TTensi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TTensiKeyPressed(evt);
            }
        });

        jLabel20.setText("Respirasi (/menit) :");

        TRespirasi.setHighlighter(null);
        TRespirasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TRespirasiKeyPressed(evt);
            }
        });

        jLabel18.setText("Nadi (/menit) :");

        TNadi.setFocusTraversalPolicyProvider(true);
        TNadi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNadiKeyPressed(evt);
            }
        });

        jLabel54.setText("SpO2 (%) :");

        SpO2.setFocusTraversalPolicyProvider(true);
        SpO2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SpO2KeyPressed(evt);
            }
        });

        jLabel22.setText("GCS (E,V,M) :");

        TGCS.setFocusTraversalPolicyProvider(true);
        TGCS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TGCSKeyPressed(evt);
            }
        });

        jLabel29.setText("Kesadaran :");

        cmbKesadaran.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Compos Mentis", "Somnolence", "Sopor", "Coma" }));
        cmbKesadaran.setPreferredSize(new java.awt.Dimension(62, 28));
        cmbKesadaran.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbKesadaranKeyPressed(evt);
            }
        });

        LingkarPerut.setHighlighter(null);
        LingkarPerut.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LingkarPerutKeyPressed(evt);
            }
        });

        jLabel25.setText("L.P. (Cm) :");

        jLabel15.setText("Alergi :");

        TAlergi.setHighlighter(null);
        TAlergi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TAlergiKeyPressed(evt);
            }
        });

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/refresh.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setText("Refresh");
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKeyPressed(evt);
            }
        });

        jLabel11.setText("Record :");
        jLabel11.setPreferredSize(new java.awt.Dimension(95, 30));

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setPreferredSize(new java.awt.Dimension(87, 30));

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });
        BtnHapus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelGlass1Layout = new javax.swing.GroupLayout(panelGlass1);
        panelGlass1.setLayout(panelGlass1Layout);
        panelGlass1Layout.setHorizontalGroup(
            panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass1Layout.createSequentialGroup()
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(KdPeg, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(TPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(BtnSeekPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelGlass1Layout.createSequentialGroup()
                .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGlass1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(BtnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BtnAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(LCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BtnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelGlass1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelGlass1Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(TSuhu, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(TTinggi, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(TBerat, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(panelGlass1Layout.createSequentialGroup()
                                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(3, 3, 3)
                                        .addComponent(scrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelGlass1Layout.createSequentialGroup()
                                            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(3, 3, 3)
                                            .addComponent(Jabatan, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(3, 3, 3)
                                            .addComponent(Btn5Soap, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(3, 3, 3)
                                            .addComponent(BtnTemplatePemeriksaan, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelGlass1Layout.createSequentialGroup()
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(3, 3, 3)
                                            .addComponent(scrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelGlass1Layout.createSequentialGroup()
                                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(3, 3, 3)
                                            .addComponent(scrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(panelGlass1Layout.createSequentialGroup()
                                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(3, 3, 3)
                                    .addComponent(scrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelGlass1Layout.createSequentialGroup()
                                    .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(3, 3, 3)
                                    .addComponent(scrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelGlass1Layout.createSequentialGroup()
                                    .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(3, 3, 3)
                                    .addComponent(scrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelGlass1Layout.createSequentialGroup()
                                    .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(3, 3, 3)
                                    .addComponent(SpO2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(5, 5, 5)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(3, 3, 3)
                                    .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelGlass1Layout.createSequentialGroup()
                                            .addGap(41, 41, 41)
                                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(TGCS, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(3, 3, 3)
                                    .addComponent(cmbKesadaran, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelGlass1Layout.createSequentialGroup()
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(3, 3, 3)
                                    .addComponent(TTensi, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(6, 6, 6)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(3, 3, 3)
                                    .addComponent(TRespirasi, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(4, 4, 4)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(3, 3, 3)
                                    .addComponent(TNadi, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(panelGlass1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(LingkarPerut, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(TAlergi, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        panelGlass1Layout.setVerticalGroup(
            panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(KdPeg, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnSeekPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Jabatan, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Btn5Soap, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnTemplatePemeriksaan, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TSuhu, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TTinggi, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TBerat, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TTensi, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TRespirasi, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TNadi, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SpO2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TGCS, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbKesadaran, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LingkarPerut, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TAlergi, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(79, 79, 79)
                .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(BtnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(BtnAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(LCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(BtnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(473, Short.MAX_VALUE))
        );

        internalFrame3.add(panelGlass1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout panelBiasa2Layout = new javax.swing.GroupLayout(panelBiasa2);
        panelBiasa2.setLayout(panelBiasa2Layout);
        panelBiasa2Layout.setHorizontalGroup(
            panelBiasa2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(internalFrame3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelBiasa2Layout.setVerticalGroup(
            panelBiasa2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(internalFrame3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        internalFrame1.add(panelBiasa2, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void TNoRwMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TNoRwMouseClicked
        Window[] wins = Window.getWindows();
        for (Window win : wins) {
            if (win instanceof JDialog) {
                win.setLocationRelativeTo(internalFrame1);
                win.toFront();
            }
        }
    }//GEN-LAST:event_TNoRwMouseClicked

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        isRawat();
        isPsien();
        kd_pj = Sequel.cariIsi("select reg_periksa.kd_pj from reg_periksa where reg_periksa.no_rawat=?", TNoRw.getText());
        kode_poli = Sequel.cariIsi("select reg_periksa.kd_poli from reg_periksa where reg_periksa.no_rawat=?", TNoRw.getText());
//            tampilPembelianObat();
    }//GEN-LAST:event_TNoRwKeyPressed

    private void TPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TPasienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TPasienActionPerformed

    private void DTPTglKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPTglKeyPressed
        Valid.pindah(evt, DTPTgl, cmbJam);
    }//GEN-LAST:event_DTPTglKeyPressed

    private void cmbJamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbJamKeyPressed
        Valid.pindah(evt, DTPTgl, cmbMnt);
    }//GEN-LAST:event_cmbJamKeyPressed

    private void cmbMntKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMntKeyPressed
        Valid.pindah(evt, cmbJam, cmbDtk);
    }//GEN-LAST:event_cmbMntKeyPressed

    private void cmbDtkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbDtkKeyPressed
//        Valid.pindah(evt,cmbMnt,TCari);
    }//GEN-LAST:event_cmbDtkKeyPressed

    private void ChkJlnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkJlnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChkJlnActionPerformed

    private void ChkInput1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInput1ActionPerformed
        // TODO add your handling code here:
        isForm();
    }//GEN-LAST:event_ChkInput1ActionPerformed

    private void ChkInput2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInput2ActionPerformed
        // TODO add your handling code here:
        isForm2();
    }//GEN-LAST:event_ChkInput2ActionPerformed

    private void ChkInput3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInput3ActionPerformed
        // TODO add your handling code here:
        isForm3();
    }//GEN-LAST:event_ChkInput3ActionPerformed

    private void ChkInput4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInput4ActionPerformed
        // TODO add your handling code here:
        isForm4();
    }//GEN-LAST:event_ChkInput4ActionPerformed

    private void tbPembelianMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPembelianMouseClicked
//        if(tabModePembelian.getRowCount()!=0){
//            try {
//                getDataObat();
//            } catch (java.lang.NullPointerException e) {
//            }
//
//        }
    }//GEN-LAST:event_tbPembelianMouseClicked

    private void tbPembelianKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbPembelianKeyReleased
//        if(tabModePembelian.getRowCount()!=0){
//            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
//                try {
//                    getDataObat();
//                } catch (java.lang.NullPointerException e) {
//                }
//            }
//
//        }
    }//GEN-LAST:event_tbPembelianKeyReleased

    private void tbPemeriksaanSOAPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPemeriksaanSOAPMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbPemeriksaanSOAPMouseClicked

    private void tbPemeriksaanSOAPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbPemeriksaanSOAPKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tbPemeriksaanSOAPKeyReleased

    private void TNoRwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TNoRwActionPerformed

    }//GEN-LAST:event_TNoRwActionPerformed

    private void KdPegKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdPegKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            TPegawai.setText(pegawai.tampil3(KdPeg.getText()));
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            BtnSeekPegawaiActionPerformed(null);
        } else {
            Valid.pindah(evt, TNoRw, TNoRw);
        }
    }//GEN-LAST:event_KdPegKeyPressed

    private void BtnSeekPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeekPegawaiActionPerformed
        akses.setform("DlgRawatJalan");
        pegawai.emptTeks();
        pegawai.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        pegawai.setLocationRelativeTo(internalFrame1);
        pegawai.setVisible(true);
    }//GEN-LAST:event_BtnSeekPegawaiActionPerformed

    private void Btn5SoapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn5SoapActionPerformed
        if (TPasien.getText().trim().equals("") || TNoRw.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Maaf, Silahkan anda pilih dulu dengan menklik data pada table...!!!");
//            TCari.requestFocus();
        } else if (TPegawai.getText().trim().equals("") || KdPeg.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Maaf, Silahkan anda pilih dulu petugas/dokter pemberi asuhan...!!!");
//            TCari.requestFocus();
        } else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            soapterakhir.setNoRM(TNoRM.getText(), KdPeg.getText(), "Ralan");
            soapterakhir.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
            soapterakhir.setLocationRelativeTo(internalFrame1);
            soapterakhir.setVisible(true);
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_Btn5SoapActionPerformed

    private void BtnTemplatePemeriksaanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTemplatePemeriksaanActionPerformed
        if (TPasien.getText().trim().equals("") || TNoRw.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Maaf, Silahkan anda pilih dulu dengan menklik data pada table...!!!");
//            TCari.requestFocus();
        } else if (TPegawai.getText().trim().equals("") || KdPeg.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Maaf, Silahkan anda pilih dulu dokter pemberi asuhan...!!!");
//            TCari.requestFocus();
        } else {
            int jmlparsial = 0;
            if (aktifkanparsial.equals("yes")) {
                jmlparsial = Sequel.cariInteger("select count(set_input_parsial.kd_pj) from set_input_parsial where set_input_parsial.kd_pj=?", Sequel.cariIsi("select reg_periksa.kd_pj from reg_periksa where reg_periksa.no_rawat=?", TNoRw.getText()));
            }
            if (jmlparsial > 0) {
                inputTemplate();
            } else {
                if (Sequel.cariRegistrasi(TNoRw.getText()) > 0) {
                    JOptionPane.showMessageDialog(rootPane, "Data billing sudah terverifikasi.\nSilahkan hubungi bagian kasir/keuangan ..!!");
//                    TCari.requestFocus();
                } else {
                    inputTemplate();
                }
            }
        }
    }//GEN-LAST:event_BtnTemplatePemeriksaanActionPerformed

    private void TKeluhanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TKeluhanKeyPressed
        Valid.pindah2(evt, KdPeg, TPemeriksaan);
    }//GEN-LAST:event_TKeluhanKeyPressed

    private void TPemeriksaanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPemeriksaanKeyPressed
//        Valid.pindah2(evt,TKeluhan,TSuhu);
    }//GEN-LAST:event_TPemeriksaanKeyPressed

    private void TPenilaianKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPenilaianKeyPressed
//        Valid.pindah2(evt,TAlergi,TindakLanjut);
    }//GEN-LAST:event_TPenilaianKeyPressed

    private void TindakLanjutKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TindakLanjutKeyPressed
        Valid.pindah2(evt, TPenilaian, TInstruksi);
    }//GEN-LAST:event_TindakLanjutKeyPressed

    private void TInstruksiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TInstruksiKeyPressed
        Valid.pindah2(evt, TindakLanjut, TEvaluasi);
    }//GEN-LAST:event_TInstruksiKeyPressed

    private void TEvaluasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TEvaluasiKeyPressed
//        Valid.pindah2(evt,TInstruksi,BtnSimpan);
    }//GEN-LAST:event_TEvaluasiKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if (TNoRw.getText().trim().equals("") || TPasien.getText().trim().equals("")) {
            Valid.textKosong(TNoRw, "No.Rawat");
        } else {
            if ((!TKeluhan.getText().trim().equals("")) || (!TPemeriksaan.getText().trim().equals("")) || (!TSuhu.getText().trim().equals(""))
                    || (!TTensi.getText().trim().equals("")) || (!TAlergi.getText().trim().equals("")) || (!TTinggi.getText().trim().equals(""))
                    || (!TBerat.getText().trim().equals("")) || (!TRespirasi.getText().trim().equals("")) || (!TNadi.getText().trim().equals(""))
                    || (!TGCS.getText().trim().equals("")) || (!TindakLanjut.getText().trim().equals("")) || (!TPenilaian.getText().trim().equals(""))
                    || (!TInstruksi.getText().trim().equals("")) || (!SpO2.getText().trim().equals("")) || (!TEvaluasi.getText().trim().equals(""))) {
                if (KdPeg.getText().trim().equals("") || TPegawai.getText().trim().equals("")) {
                    Valid.textKosong(KdPeg, "Dokter/Paramedis masih kosong...!!");
                } else {
                    if (akses.getkode().equals("Admin Utama")) {
                        if (Sequel.menyimpantf("pemeriksaan_ralan", "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?", "Data", 21, new String[]{
                            TNoRw.getText(), Valid.SetTgl(DTPTgl.getSelectedItem() + ""), cmbJam.getSelectedItem() + ":" + cmbMnt.getSelectedItem() + ":" + cmbDtk.getSelectedItem(),
                            TSuhu.getText(), TTensi.getText(), TNadi.getText(), TRespirasi.getText(), TTinggi.getText(), TBerat.getText(),
                            SpO2.getText(), TGCS.getText(), cmbKesadaran.getSelectedItem().toString(), TKeluhan.getText(), TPemeriksaan.getText(), TAlergi.getText(),
                            LingkarPerut.getText(), TindakLanjut.getText(), TPenilaian.getText(), TInstruksi.getText(), TEvaluasi.getText(), KdPeg.getText()}) == true) {
//                            tabModeRiwayatSOAP.addRow(new Object[]{
//                                false, TNoRw.getText(), TNoRM.getText(), TPasien.getText(), Valid.SetTgl(DTPTgl.getSelectedItem() + ""), cmbJam.getSelectedItem() + ":" + cmbMnt.getSelectedItem() + ":" + cmbDtk.getSelectedItem(),
//                                TSuhu.getText(), TTensi.getText(), TNadi.getText(), TRespirasi.getText(), TTinggi.getText(), TBerat.getText(), SpO2.getText(), TGCS.getText(), cmbKesadaran.getSelectedItem().toString(),
//                                TKeluhan.getText(), TPemeriksaan.getText(), TAlergi.getText(), LingkarPerut.getText(), TindakLanjut.getText(), TPenilaian.getText(), TInstruksi.getText(), TEvaluasi.getText(),
//                                KdPeg.getText(), TPegawai.getText(), Jabatan.getText()
//                            });
                            TSuhu.setText("");
                            TTensi.setText("");
                            TNadi.setText("");
                            TRespirasi.setText("");
                            TTinggi.setText("");
                            TBerat.setText("");
                            TGCS.setText("");
                            TKeluhan.setText("");
                            TPemeriksaan.setText("");
                            TAlergi.setText("");
                            LingkarPerut.setText("");
                            TindakLanjut.setText("");
                            TPenilaian.setText("");
                            TInstruksi.setText("");
                            SpO2.setText("");
                            TEvaluasi.setText("");
                            cmbKesadaran.setSelectedIndex(0);
                            LCount.setText("" + tabModeRiwayatSOAP.getRowCount());
                            Notifications.getInstance().show(Notifications.Type.SUCCESS,Notifications.Location.TOP_RIGHT, "Data Berhasil Disimpan");
                            tampilRiwayatSOAP();
                        }
                    } else {
                        if (akses.getkode().equals(KdPeg.getText())) {
                            if (Sequel.menyimpantf("pemeriksaan_ralan", "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?", "Data", 21, new String[]{
                                TNoRw.getText(), Valid.SetTgl(DTPTgl.getSelectedItem() + ""), cmbJam.getSelectedItem() + ":" + cmbMnt.getSelectedItem() + ":" + cmbDtk.getSelectedItem(),
                                TSuhu.getText(), TTensi.getText(), TNadi.getText(), TRespirasi.getText(), TTinggi.getText(), TBerat.getText(),
                                SpO2.getText(), TGCS.getText(), cmbKesadaran.getSelectedItem().toString(), TKeluhan.getText(), TPemeriksaan.getText(), TAlergi.getText(),
                                LingkarPerut.getText(), TindakLanjut.getText(), TPenilaian.getText(), TInstruksi.getText(), TEvaluasi.getText(), KdPeg.getText()}) == true) {
//                                tabModePemeriksaan.addRow(new Object[]{
//                                    false, TNoRw.getText(), TNoRM.getText(), TPasien.getText(), Valid.SetTgl(DTPTgl.getSelectedItem() + ""), cmbJam.getSelectedItem() + ":" + cmbMnt.getSelectedItem() + ":" + cmbDtk.getSelectedItem(),
//                                    TSuhu.getText(), TTensi.getText(), TNadi.getText(), TRespirasi.getText(), TTinggi.getText(), TBerat.getText(), SpO2.getText(), TGCS.getText(), cmbKesadaran.getSelectedItem().toString(),
//                                    TKeluhan.getText(), TPemeriksaan.getText(), TAlergi.getText(), LingkarPerut.getText(), TindakLanjut.getText(), TPenilaian.getText(), TInstruksi.getText(), TEvaluasi.getText(),
//                                    KdPeg.getText(), TPegawai.getText(), Jabatan.getText()
//                                });
                                TSuhu.setText("");
                                TTensi.setText("");
                                TNadi.setText("");
                                TRespirasi.setText("");
                                TTinggi.setText("");
                                TBerat.setText("");
                                TGCS.setText("");
                                TKeluhan.setText("");
                                TPemeriksaan.setText("");
                                TAlergi.setText("");
                                LingkarPerut.setText("");
                                TindakLanjut.setText("");
                                TPenilaian.setText("");
                                TInstruksi.setText("");
                                SpO2.setText("");
                                TEvaluasi.setText("");
                                cmbKesadaran.setSelectedIndex(0);
                                LCount.setText("" + tabModeRiwayatSOAP.getRowCount());
                                Notifications.getInstance().show(Notifications.Type.SUCCESS,Notifications.Location.TOP_RIGHT, "Data Berhasil Disimpan");
                                tampilRiwayatSOAP();
                            }
                            
                        } else {
                            JOptionPane.showMessageDialog(null, "Hanya bisa disimpan oleh dokter/petugas yang bersangkutan..!!");
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnSimpanActionPerformed(null);
        } else {

        }
    }//GEN-LAST:event_BtnSimpanKeyPressed

    private void TSuhuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TSuhuKeyPressed
        Valid.pindah(evt, TPemeriksaan, TTinggi);
    }//GEN-LAST:event_TSuhuKeyPressed

    private void TTinggiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TTinggiKeyPressed
        Valid.pindah(evt, TSuhu, TBerat);
    }//GEN-LAST:event_TTinggiKeyPressed

    private void TBeratKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TBeratKeyPressed
        Valid.pindah(evt, TTinggi, TTensi);
    }//GEN-LAST:event_TBeratKeyPressed

    private void TTensiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TTensiKeyPressed
        Valid.pindah(evt, TBerat, TRespirasi);
    }//GEN-LAST:event_TTensiKeyPressed

    private void TRespirasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TRespirasiKeyPressed
        Valid.pindah(evt, TTensi, TNadi);
    }//GEN-LAST:event_TRespirasiKeyPressed

    private void TNadiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNadiKeyPressed
        Valid.pindah(evt, TRespirasi, SpO2);
    }//GEN-LAST:event_TNadiKeyPressed

    private void SpO2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SpO2KeyPressed
        Valid.pindah(evt, TNadi, TGCS);
    }//GEN-LAST:event_SpO2KeyPressed

    private void TGCSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TGCSKeyPressed
        Valid.pindah(evt, TNadi, cmbKesadaran);
    }//GEN-LAST:event_TGCSKeyPressed

    private void cmbKesadaranKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbKesadaranKeyPressed
        Valid.pindah(evt, TGCS, LingkarPerut);
    }//GEN-LAST:event_cmbKesadaranKeyPressed

    private void LingkarPerutKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LingkarPerutKeyPressed
        Valid.pindah(evt, cmbKesadaran, TAlergi);
    }//GEN-LAST:event_LingkarPerutKeyPressed

    private void TAlergiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TAlergiKeyPressed
        Valid.pindah(evt, LingkarPerut, TPenilaian);
    }//GEN-LAST:event_TAlergiKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
//        TCari.setText("");
//        TCariPasien.setText("");
        tampilRiwayatSOAP();
        Notifications.getInstance().show(Notifications.Type.SUCCESS,Notifications.Location.TOP_RIGHT, "BERHASIL REFRESH DATA!");
    }//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllActionPerformed(null);
        }else{
//            Valid.pindah(evt, BtnPrint, BtnKeluar);
        }
    }//GEN-LAST:event_BtnAllKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(tabModeRiwayatSOAP.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
                    TNoRw.requestFocus();
                }else{
                    for(i=0;i<tbPemeriksaanSOAP.getRowCount();i++){
                        if(tbPemeriksaanSOAP.getValueAt(i,0).toString().equals("true")){
                            if(akses.getkode().equals("Admin Utama")){
                                Sequel.queryu("delete from pemeriksaan_ralan where no_rawat='"+tbPemeriksaanSOAP.getValueAt(i,1).toString()+
                                        "' and tgl_perawatan='"+tbPemeriksaanSOAP.getValueAt(i,2).toString()+
                                        "' and jam_rawat='"+tbPemeriksaanSOAP.getValueAt(i,3).toString()+"' ");
                                tabModeRiwayatSOAP.removeRow(i);
                                i--;
                                
                                Notifications.getInstance().show(Notifications.Type.SUCCESS,Notifications.Location.TOP_RIGHT, "DATA BERHASIL DIHAPUS");
                            }else{
                                if(akses.getkode().equals(tbPemeriksaanSOAP.getValueAt(i,5).toString())){
                               Sequel.queryu("delete from pemeriksaan_ralan where no_rawat='"+tbPemeriksaanSOAP.getValueAt(i,1).toString()+
                                        "' and tgl_perawatan='"+tbPemeriksaanSOAP.getValueAt(i,2).toString()+
                                        "' and jam_rawat='"+tbPemeriksaanSOAP.getValueAt(i,3).toString()+"' ");
                                    tabModeRiwayatSOAP.removeRow(i);
                                    i--;
                                }else{
                                    JOptionPane.showMessageDialog(null,"Hanya bisa dihapus oleh dokter/petugas yang bersangkutan..!!");
                                }
                            }
                        }
                    }
                    LCount.setText(""+tabModeRiwayatSOAP.getRowCount());
                }
//        BtnBatalActionPerformed(evt);
    }//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
//            Valid.pindah(evt, BtnBatal, BtnPrint);
        }
    }//GEN-LAST:event_BtnHapusKeyPressed

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
            java.util.logging.Logger.getLogger(ERMrspw.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ERMrspw.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ERMrspw.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ERMrspw.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ERMrspw().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.Button Btn5Soap;
    private widget.Button BtnAll;
    private widget.Button BtnHapus;
    private widget.Button BtnSeekPegawai;
    private widget.Button BtnSimpan;
    private widget.Button BtnTemplatePemeriksaan;
    private widget.CekBox ChkInput1;
    private widget.CekBox ChkInput2;
    private widget.CekBox ChkInput3;
    private widget.CekBox ChkInput4;
    private widget.CekBox ChkJln;
    private widget.Tanggal DTPTgl;
    private widget.PanelBiasa FormInput;
    private widget.TextBox Jabatan;
    private widget.TextBox KdPeg;
    private widget.Label LCount;
    private widget.TextBox LingkarPerut;
    private widget.TextBox SpO2;
    private widget.TextBox TAlergi;
    private widget.TextBox TBerat;
    private widget.TextArea TEvaluasi;
    private widget.TextBox TGCS;
    private widget.TextArea TInstruksi;
    private widget.TextBox TJenisKelamin;
    private widget.TextArea TKeluhan;
    private widget.TextBox TNadi;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.TextBox TPegawai;
    private widget.TextArea TPemeriksaan;
    private widget.TextArea TPenilaian;
    private widget.TextBox TRespirasi;
    private widget.TextBox TSuhu;
    private widget.TextBox TTensi;
    private widget.TextBox TTinggi;
    private widget.TextArea TindakLanjut;
    private widget.ComboBox cmbDtk;
    private widget.ComboBox cmbJam;
    private widget.ComboBox cmbKesadaran;
    private widget.ComboBox cmbMnt;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame3;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.Label jLabel20;
    private widget.Label jLabel22;
    private widget.Label jLabel23;
    private widget.Label jLabel25;
    private widget.Label jLabel26;
    private widget.Label jLabel28;
    private widget.Label jLabel29;
    private widget.Label jLabel3;
    private widget.Label jLabel37;
    private widget.Label jLabel4;
    private widget.Label jLabel41;
    private widget.Label jLabel5;
    private widget.Label jLabel53;
    private widget.Label jLabel54;
    private widget.Label jLabel56;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private widget.PanelBiasa panelBiasa1;
    private widget.PanelBiasa panelBiasa2;
    private widget.PanelBiasa panelBiasa3;
    private widget.PanelBiasa panelBiasa4;
    private widget.panelGlass panelGlass1;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane10;
    private widget.ScrollPane scrollPane2;
    private widget.ScrollPane scrollPane3;
    private widget.ScrollPane scrollPane4;
    private widget.ScrollPane scrollPane5;
    private widget.ScrollPane scrollPane6;
    private widget.ScrollPane scrollPane7;
    private widget.ScrollPane scrollPane8;
    private widget.ScrollPane scrollPane9;
    private widget.Table tbPembelian;
    private widget.Table tbPemeriksaanSOAP;
    // End of variables declaration//GEN-END:variables

    private void isForm() {
        if (ChkInput1.isSelected() == true) {
            ChkInput1.setVisible(false);
            jPanel1.setPreferredSize(new Dimension(WIDTH, 282));
            scrollPane1.setVisible(true);
            ChkInput1.setVisible(true);
        } else if (ChkInput1.isSelected() == false) {
            ChkInput1.setVisible(false);
            jPanel1.setPreferredSize(new Dimension(WIDTH, 20));
            scrollPane1.setVisible(false);
            ChkInput1.setVisible(true);
        }
    }

    private void isForm2() {
        if (ChkInput2.isSelected() == true) {
            ChkInput2.setVisible(false);
            jPanel2.setPreferredSize(new Dimension(WIDTH, 282));
            scrollPane2.setVisible(true);
            ChkInput2.setVisible(true);
        } else if (ChkInput2.isSelected() == false) {
            ChkInput2.setVisible(false);
            jPanel2.setPreferredSize(new Dimension(WIDTH, 20));
            scrollPane2.setVisible(false);
            ChkInput2.setVisible(true);
        }
    }

    private void isForm3() {
        if (ChkInput3.isSelected() == true) {
            ChkInput3.setVisible(false);
            jPanel3.setPreferredSize(new Dimension(WIDTH, 282));
            scrollPane3.setVisible(true);
            ChkInput3.setVisible(true);
        } else if (ChkInput3.isSelected() == false) {
            ChkInput3.setVisible(false);
            jPanel3.setPreferredSize(new Dimension(WIDTH, 20));
            scrollPane3.setVisible(false);
            ChkInput3.setVisible(true);
        }
    }

    private void isForm4() {
        if (ChkInput4.isSelected() == true) {
            ChkInput4.setVisible(false);
            jPanel4.setPreferredSize(new Dimension(WIDTH, 282));
            scrollPane4.setVisible(true);
            ChkInput4.setVisible(true);
        } else if (ChkInput4.isSelected() == false) {
            ChkInput4.setVisible(false);
            jPanel4.setPreferredSize(new Dimension(WIDTH, 20));
            scrollPane4.setVisible(false);
            ChkInput4.setVisible(true);
        }
    }

    private void isRawat() {
        Sequel.cariIsi("select reg_periksa.no_rkm_medis from reg_periksa where reg_periksa.no_rawat=? ", TNoRM, TNoRw.getText());
    }

    private void isPsien() {
        Sequel.cariIsi("select concat(pasien.nm_pasien,' (',pasien.umur,')') from pasien where pasien.no_rkm_medis=? ", TPasien, TNoRM.getText());
        Sequel.cariIsi("select pasien.jk from pasien where pasien.no_rkm_medis=?", TJenisKelamin, TNoRM.getText());
    }

    public void SetPoli(String KodePoli) {
        this.kode_poli = KodePoli;
    }

    public void SetPj(String KodePj) {
        this.kd_pj = KodePj;
    }

    public void setNoRm(String norwt, String norm, Date tgl1, Date tgl2) {
        TNoRw.setText(norwt);
        TNoRM.setText(norm);

        isRawat();
        isPsien();

        ChkInput1.setSelected(true);
        tampilPembelianObat();
        tampilRiwayatSOAP();
    }

    private void inputTemplate() {
        if (dokter.tampil3(KdPeg.getText()).equals("")) {
            JOptionPane.showMessageDialog(null, "Template pemeriksaan hanya untuk dokter...!!");
        } else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            MasterCariTemplatePemeriksaan templatepemeriksaan = new MasterCariTemplatePemeriksaan(null, false);
            templatepemeriksaan.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
            templatepemeriksaan.setLocationRelativeTo(internalFrame1);
            templatepemeriksaan.isCek();
            templatepemeriksaan.setDokter(KdPeg.getText(), Valid.SetTgl(DTPTgl.getSelectedItem() + ""), cmbJam.getSelectedItem() + ":" + cmbMnt.getSelectedItem() + ":" + cmbDtk.getSelectedItem(), TNoRw.getText(), TNoRM.getText());
            templatepemeriksaan.tampil();
            templatepemeriksaan.setVisible(true);
            this.setCursor(Cursor.getDefaultCursor());
        }
    }

    private void tampilPembelianObat() {
        Valid.tabelKosong(tabModePembelian);
        i = 0;
        try {
            ps = koneksi.prepareStatement("select penjualan.nota_jual, penjualan.tgl_jual, penjualan.no_rkm_medis, penjualan.status, detailjual.kode_brng, databarang.nama_brng, detailjual.jumlah\n"
                    + "FROM penjualan inner join detailjual on detailjual.nota_jual = penjualan.nota_jual inner join databarang on detailjual.kode_brng=databarang.kode_brng\n"
                    + "WHERE penjualan.status='Sudah Dibayar' and penjualan.no_rkm_medis= ? "
                    + "order by penjualan.tgl_jual desc");
            ps.setString(1, TNoRM.getText());
            System.out.println("Nilai TNoRM " + TNoRM.getText().trim());
            try {

                rs = ps.executeQuery();
                while (rs.next()) {
                    i++;
                    tabModePembelian.addRow(new String[]{
                        i + "", rs.getString("kode_brng"), rs.getString("nama_brng"), rs.getString("jumlah"), rs.getString("tgl_jual")
                    });
                }

                logger.info("Berhasil Fetching Data Obat dengan No RM :" + TNoRM.getText().trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tampilRiwayatSOAP() {
        Valid.tabelKosong(tabModeRiwayatSOAP);
        i = 0;

        try {
            ps = koneksi.prepareStatement("select pemeriksaan_ralan.no_rawat, reg_periksa.tgl_registrasi, reg_periksa.status_lanjut, "
                    + "pemeriksaan_ralan.tgl_perawatan, pemeriksaan_ralan.jam_rawat, "
                    + "CONCAT(pemeriksaan_ralan.suhu_tubuh,\n"
                    + "pemeriksaan_ralan.tensi, pemeriksaan_ralan.nadi, pemeriksaan_ralan.respirasi,\n"
                    + "pemeriksaan_ralan.tinggi, pemeriksaan_ralan.berat, pemeriksaan_ralan.gcs,\n"
                    + "pemeriksaan_ralan.spo2, pemeriksaan_ralan.kesadaran, pemeriksaan_ralan.keluhan,\n"
                    + "pemeriksaan_ralan.pemeriksaan, pemeriksaan_ralan.alergi, pemeriksaan_ralan.lingkar_perut,\n"
                    + "pemeriksaan_ralan.rtl, pemeriksaan_ralan.penilaian, pemeriksaan_ralan.instruksi,\n"
                    + "pemeriksaan_ralan.evaluasi, pemeriksaan_ralan.nip) AS SOAPIE\n"
                    + "from pemeriksaan_ralan inner join reg_periksa on pemeriksaan_ralan.no_rawat = reg_periksa.no_rawat\n"
                    + "where reg_periksa.stts<>'Batal' and reg_periksa.no_rawat=? order by reg_periksa.tgl_registrasi desc");
            System.out.println("Nilai TNoRw " + TNoRw.getText());
            ps.setString(1, TNoRw.getText());
            try {

                rs = ps.executeQuery(); 

                while (rs.next()) {
                    tabModeRiwayatSOAP.addRow(new Object[]{
                        false ,rs.getString("no_rawat"), rs.getString("tgl_perawatan"),rs.getString("jam_rawat"), rs.getString("status_lanjut"), rs.getString("SOAPIE")
                    });
                }

                logger.info("Berhasil Fetching Data SOAPIE dengan No RM :" + TNoRM.getText().trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDataPemeriksaan() {

    }
}
