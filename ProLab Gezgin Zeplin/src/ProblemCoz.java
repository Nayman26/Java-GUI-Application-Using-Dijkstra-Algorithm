import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ProblemCoz {

	/*
	 * Bu s�n�f iki problemin de ��z�m�nden ve sonu�lar�n ��kt�s�ndan sorunlu
	 * statik s�n�ft�r.
	 */
	
	// Yak�t paras� KM/TL dir.
	private final static int YOLCUUCRETI = 200, YAKITPARASI = 10;
	private static List<Double> karlar = new ArrayList<>();

	static List<List<Sehir>> maxKarProblemi(Sehir kaynak, Sehir hedef) {
		/*
		 *  Maximum k�r probleminin ��z�m� i�in kullan�lan fonksiyon. Ka� farkl�
		 *  g�zergah buldu�unu d�nd�r�r.
		 */
		long baslangicZamani = System.nanoTime();
		List<Sehir> enKisaGuzergah, tempEnKisaGuzergah = null;
		List<List<Sehir>> guzergahlar = new ArrayList<>();
		List<Integer> kisiSayilari = new ArrayList<>();

		// 5 ile 50 aras� yolcu say�lar� i�in t�m farkl� en iyi g�zergahlar listelenir.
		int yolcuSayisi;
		for (yolcuSayisi = 5; yolcuSayisi <= 50; yolcuSayisi++) {
			Graf.egimAyarla(80 - yolcuSayisi);
			enKisaGuzergah = Dijkstra.enKisaYol(kaynak, hedef);
			if (enKisaGuzergah != null) {
				if (!enKisaGuzergah.equals(tempEnKisaGuzergah)) {
					guzergahlar.add(enKisaGuzergah);
					tempEnKisaGuzergah = enKisaGuzergah;
					if (yolcuSayisi != 5)
						kisiSayilari.add(yolcuSayisi - 1);
				}
			} else
				break;
		}
		kisiSayilari.add(--yolcuSayisi);

		// Listelenen g�zergahlar dosyaYaz fonksiyonu ile uygun formatta yazd�r�l�r.
		try {
			FileWriter writer = new FileWriter("maxKar.txt");
			writer.write("Yolculuk: " + kaynak.getSehirIsmi() + " --> " + hedef.getSehirIsmi() + "\r\n");
			writer.write(guzergahlar.size() + " farkl� g�zergah bulundu" + "\r\n");
			writer.write("\r\n***********************************************\r\n");
			writer.close();
			for (int i = 0; i < guzergahlar.size(); i++) {
				dosyaYaz(1, guzergahlar, kisiSayilari, i);
			}
			writer = new FileWriter("maxKar.txt", true);

			List<Integer> siralama = new ArrayList<>();

			for (int i = 0; i < karlar.size(); i++) {
				int enBuyuk = 0;
				for (int j = 0; j < karlar.size(); j++)
					if (karlar.get(j) > karlar.get(enBuyuk))
						enBuyuk = j;
				karlar.set(enBuyuk, -Double.MAX_VALUE);
				siralama.add(enBuyuk);
			}

			writer.write("Kara g�re s�ralama:\r\n");
			if (siralama.size() > 0) {
				for (int i = 0; i < siralama.size(); i++) {
					int index = siralama.get(i) + 1;
					writer.write("G�zergah " + index);
					if (i != siralama.size() - 1) {
						writer.write(" > ");
					}
				}
			} else
				writer.write("YOK");

			karlar.clear();
			siralama.clear();
			writer.write("\r\n\n�al��ma zaman�: " + (System.nanoTime() - baslangicZamani) / 1000000 + "ms\r\n");
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return guzergahlar;
	}

	static List<List<Sehir>> optUcretProblemi(Sehir kaynak, Sehir hedef) {
		/*
		 * %50 kar i�in optimum �cret probleminin ��z�m� i�in kullan�lan
		 * fonksiyon. Ka� farkl� g�zergah buldu�unu d�nd�r�r.
		 */
		long baslangicZamani = System.nanoTime();
		List<List<Sehir>> guzergahlar = new ArrayList<>();
		List<Integer> yolcuSayilari = new ArrayList<>();
		// 10,20,30,40 ve 50 yolcu i�in en k�sa g�zergahlar bulunur ve listelenir.
		for (int yolcuSayisi = 10; yolcuSayisi <= 50; yolcuSayisi = yolcuSayisi + 10) {
			Graf.egimAyarla(80 - yolcuSayisi);
			List<Sehir> guzergah = Dijkstra.enKisaYol(kaynak, hedef);
			if (guzergah == null) {
				break;
			}
			guzergahlar.add(guzergah);
			yolcuSayilari.add(yolcuSayisi);
		}

		// Listelenen g�zergahlar dosyaYaz fonksiyonu ile uygun formatta yazd�r�l�r.
		try {
			FileWriter writer = new FileWriter("optUcret.txt");
			writer.close();
			for (int i = 0; i < guzergahlar.size(); i++) {
				double maliyet = dosyaYaz(2, guzergahlar, yolcuSayilari, i);
				double opt = maliyet * 3 / 2 / yolcuSayilari.get(i);
				writer = new FileWriter("optUcret.txt", true);
				writer.write("\r\n" + yolcuSayilari.get(i) + " yolcu --> %50 k�r i�in ki�i ba�� �cret: " + opt + " TL");
				writer.write("\r\n\n***********************************************\r\n");
				writer.close();
			}
			writer = new FileWriter("optUcret.txt", true);
			for (int i = 1; i <= 5; i++) {
				if (guzergahlar.size() >= i) {
					continue;
				}
				int yolcuS = i * 10;
				writer.write("\r\n" + yolcuS + " yolcu i�in g�zergah bulunamad�! \r\n");
				writer.write("\r\n***********************************************\r\n");
			}

			List<Integer> siralama = new ArrayList<>();

			for (int i = 0; i < karlar.size(); i++) {
				int enBuyuk = 0;
				for (int j = 0; j < karlar.size(); j++)
					if (karlar.get(j) > karlar.get(enBuyuk))
						enBuyuk = j;
				karlar.set(enBuyuk, -Double.MAX_VALUE);
				siralama.add(enBuyuk);
			}

			writer.write("K�ra g�re s�ralama:\r\n");
			if (siralama.size() > 0) {
				for (int i = 0; i < siralama.size(); i++) {
					int index = siralama.get(i) + 1;
					writer.write("G�zergah " + index);
					if (i != siralama.size() - 1) {
						writer.write(" > ");
					}
				}
			} else
				writer.write("YOK");

			karlar.clear();
			siralama.clear();

			writer.write("\r\n\n�al��ma zaman�: " + (System.nanoTime() - baslangicZamani) / 1000000 + "ms\r\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return guzergahlar;
	}

	static private double dosyaYaz(int mod, List<List<Sehir>> guzergahlar, List<Integer> yolcuSayilari, int index)
			throws IOException {
		/*
		 * Bu fonksiyon al�nan g�zergah ve yolcu say�s� bilgisi ile dosyaya
		 * maliyet, gelir, kar ve detayl� olarak gidilen g�zergah bilgisini
		 * hesaplay�p yazd�r�r.
		 */
		String dosyaAdi = "HATA.txt";
		if (mod == 1) {
			dosyaAdi = "maxKar.txt";
		} else if (mod == 2) {
			dosyaAdi = "optUcret.txt";
		}
		List<Sehir> guzergah = guzergahlar.get(index);
		int yolcuSayisi = yolcuSayilari.get(index);
		int guzergahNo = index + 1;
		double mesafe = 0, maliyet, kar;
		int para;
		Sehir temp;
		for (int i = 0; i < guzergah.size() - 1; i++) {
			temp = guzergah.get(i);
			mesafe += temp.komsuyaMesafe((guzergah.get(i + 1)));
		}
		maliyet = mesafe * YAKITPARASI;
		para = yolcuSayisi * YOLCUUCRETI;
		kar = ((para - maliyet) / maliyet) * 100;
		FileWriter writer = new FileWriter(dosyaAdi, true);
		writer.write("---G�zergah " + guzergahNo + "---\r\n\n");
		if (mod == 1) {
			if (index != 0) {
				writer.write("Yolcu say�s�: " + yolcuSayilari.get(index - 1) + " - " + yolcuSayisi + "\r\n");
			} else if (yolcuSayisi == 5) {
				writer.write("Yolcu say�s�: 5\r\n");
			} else {
				writer.write("Yolcu say�s�: 5 - " + yolcuSayisi + "\r\n");
			}
		} else if (mod == 2) {
			writer.write("Yolcu say�s�: " + yolcuSayisi + "\r\n");
		}

		writer.write("Maliyet: " + maliyet + " TL\r\n");
		writer.write("Gelir: " + para + " TL\r\n\n");

		for (int i = 0; i < guzergah.size() - 1; i++) {
			Sehir sehir = guzergah.get(i), sonraki = guzergah.get(i + 1);

			writer.write(sehir.getSehirIsmi() + " (" + sehir.getLat() + ", " + sehir.getLongt() + ") --> "
					+ sonraki.getSehirIsmi() + " (" + sonraki.getLat() + ", " + sonraki.getLongt() + ")");
			writer.write("\r\n<---" + sehir.komsuyaMesafe(sonraki) + "km--->");
			writer.write("\r\n<---" + sehir.komsuyaEgim(sonraki) + " derece--->\r\n\n");
		}
		writer.write("\r\nToplam: " + mesafe + "km\r\n");
		if (kar >= 0) {
			writer.write("K�r: %" + kar + "\r\n");
		} else {
			writer.write("Zarar: %" + -kar + "\r\n");
		}

		if (mod == 1)
			writer.write("\r\n***********************************************\r\n");

		writer.close();
		karlar.add(kar);
		return maliyet;
	}
}
