import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
class Harita extends JFrame {

	// Bu sýnýf alýnan güzergâhý haritaya yansýtýr.

	private List<Sehir> guzergah = new ArrayList<>();
	private int koordinatlar[][] = new int[81][2];

	Harita(List<Sehir> guzergah) {
		this.guzergah = guzergah;
		setMaximumSize(new Dimension(1366, 768));
		setSize(getMaximumSize());
		setResizable(false);
		setContentPane(new JLabel(new ImageIcon("turkiye.png")));

		koordinatCek();
	}

	private void koordinatCek() {
		// koordinatlar.txt dosyasýndan koordinatlarý bir diziye aktaran fonksiyon
		String[] tempKoordinatlar;
		try {
			BufferedReader reader = new BufferedReader(new FileReader("koordinatlar.txt"));
			for (int i = 0; i < 81; i++) {
				tempKoordinatlar = reader.readLine().split(" ");
				koordinatlar[i][0] = Integer.parseInt(tempKoordinatlar[0]);
				koordinatlar[i][1] = Integer.parseInt(tempKoordinatlar[1]);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		// Noktalarý koyan döngü
		for (int i = 0; i < 81; i++)
			g.fillOval(koordinatlar[i][0] - 5, koordinatlar[i][1] - 5, 10, 10);

		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(5));
		g2.setFont(new Font("Arial", Font.BOLD, 15));

		// Güzergâhýn ve yol mesafelerinin çizimi, yazdýrýlmasý
		for (int i = 0; i < guzergah.size() - 1; i++) {
			Sehir sehir = guzergah.get(i);
			Sehir sonraki = guzergah.get(i + 1);
			
			g2.setColor(Color.RED);
			g2.draw(new Line2D.Float(koordinatlar[sehir.getPlaka() - 1][0], koordinatlar[sehir.getPlaka() - 1][1],
					koordinatlar[sonraki.getPlaka() - 1][0], koordinatlar[sonraki.getPlaka() - 1][1]));
			
			int ii = (koordinatlar[sehir.getPlaka() - 1][0] + koordinatlar[sonraki.getPlaka() - 1][0]) / 2;
			int jj = (koordinatlar[sehir.getPlaka() - 1][1] + koordinatlar[sonraki.getPlaka() - 1][1]) / 2;
			
			g2.setColor(Color.BLACK);
			g2.drawString("" + (double) Math.round(sehir.komsuyaMesafe(sonraki) * 100) / 100 + "KM", ii, jj);
		}
	}

}
