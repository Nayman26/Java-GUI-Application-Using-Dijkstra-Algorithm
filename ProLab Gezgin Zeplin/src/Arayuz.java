import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.border.TitledBorder;

public class Arayuz {

	private JFrame frmMain;
	private Sehir kaynak, hedef;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Arayuz window = new Arayuz();

					window.frmMain.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private Arayuz() {
		initialize();
	}

	private void initialize() {
		frmMain = new JFrame();
		frmMain.setTitle("Gezgin Zeplin");
		frmMain.setResizable(false);
		frmMain.setBounds(100, 100, 440, 300);
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		frmMain.getContentPane().add(panel);
		frmMain.getContentPane().setLayout(null);
		panel.setBounds(0, 0, 220, 271);
		panel.setLayout(null);

		JLabel lblNereden = new JLabel("Nereden:");
		lblNereden.setBounds(10, 14, 63, 14);
		panel.add(lblNereden);

		JComboBox<String> cmb_kaynak = new JComboBox<String>();
		cmb_kaynak.setBounds(90, 11, 124, 20);
		panel.add(cmb_kaynak);

		JLabel lblNereye = new JLabel("Nereye:");
		lblNereye.setBounds(10, 51, 46, 14);
		panel.add(lblNereye);

		JComboBox<String> cmb_hedef = new JComboBox<String>();
		cmb_hedef.setBounds(90, 48, 124, 20);
		panel.add(cmb_hedef);

		for (String item : Graf.sehirIsimleri) {
			cmb_kaynak.addItem(item);
			cmb_hedef.addItem(item);
		}

		JButton btn_maxKar = new JButton("Maximum Kâr");
		btn_maxKar.setBounds(10, 76, 204, 23);
		panel.add(btn_maxKar);

		JButton btn_optUcret = new JButton("Optimum Ücret");
		btn_optUcret.setBounds(10, 110, 204, 23);
		panel.add(btn_optUcret);

		JPanel panel2 = new JPanel();
		panel2.setBorder(new TitledBorder(null, "Güzergâhlar", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel2.setBounds(220, 0, 214, 271);
		frmMain.getContentPane().add(panel2);
		panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		/*
		 * Maximum kâr butonu için: ProblemCoz statik sýnýfýný kullanarak
		 * problemi çözer ve Harita sýnýfýndan türeterek çizdirir.
		 */
		btn_maxKar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel2.removeAll();
				panel2.repaint();
				if (cmb_kaynak.getSelectedIndex() != 0 && cmb_hedef.getSelectedIndex() != 0) {
					Graf.kaynak_hedefBelirle(cmb_kaynak.getSelectedIndex(), cmb_hedef.getSelectedIndex());
					Graf.sehirleriEkle();
					kaynak = Graf.sehirler.get(cmb_kaynak.getSelectedIndex() - 1);
					hedef = Graf.sehirler.get(cmb_hedef.getSelectedIndex() - 1);
					List<List<Sehir>> guzergahlar = ProblemCoz.maxKarProblemi(kaynak, hedef);

					for (int i = 0; i < guzergahlar.size(); i++) {
						JButton buton = new JButton();
						panel2.add(buton);
						buton.setText("Güzergâh " + (i + 1));
						Harita harita = new Harita(guzergahlar.get(i));
						buton.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								harita.setVisible(true);	
							}
						});
					}
					idealGuzergah(1, panel2);
				} else
					JOptionPane.showInternalMessageDialog(frmMain.getContentPane(),
							"Lütfen kaynak ve hedef þehri seçiniz!", "HATA", JOptionPane.WARNING_MESSAGE);

				
			}
		});
		
		/*
		 * Optimum Ücret butonu için: ProblemCoz statik sýnýfýný kullanarak
		 * problemi çözer ve Harita sýnýfýndan türeterek çizdirir.
		 */
		btn_optUcret.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel2.removeAll();
				panel2.repaint();
				if (cmb_kaynak.getSelectedIndex() != 0 && cmb_hedef.getSelectedIndex() != 0) {
					Graf.kaynak_hedefBelirle(cmb_kaynak.getSelectedIndex(), cmb_hedef.getSelectedIndex());
					Graf.sehirleriEkle();
					kaynak = Graf.sehirler.get(cmb_kaynak.getSelectedIndex() - 1);
					hedef = Graf.sehirler.get(cmb_hedef.getSelectedIndex() - 1);
					List<List<Sehir>> guzergahlar = ProblemCoz.optUcretProblemi(kaynak, hedef);

					for (int i = 0; i < guzergahlar.size(); i++) {
						JButton buton = new JButton();
						panel2.add(buton);
						buton.setText((i + 1) * 10 + " Yolcu");
						Harita harita = new Harita(guzergahlar.get(i));
						buton.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								harita.setVisible(true);	
							}
						});
					}
					idealGuzergah(2, panel2);
				} else
					JOptionPane.showInternalMessageDialog(frmMain.getContentPane(),
							"Lütfen kaynak ve hedef þehri seçiniz!", "HATA", JOptionPane.WARNING_MESSAGE);
			}
		});
	}

	private void idealGuzergah(int mod, JPanel panel) {
		/*
		 * Bu fonksiyonun tek amacý ideal güzergâhý sonuç metin belgesinden
		 * çekip Label oluþturmaktýr.
		 */
		String ilk = null;
		try {
			BufferedReader reader;
			if (mod == 1)
				reader = new BufferedReader(new FileReader("maxKar.txt"));
			else
				reader = new BufferedReader(new FileReader("optUcret.txt"));
			ilk = reader.readLine();
			while (ilk != null) {
				if (ilk.contains("sýralama")) {
					ilk = reader.readLine();
					reader.close();
					if (ilk.contains(">"))
						ilk = ilk.substring(0, ilk.indexOf('>'));
					break;
				}
				ilk = reader.readLine();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		JLabel bilgi = new JLabel();
		panel.add(bilgi);
		if (!ilk.startsWith("Güzergah"))
			bilgi.setText("Güzergah bulunamadý!");
		else
			bilgi.setText("En kârlý güzergah: " + ilk);

		JLabel bilgi2 = new JLabel();
		panel.add(bilgi2);
		if (mod == 1)
			bilgi2.setText("Detaylar maxKar.txt dosyasýndadýr.");
		else
			bilgi2.setText("Detaylar optUcret.txt dosyasýndadýr.");
	}
}
