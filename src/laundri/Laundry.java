package laundri;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;


public class Laundry extends javax.swing.JFrame {

    data_pelanggan dataPel;
    DefaultTableModel tblplgn;
    DefaultTableModel tblselesai;
    Color defaultcolor, clickedcolor, white, black;
    
    //inisialisasi variabel untuk nilai tagihan pada setiap jenis cucian
    int beratP;
    int helaijas;
    int lembarkarpet;
    int pasangsepatu;
    int unittas;
    int helaibed;
    int totalbiaya;
   
    public Laundry() {
        initComponents();
       
        
        NOHP.setEditable(false);
        NAMA.setEditable(false);
        TANGGAL.setEditable(false);
        TOTAL.setEditable(false);
        nota.setEditable(false);
        nota.setText("");
        //animasi click menu
        defaultcolor = new Color(0,204,255);
        clickedcolor = new Color(227,224,224);
        white = new Color(255,255,255);
        black = new Color(0,0,0);
        
        //inisialisasi tabel
        dataPel = new data_pelanggan();
        tblplgn = (DefaultTableModel)tabelpel.getModel();
        tblselesai = (DefaultTableModel)dataselesai.getModel();

        try{
        //refresh table
        refreshTable();
        refreshTableSelesai();
        }catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, "tidak ada data");
        }
        
        //manipulasi isi text berupa string 0
        pakaian.setText("0");
        jas.setText("0");
        karpet.setText("0");
        sepatu.setText("0");
        tas.setText("0");
        bedcover.setText("0");
    }
    
    //metod merefresh table pelanggan yang diproses
    public void refreshTable (){
        //reset row
        int jumlahRow = tblplgn.getRowCount();
        for(int i=jumlahRow-1; i>=0; i--){
            tblplgn.removeRow(i);
        }
        ResultSet data = dataPel.getPlgn();
        try {
            while(data.next()){
                tblplgn.addRow(new Object[] {
                    data.getString("no_hp"),
                    data.getString("nama"),
                    data.getString("tanggal"),
                    data.getString("total_tagihan"),
                    status.getText()
                });  
            }
        } catch (SQLException ex) {
            Logger.getLogger(Laundry.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    //metod merefresh tabel pelanggan yang sudah selesai diproses
    public void refreshTableSelesai (){
        //reset row
        int jumlahRow = tblselesai.getRowCount();
        for(int i=jumlahRow-1; i>=0; i--){
            tblselesai.removeRow(i);
        }
        ResultSet data = dataPel.getPlgnSelesai();
        try {
            while(data.next()){
                tblselesai.addRow(new Object[] {
                    data.getString("no_hp"),
                    data.getString("nama"),
                    data.getString("tanggal"),
                    data.getString("total_tagihan"),
                    status2.getText()
                });  
            }
        } catch (SQLException ex) {
            Logger.getLogger(Laundry.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    //pencarian data pelanggan
    public void search(String noHP){
        try{
            tblplgn = (DefaultTableModel)tabelpel.getModel();
            TableRowSorter<DefaultTableModel> HPno = new TableRowSorter<>(tblplgn);
            tabelpel.setRowSorter(HPno);
            HPno.setRowFilter(RowFilter.regexFilter(noHP));
        }catch(PatternSyntaxException e){
            JOptionPane.showMessageDialog(rootPane, "simbol dilarang!");
        }
    }
    
    //pencarian data pelanggan selesai diproses
    public void searchselesai(String noHP){
        try{
        tblselesai = (DefaultTableModel)dataselesai.getModel();
        TableRowSorter<DefaultTableModel> HPno = new TableRowSorter<>(tblselesai);
        dataselesai.setRowSorter(HPno);
        HPno.setRowFilter(RowFilter.regexFilter(noHP));
        }catch(PatternSyntaxException e){
            JOptionPane.showMessageDialog(rootPane, "simbol dilarang!");
        }
    }
    
    //metod penghitungan jenis cucian lain selain pakaian dan menghitung total tagihan
    public void selainPakaian(){
        String jasku = jas.getText();
        String karpetku = karpet.getText();
        String sepatuku = sepatu.getText();
        String tasku = tas.getText();
        String bedcoku = bedcover.getText();
        
        //tagihan jas
        helaijas = Integer.parseInt(jasku);
        helaijas *= 15000;
        String bj = Integer.toString(helaijas);
        totaljas.setText(bj);

        //tagihan karpet
        lembarkarpet = Integer.parseInt(karpetku);
        lembarkarpet *= 20000;
        String bk = Integer.toString(lembarkarpet);
        totalkar.setText(bk);

        //tagihan Sepatu
        pasangsepatu = Integer.parseInt(sepatuku);
        pasangsepatu *= 25000;
        String bs = Integer.toString(pasangsepatu);
        totalsep.setText(bs);

        //tagihan Tas
        unittas = Integer.parseInt(tasku);
        unittas *= 10000;
        String bt = Integer.toString(unittas);
        totaltas.setText(bt);

        //tagihan Bed Cover
        helaibed = Integer.parseInt(bedcoku);
        helaibed *= 25000;
        String bc = Integer.toString(helaibed);
        totalbed.setText(bc);

        //Total tagihan
        totalbiaya = beratP+helaijas+lembarkarpet+pasangsepatu+unittas+helaibed;
        String btot = Integer.toString(totalbiaya);
        total.setText(btot);
    }
    //String only alphabet
    public static boolean validateLetters(String txt){
        String regx = "a-zA-Z+\\.?";
        Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txt);
        return matcher.find();
    }
    
    public static boolean isAlpha(String txt){
        if(txt == null){
            return false;
        }
        for(int i=0;i<txt.length();i++){
            char c = txt.charAt(i);
            if(!(c>='A'&&c<='Z')&&!(c>='a'&&c<='z'))
                return false;
        }
        return true;
    }
    
    //method megambil data pelanggan untuk kebutuhan bill dan database
    public void data(){
        try{
            String namaku = nama.getText();
            String nohpku = nohp.getText();
            String pakaianku = pakaian.getText(); 
            String jasku = jas.getText();
            String karpetku = karpet.getText();
            String sepatuku = sepatu.getText();
            String tasku = tas.getText();
            String bedcoku = bedcover.getText();
            Object[] huruf = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
                              "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
                              "`","~","!","@","#","$","%","^","&","*","(",")","-","_","+","=","[","]","{","}",";",":","'",",","<",".",">","/","?","\\","|"};                    
           
            if(!"".equals(namaku)&&!"".equals(nohpku)){
                if(namaku.matches("[ a-zA-Z]+$")){
                    if(nohpku.matches("[0-9]+$")){
                        long noHP = Long.parseLong(nohpku);
                        //tagihan Pakaian
                        int beratpak = Integer.parseInt(pakaianku);
                        if(cuci.isSelected()&&!setrika.isSelected()){
                            if("0".equals(pakaianku)&&"0".equals(jasku)&&"0".equals(karpetku)&&"0".equals(sepatuku)&&"0".equals(tasku)&&"0".equals(bedcoku)){
                            JOptionPane.showMessageDialog(rootPane, "input salah satu barang cucian!");
                            }else{
                                beratP = beratpak*5000;
                                String bp = Integer.toString(beratP);
                                totalpak.setText(bp);
                                //memanggil metod menghitung total jenis cucian lain
                                selainPakaian();
                                //memanggil metod pembuatan bill tagihan
                                bill();
                                //memasukkan data ke database dengan metod insertDataToDB
                                insertDataToDB();
                            }
                        }
                        if(setrika.isSelected()&&!cuci.isSelected()){
                            if("0".equals(pakaianku)&&"0".equals(jasku)&&"0".equals(karpetku)&&"0".equals(sepatuku)&&"0".equals(tasku)&&"0".equals(bedcoku)){
                            JOptionPane.showMessageDialog(rootPane, "input salah satu barang cucian!");
                            }else{
                                beratP = beratpak*3000;
                                String bp = Integer.toString(beratP);
                                totalpak.setText(bp); 
                                //memanggil metod menghitung total jenis cucian lain
                                selainPakaian();
                                //memanggil metod pembuatan bill tagihan
                                bill();
                                //memasukkan data ke database dengan metod insertDataToDB
                                insertDataToDB();
                            }
                        }
                        if(cuci.isSelected()&&setrika.isSelected()){
                            if("0".equals(pakaianku)&&"0".equals(jasku)&&"0".equals(karpetku)&&"0".equals(sepatuku)&&"0".equals(tasku)&&"0".equals(bedcoku)){
                            JOptionPane.showMessageDialog(rootPane, "input salah satu barang cucian!");
                            }else{
                                beratP = beratpak*7000;
                                String bk = Integer.toString(beratP);
                                totalpak.setText(bk);
                                //memanggil metod menghitung total jenis cucian lain
                                selainPakaian();
                                //memanggil metod pembuatan bill tagihan
                                bill();
                                //memasukkan data ke database dengan metod insertDataToDB
                                insertDataToDB();
                            }
                        }
                        if(!cuci.isSelected()&&!setrika.isSelected()){
                            if("0".equals(pakaianku)&&"0".equals(jasku)&&"0".equals(karpetku)&&"0".equals(sepatuku)&&"0".equals(tasku)&&"0".equals(bedcoku)){
                            JOptionPane.showMessageDialog(rootPane, "input salah satu barang cucian!");
                            }else{
                                selainPakaian();
                                if("0".equals(pakaianku)){
                                    int pak = Integer.parseInt(pakaianku);
                                    pak *= 0;
                                    String pakk = Integer.toString(pak);
                                    totalpak.setText(pakk);
                                    //memanggil metod menghitung total jenis cucian lain
                                    selainPakaian();
                                    //memanggil metod pembuatan bill tagihan
                                    bill();
                                    //memasukkan data ke database dengan metod insertDataToDB
                                    insertDataToDB();
                                }
                                if(!"0".equals(pakaianku)){
                                    JOptionPane.showMessageDialog(rootPane, "input pilihan proses pakaian!");
                                }
                            }
                        }
                    }else{
                        JOptionPane.showMessageDialog(rootPane, "silahkan input nomor HP yang valid");
                    }
                }else{
                    JOptionPane.showMessageDialog(rootPane, "nama hanya berupa alfabet!");
                }    
            }
            if("".equals(namaku)||"".equals(nohpku)){
                JOptionPane.showMessageDialog(rootPane, "silahkan input nomor HP dan nama");
            }
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(rootPane, "input jumlah yang valid!");
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(rootPane, "silahkan input tanggal!");
        }
    }
    
    //metod untuk membuat bill tagihan
    public void bill(){
        //pembuatan nota
        nota.setText("");
        String namaku = nama.getText();
        String pakaian1 = totalpak.getText();
        String jas1 = totaljas.getText();
        String karpet1 = totalkar.getText();
        String sepatu1 = totalsep.getText();
        String tas1 = totaltas.getText();
        String bed1 = totalbed.getText();
        String totalAll1 = total.getText();
            
        //mengubah format tanggal
        DateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy");
        String tanggalku = fmt.format(this.tanggal.getDate());
            
        nota.setText("Hai kak "+namaku
                    +"\nTerima kasih telah menggunakan layanan laundry kami:)"
                    +"\nSemoga harimu menyenangkan.."
                    +"\n\nBerikut rincian tagihan laundry kak "+namaku
                    +"\nTanggal = "+tanggalku
                    +"\n========================"
                    +"\nPakaian      = Rp."+pakaian1
                    +"\nJas             = Rp."+jas1
                    +"\nKarpet        = Rp."+karpet1
                    +"\nSepatu       = Rp."+sepatu1
                    +"\nTas            = Rp."+tas1
                    +"\nBed Cover = Rp."+bed1
                    +"\n_____________________"
                    +"\nTotal = Rp."+totalAll1
                    +"\n\nSyarat & Ketentuan"
                    +"\nUntuk pembayaran transfer dapat dikirim ke rekening"
                    +"\n\nBank Mandiri"
                    +"\n1080020610656"
                    +"\nan. Salman Al Haritsi"
                    +"\n\nCatatan:"
                    +"\n1. Pengambilan barang harap disertai nota"
                    +"\n2. Barang yang tidak diambil setelah 1 bulan diluar tanggung jawab pihak laundry"
                    +"\n3. Barang hilang/rusak karena proses pengerjaan diganti maksimal 2x ongkos cuci"
                    +"\n4. Kerusakan barang karena luntur/susut diluar tanggung jawab pihak laundry"
                    +"\n5. Hak klaim berlaku 24 jam setelah barang diambil"
                    +"\n6. Hasil cucian yang tidak bersih dapat dikembalikan untuk dicuci ulang, maksimal 24 jam setelah pengambilan"
                    +"\n7. Setiap kostumer harus menyetujui ketentuan diatas"
                    +"\n\n========================"
                    +"\nTerimakasih:)"
                    +"\nJangan lupa kembali lagi ya..."
                    +"\ncostumer care: +6282214950740");
    }
    
    //metod untuk memasukkan data yang sudah di input ke dalam database 
    public void insertDataToDB(){
        //mengubah format tanggal
        DateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy");
        String tanggalku = fmt.format(this.tanggal.getDate());
        
        //insert data to database
        boolean insert = dataPel.insertplgn(nohp.getText(), nama.getText(), tanggalku, "Rp."+total.getText());
        if(insert == true){
           refreshTable();
            JOptionPane.showMessageDialog(rootPane, "berhasil insert data pelanggan!");
            JOptionPane.showMessageDialog(rootPane, "bill tagihan dicetak");
        }else{
            JOptionPane.showMessageDialog(rootPane, "gagal insert data pelanggan!");
        }
    }
    
    //metod menghapus data yang baru di input
    public void clearData(){
        nama.setText("");
        nohp.setText("");
        totalpak.setText("");
        totaljas.setText("");
        totalkar.setText("");
        totalsep.setText("");
        totaltas.setText("");
        totalbed.setText("");
        total.setText("");
        pakaian.setText("0");
        jas.setText("0");
        karpet.setText("0");
        sepatu.setText("0");
        tas.setText("0");
        bedcover.setText("0");
        cuci.setSelected(false);
        setrika.setSelected(false);
    }
    
    //metod menyalin bill tagihan ke clipboard
    public void copy(){
        String nota1 = nota.getText();
        if(!"".equals(nota1)){
            Clipboard clip=Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection strsel=new StringSelection(nota1);
            clip.setContents(strsel, strsel);
            JOptionPane.showMessageDialog(rootPane, "Nota sudah di copy ke clipboard");
        }if("".equals(nota1)){
            JOptionPane.showMessageDialog(rootPane, "tidak ada data tagihan");
        }     
    }
    
    //metod penyerahan laundri yang sudah selesai diproses
    public void selesai(){
        String nohpsel = NOHP.getText();
        String namasel = NAMA.getText();
        String tanggalsel = TANGGAL.getText();
        String totalsel = TOTAL.getText();
        try{
            if(!"".equals(nohpsel)&&!"".equals(namasel)&&!"".equals(tanggalsel)&&!"".equals(totalsel)){
                DateFormat fmt1 = new SimpleDateFormat("dd-MMM-yyyy");
                String tanggalku1 = fmt1.format(this.tanggal2.getDate());
                dataPel.hapusplgn(NOHP.getText());
                //refreshTable();
                int row = tabelpel.getSelectedRow();
                boolean insert = dataPel.insertselesai(NOHP.getText(), NAMA.getText(), tanggalku1, TOTAL.getText());
                if(insert == true){
                    refreshTable();
                    refreshTableSelesai();
                    NOHP.setText("");
                    NAMA.setText("");
                    TANGGAL.setText("");
                    TOTAL.setText("");
                }
                JOptionPane.showMessageDialog(rootPane, "laundry diserahkan!");
            }
            else{
                JOptionPane.showMessageDialog(rootPane, "pilih data!!");
            }   
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(rootPane, "silahkan input tanggal valid!");
        }
    }
        
    //metod untuk memasukkan data kedalam text saat di klik
    public void klikdataTbl(){
        int row = tabelpel.getSelectedRow();
        NOHP.setText(tabelpel.getValueAt(row, 0).toString());
        NAMA.setText(tabelpel.getValueAt(row, 1).toString());
        TANGGAL.setText(tabelpel.getValueAt(row, 2).toString());
        TOTAL.setText(tabelpel.getValueAt(row, 3).toString()); 
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        status = new javax.swing.JLabel();
        status2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        p1 = new javax.swing.JPanel();
        m11 = new javax.swing.JLabel();
        p2 = new javax.swing.JPanel();
        m22 = new javax.swing.JLabel();
        p3 = new javax.swing.JPanel();
        m33 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        menu1 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        nama = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        nohp = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        pakaian = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        cuci = new javax.swing.JCheckBox();
        setrika = new javax.swing.JCheckBox();
        jPanel9 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jas = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        karpet = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        bedcover = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        sepatu = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        tas = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        totalbed = new javax.swing.JLabel();
        hapus = new javax.swing.JButton();
        proses = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        totaltas = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        totalsep = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        totalkar = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        totaljas = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        totalpak = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        total = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        tanggal = new com.toedter.calendar.JDateChooser();
        jLabel27 = new javax.swing.JLabel();
        menu2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        nota = new javax.swing.JTextArea();
        menu3 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelpel = new javax.swing.JTable();
        carinohp = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        dataselesai = new javax.swing.JTable();
        tanggal2 = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        NOHP = new javax.swing.JTextField();
        NAMA = new javax.swing.JTextField();
        TOTAL = new javax.swing.JTextField();
        TANGGAL = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        carinohp1 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        status.setFont(new java.awt.Font("NSimSun", 1, 12)); // NOI18N
        status.setForeground(new java.awt.Color(255, 0, 0));
        status.setText("DIPROSES");

        status2.setBackground(new java.awt.Color(51, 255, 51));
        status2.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        status2.setForeground(new java.awt.Color(0, 255, 0));
        status2.setText("SELESAI");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("e-bill laundry");
        setResizable(false);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(51, 0, 153));
        jPanel2.setForeground(new java.awt.Color(0, 0, 102));

        jLabel26.setIcon(new javax.swing.ImageIcon("C:\\Users\\salma\\Downloads\\elaundry1.png")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(245, Short.MAX_VALUE)
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(174, 174, 174))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel26)
                .addContainerGap())
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 760, 70));

        jPanel3.setBackground(new java.awt.Color(0, 204, 255));

        p1.setBackground(new java.awt.Color(0, 204, 255));
        p1.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                p1MouseWheelMoved(evt);
            }
        });
        p1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                p1MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                p1MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                p1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                p1MouseReleased(evt);
            }
        });

        m11.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        m11.setIcon(new javax.swing.ImageIcon("C:\\Users\\salma\\Downloads\\client1.png")); // NOI18N
        m11.setText(" DATA");
        m11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                m11MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                m11MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                m11MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout p1Layout = new javax.swing.GroupLayout(p1);
        p1.setLayout(p1Layout);
        p1Layout.setHorizontalGroup(
            p1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(m11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        p1Layout.setVerticalGroup(
            p1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(m11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        p2.setBackground(new java.awt.Color(0, 204, 255));
        p2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                p2MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                p2MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                p2MouseReleased(evt);
            }
        });

        m22.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        m22.setIcon(new javax.swing.ImageIcon("C:\\Users\\salma\\Downloads\\client.png")); // NOI18N
        m22.setText(" BILL");
        m22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                m22MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                m22MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                m22MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout p2Layout = new javax.swing.GroupLayout(p2);
        p2.setLayout(p2Layout);
        p2Layout.setHorizontalGroup(
            p2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(m22)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        p2Layout.setVerticalGroup(
            p2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(m22)
                .addContainerGap())
        );

        p3.setBackground(new java.awt.Color(0, 204, 255));
        p3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                p3MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                p3MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                p3MouseReleased(evt);
            }
        });

        m33.setBackground(new java.awt.Color(51, 51, 255));
        m33.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        m33.setIcon(new javax.swing.ImageIcon("C:\\Users\\salma\\Downloads\\client.22png.png")); // NOI18N
        m33.setText("PELANGGAN");
        m33.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                m33MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                m33MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                m33MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout p3Layout = new javax.swing.GroupLayout(p3);
        p3.setLayout(p3Layout);
        p3Layout.setHorizontalGroup(
            p3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(m33)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        p3Layout.setVerticalGroup(
            p3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p3Layout.createSequentialGroup()
                .addComponent(m33, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(p1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(p2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(p3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(p1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(p2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(p3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(440, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 170, 630));

        jPanel11.setBackground(new java.awt.Color(227, 224, 224));

        jLabel1.setFont(new java.awt.Font("NSimSun", 1, 12)); // NOI18N
        jLabel1.setText("Nama  :");

        nohp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nohpActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("NSimSun", 1, 12)); // NOI18N
        jLabel2.setText("No HP :");

        jLabel7.setFont(new java.awt.Font("NSimSun", 1, 12)); // NOI18N
        jLabel7.setText("Karpet   ");

        jLabel9.setFont(new java.awt.Font("NSimSun", 1, 12)); // NOI18N
        jLabel9.setText("Sepatu  ");

        jLabel10.setFont(new java.awt.Font("NSimSun", 1, 12)); // NOI18N
        jLabel10.setText("Tas");

        jPanel5.setBackground(new java.awt.Color(0, 204, 255));

        jLabel12.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel12.setText("Berat  =");

        pakaian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pakaianActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel13.setText("KG");

        cuci.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        cuci.setText("Cuci");
        cuci.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cuciActionPerformed(evt);
            }
        });

        setrika.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        setrika.setText("Setrika");
        setrika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setrikaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pakaian, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cuci, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(setrika, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(pakaian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(cuci)
                    .addComponent(setrika))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(0, 204, 255));

        jLabel14.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel14.setText("Helai  =");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jas, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("NSimSun", 1, 12)); // NOI18N
        jLabel3.setText("Pakaian  ");

        jLabel4.setFont(new java.awt.Font("NSimSun", 1, 12)); // NOI18N
        jLabel4.setText("Jas ");

        jPanel6.setBackground(new java.awt.Color(0, 204, 255));

        jLabel15.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel15.setText("Helai  =");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(karpet, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(karpet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(0, 204, 255));

        jLabel16.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel16.setText("Helai  =");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bedcover, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(183, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(bedcover, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(0, 204, 255));

        jLabel17.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel17.setText("Pasang =");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sepatu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(sepatu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel13.setBackground(new java.awt.Color(0, 204, 255));

        jLabel18.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel18.setText("Unit   =");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tas, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(tas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel15.setBackground(new java.awt.Color(0, 204, 255));

        jLabel24.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel24.setText("Rp.");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalbed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(totalbed))
                .addContainerGap())
        );

        hapus.setBackground(new java.awt.Color(255, 0, 51));
        hapus.setFont(new java.awt.Font("NSimSun", 1, 12)); // NOI18N
        hapus.setText("HAPUS");
        hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusActionPerformed(evt);
            }
        });

        proses.setBackground(new java.awt.Color(0, 255, 0));
        proses.setFont(new java.awt.Font("NSimSun", 1, 12)); // NOI18N
        proses.setText("PROSES");
        proses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prosesActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("NSimSun", 1, 12)); // NOI18N
        jLabel8.setText("Bed Cover ");

        jPanel4.setBackground(new java.awt.Color(0, 204, 255));

        jLabel23.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel23.setText("Rp.");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totaltas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(totaltas))
                .addContainerGap())
        );

        jPanel14.setBackground(new java.awt.Color(0, 204, 255));

        jLabel22.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel22.setText("Rp.");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalsep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(totalsep))
                .addContainerGap())
        );

        jPanel17.setBackground(new java.awt.Color(0, 204, 255));

        jLabel21.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel21.setText("Rp.");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalkar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(totalkar))
                .addContainerGap())
        );

        jPanel18.setBackground(new java.awt.Color(0, 204, 255));

        jLabel20.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel20.setText("Rp.");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totaljas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(totaljas))
                .addContainerGap())
        );

        jPanel19.setBackground(new java.awt.Color(0, 204, 255));

        jLabel19.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel19.setText("Rp.");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalpak, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(totalpak))
                .addContainerGap())
        );

        jPanel20.setBackground(new java.awt.Color(0, 204, 255));

        jLabel25.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel25.setText("Rp.");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(total, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(total))
                .addContainerGap())
        );

        jLabel11.setFont(new java.awt.Font("NSimSun", 1, 12)); // NOI18N
        jLabel11.setText("TOTAL ");

        tanggal.setDateFormatString("dd-MMM-yyyy");

        jLabel27.setFont(new java.awt.Font("NSimSun", 1, 12)); // NOI18N
        jLabel27.setText("Tanggal :");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                            .addGap(106, 106, 106)
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(21, 21, 21))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                            .addGap(13, 13, 13)
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4)
                                .addComponent(jLabel9)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8))
                            .addGap(11, 11, 11)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                    .addGap(58, 58, 58)
                                    .addComponent(hapus)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(proses)
                                    .addGap(16, 16, 16))
                                .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nama, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                            .addComponent(nohp))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tanggal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(0, 53, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel27)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nohp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addComponent(tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addGap(26, 26, 26))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)))
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel4))
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel7))
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel9)
                        .addGap(41, 41, 41)
                        .addComponent(jLabel10))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel8))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(21, 21, 21)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel11)))
                .addGap(33, 33, 33)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(proses, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(87, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout menu1Layout = new javax.swing.GroupLayout(menu1);
        menu1.setLayout(menu1Layout);
        menu1Layout.setHorizontalGroup(
            menu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        menu1Layout.setVerticalGroup(
            menu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab1", menu1);

        jPanel7.setBackground(new java.awt.Color(227, 224, 224));

        jButton3.setBackground(new java.awt.Color(0, 255, 102));
        jButton3.setFont(new java.awt.Font("NSimSun", 1, 12)); // NOI18N
        jButton3.setText("COPY");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        nota.setColumns(20);
        nota.setRows(5);
        nota.setText("\n");
        jScrollPane1.setViewportView(nota);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(158, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(239, 239, 239))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(152, 152, 152)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(94, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout menu2Layout = new javax.swing.GroupLayout(menu2);
        menu2.setLayout(menu2Layout);
        menu2Layout.setHorizontalGroup(
            menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        menu2Layout.setVerticalGroup(
            menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab2", menu2);

        jPanel12.setBackground(new java.awt.Color(227, 224, 224));

        tabelpel.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        tabelpel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nomor HP", "Nama", "Tanggal", "Total", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelpel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelpelMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelpel);

        carinohp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                carinohpKeyReleased(evt);
            }
        });

        dataselesai.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        dataselesai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nomor HP", "Nama", "Tanggal", "Total", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(dataselesai);

        tanggal2.setDateFormatString("dd-MMM-yyyy");
        tanggal2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tanggal2MouseClicked(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 255, 51));
        jButton1.setFont(new java.awt.Font("NSimSun", 1, 12)); // NOI18N
        jButton1.setText("SELESAI");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel5.setText("Nomor HP");

        jLabel6.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel6.setText("Nama");

        jLabel28.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel28.setText("Tanggal");

        jLabel29.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel29.setText("Tanggal Pengambilan");

        jLabel30.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel30.setText("Total");

        carinohp1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                carinohp1KeyReleased(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel31.setText("CARI");

        jLabel32.setFont(new java.awt.Font("NSimSun", 0, 12)); // NOI18N
        jLabel32.setText("CARI");

        jButton2.setIcon(new javax.swing.ImageIcon("C:\\Users\\salma\\Downloads\\SSS.png")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(147, 147, 147)
                .addComponent(jLabel5)
                .addGap(106, 106, 106)
                .addComponent(jLabel6)
                .addGap(104, 104, 104)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                .addComponent(jLabel30)
                .addGap(80, 80, 80))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(carinohp1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(carinohp, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                                    .addComponent(NOHP))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tanggal2, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel12Layout.createSequentialGroup()
                                            .addComponent(jLabel29)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel12Layout.createSequentialGroup()
                                            .addComponent(NAMA, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(29, 29, 29)
                                            .addComponent(TANGGAL, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(31, 31, 31)
                                            .addComponent(TOTAL, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
                .addGap(43, 43, 43))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(carinohp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel28)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NOHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(NAMA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TANGGAL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TOTAL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)))
                        .addComponent(tanggal2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(carinohp1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );

        javax.swing.GroupLayout menu3Layout = new javax.swing.GroupLayout(menu3);
        menu3.setLayout(menu3Layout);
        menu3Layout.setHorizontalGroup(
            menu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        menu3Layout.setVerticalGroup(
            menu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab3", menu3);

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, -10, 690, 710));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 744, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 681, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void m22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_m22MouseClicked
        jTabbedPane1.setSelectedIndex(1);
        p2.setBackground(clickedcolor);
    }//GEN-LAST:event_m22MouseClicked

    private void p1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p1MouseClicked
        jTabbedPane1.setSelectedIndex(0);
        p1.setBackground(clickedcolor);
    }//GEN-LAST:event_p1MouseClicked

    private void p2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p2MouseClicked
        jTabbedPane1.setSelectedIndex(1);
        p2.setBackground(clickedcolor);
    }//GEN-LAST:event_p2MouseClicked

    private void m33MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_m33MouseClicked
        jTabbedPane1.setSelectedIndex(2);
        p3.setBackground(clickedcolor);
    }//GEN-LAST:event_m33MouseClicked

    private void m11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_m11MouseClicked
        jTabbedPane1.setSelectedIndex(0);
        p1.setBackground(clickedcolor);
    }//GEN-LAST:event_m11MouseClicked

    private void m11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_m11MousePressed
        p1.setBackground(clickedcolor);
        p2.setBackground(defaultcolor);
        p3.setBackground(defaultcolor);
        m11.setForeground(white);
        
    }//GEN-LAST:event_m11MousePressed

    private void m11MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_m11MouseReleased
        p1.setBackground(defaultcolor);
        m11.setForeground(black);
    }//GEN-LAST:event_m11MouseReleased

    private void m22MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_m22MousePressed
        p1.setBackground(defaultcolor);
        p2.setBackground(clickedcolor);
        p3.setBackground(defaultcolor);
        m22.setForeground(white);
    }//GEN-LAST:event_m22MousePressed

    private void m22MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_m22MouseReleased
        p2.setBackground(defaultcolor);
        m22.setForeground(black);
    }//GEN-LAST:event_m22MouseReleased

    private void m33MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_m33MousePressed
        p1.setBackground(defaultcolor);
        p2.setBackground(defaultcolor);
        p3.setBackground(clickedcolor);
        m33.setForeground(white);
    }//GEN-LAST:event_m33MousePressed

    private void m33MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_m33MouseReleased
        p3.setBackground(defaultcolor);
        m33.setForeground(black);
    }//GEN-LAST:event_m33MouseReleased

    private void nohpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nohpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nohpActionPerformed

    private void p1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p1MousePressed
        p1.setBackground(clickedcolor);
        p2.setBackground(defaultcolor);
        p3.setBackground(defaultcolor);
        m11.setForeground(white);
    }//GEN-LAST:event_p1MousePressed

    private void p1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p1MouseReleased
        p1.setBackground(defaultcolor);
        m11.setForeground(black);
    }//GEN-LAST:event_p1MouseReleased

    private void p2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p2MousePressed
        p1.setBackground(defaultcolor);
        p2.setBackground(clickedcolor);
        p3.setBackground(defaultcolor);
        m22.setForeground(white);
    }//GEN-LAST:event_p2MousePressed

    private void p2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p2MouseReleased
        p2.setBackground(defaultcolor);
        m22.setForeground(black);
    }//GEN-LAST:event_p2MouseReleased

    private void p3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p3MouseClicked
        jTabbedPane1.setSelectedIndex(2);
        p3.setBackground(clickedcolor);
    }//GEN-LAST:event_p3MouseClicked

    private void p3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p3MousePressed
        p1.setBackground(defaultcolor);
        p2.setBackground(defaultcolor);
        p3.setBackground(clickedcolor);
        m33.setForeground(white);
    }//GEN-LAST:event_p3MousePressed

    private void p3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p3MouseReleased
        p3.setBackground(defaultcolor);
        m33.setForeground(black);
    }//GEN-LAST:event_p3MouseReleased

    private void p1MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_p1MouseWheelMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_p1MouseWheelMoved

    private void pakaianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pakaianActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pakaianActionPerformed

    private void cuciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cuciActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cuciActionPerformed

    private void setrikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setrikaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_setrikaActionPerformed

    private void prosesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prosesActionPerformed
        data();
    }//GEN-LAST:event_prosesActionPerformed

    private void hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusActionPerformed
        clearData();
    }//GEN-LAST:event_hapusActionPerformed
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        copy();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void carinohpKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_carinohpKeyReleased
        String searchString = carinohp.getText();
        search(searchString);
    }//GEN-LAST:event_carinohpKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        selesai();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tabelpelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelpelMouseClicked
        klikdataTbl();        
    }//GEN-LAST:event_tabelpelMouseClicked

    private void tanggal2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tanggal2MouseClicked
        
    }//GEN-LAST:event_tanggal2MouseClicked

    private void carinohp1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_carinohp1KeyReleased
        String searchString = carinohp1.getText();
        searchselesai(searchString);
    }//GEN-LAST:event_carinohp1KeyReleased

    private void p1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p1MouseExited
        
    }//GEN-LAST:event_p1MouseExited

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        NOHP.setText("");
        NAMA.setText("");
        TANGGAL.setText("");
        TOTAL.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(Laundry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Laundry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Laundry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Laundry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Laundry().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField NAMA;
    private javax.swing.JTextField NOHP;
    private javax.swing.JTextField TANGGAL;
    private javax.swing.JTextField TOTAL;
    private javax.swing.JTextField bedcover;
    private javax.swing.JTextField carinohp;
    private javax.swing.JTextField carinohp1;
    private javax.swing.JCheckBox cuci;
    private javax.swing.JTable dataselesai;
    private javax.swing.JButton hapus;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jas;
    private javax.swing.JTextField karpet;
    private javax.swing.JLabel m11;
    private javax.swing.JLabel m22;
    private javax.swing.JLabel m33;
    private javax.swing.JPanel menu1;
    private javax.swing.JPanel menu2;
    private javax.swing.JPanel menu3;
    private javax.swing.JTextField nama;
    private javax.swing.JTextField nohp;
    private javax.swing.JTextArea nota;
    private javax.swing.JPanel p1;
    private javax.swing.JPanel p2;
    private javax.swing.JPanel p3;
    private javax.swing.JTextField pakaian;
    private javax.swing.JButton proses;
    private javax.swing.JTextField sepatu;
    private javax.swing.JCheckBox setrika;
    private javax.swing.JLabel status;
    private javax.swing.JLabel status2;
    private javax.swing.JTable tabelpel;
    private com.toedter.calendar.JDateChooser tanggal;
    private com.toedter.calendar.JDateChooser tanggal2;
    private javax.swing.JTextField tas;
    private javax.swing.JLabel total;
    private javax.swing.JLabel totalbed;
    private javax.swing.JLabel totaljas;
    private javax.swing.JLabel totalkar;
    private javax.swing.JLabel totalpak;
    private javax.swing.JLabel totalsep;
    private javax.swing.JLabel totaltas;
    // End of variables declaration//GEN-END:variables
}
