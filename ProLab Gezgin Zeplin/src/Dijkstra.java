import java.util.*;

class Dijkstra {
	private static final double SONSUZ = Double.MAX_VALUE;

	private static double[][] matrisOlustur(List<Sehir> sehirler, List<Yol> yollar) {
		// Bu fonksiyon Dijkstra a��rl�klar matrisini olu�turur.
		double[][] agirliklar = new double[sehirler.size()][sehirler.size()];
		for (int i = 0; i < sehirler.size(); i++) {
			Arrays.fill(agirliklar[i], SONSUZ);
		}
		for (Yol yol : yollar) {
			agirliklar[yol.getKaynak().getPlaka() - 1][yol.getHedef().getPlaka() - 1] = yol.getAgirlik();
		}
		return agirliklar;
	}
	
	static List<Sehir> enKisaYol(Sehir kaynak, Sehir hedef) {
		// En k�sa g�zerg�h Dijkstra algoritmas� ile hesaplan�r.
		if (kaynak.equals(hedef)) {
			List<Sehir> tek = new ArrayList<>();
			tek.add(kaynak);
			return tek;
		}
		List<Sehir> sehirler = Graf.sehirler;
		List<Yol> yollar = Graf.yollar;
		double[][] agirliklar = matrisOlustur(sehirler, yollar);
		double[] agirliklar_kaynak = new double[sehirler.size()];
		Sehir[] ugrananSehirler = new Sehir[sehirler.size()];
		List<Sehir> adaylar = new ArrayList<>();

		// Adaylar� ve �ehir a��rl�klar�n� ayarlar.
		for (int i = 0; i < sehirler.size(); i++) {
			adaylar.add(sehirler.get(i));
			agirliklar_kaynak[i] = agirliklar[kaynak.getPlaka() - 1][i];
			if (agirliklar_kaynak[i] != SONSUZ) {
				ugrananSehirler[i] = kaynak;
			}
		}

		// Bu d�ng� a��rl�k matrisini kullanarak grafi�i olu�turur.
		for (int i = 0; i < sehirler.size() - 1; i++) {
			double enKisa = SONSUZ;
			Sehir sehir = kaynak;
			// En yak�n aday �ehri bul.
			for (Sehir aday : adaylar) {
				if (agirliklar_kaynak[aday.getPlaka() - 1] < enKisa) {
					sehir = aday;
					enKisa = agirliklar_kaynak[aday.getPlaka() - 1];
				}
			}
			adaylar.remove(sehir);

			/*
			 * Aday kom�ular�n� tek tek tarar ve daha d���k a��rl�k sa�l�yorsa
			 * �ehrin a��rl���n� g�nceller. Anlamas� �ok kolay de�il. Yorum
			 * sat�rlar� koda eklenirse kolayca anla��l�r.
			 */
			for (int j = 0; j < sehirler.size() - 1; j++) {
				if (agirliklar_kaynak[sehir.getPlaka() - 1] != SONSUZ && agirliklar[sehir.getPlaka() - 1][j] != SONSUZ
						&& agirliklar_kaynak[sehir.getPlaka() - 1]
								+ agirliklar[sehir.getPlaka() - 1][j] < agirliklar_kaynak[j]) {
					//System.out.println(kaynak.getSehirIsmi() + "-->" + sehir.getSehirIsmi() + " " + agirliklar_kaynak[sehir.getPlaka() - 1]);
					//System.out.println(sehir.getSehirIsmi() + " --> " + Graf.sehirIsimleri[j+1] + " " + agirliklar[sehir.getPlaka() - 1][j]);
					//System.out.println(kaynak.getSehirIsmi() + " --> " + Graf.sehirIsimleri[j+1] + " " + agirliklar_kaynak[j]);
					agirliklar_kaynak[j] = agirliklar_kaynak[sehir.getPlaka() - 1] + agirliklar[sehir.getPlaka() - 1][j];
					//System.out.println("Art�k " + kaynak.getSehirIsmi() + " --> " + Graf.sehirIsimleri[j+1] + " " + agirliklar_kaynak[j]);
					ugrananSehirler[j] = sehir;
					//System.out.println("ugrananSehirler["+j+"] = " + sehir.getSehirIsmi() + "\n");
				}
			}
		}
		adaylar.clear();

		/* 
		 * U�ranan �ehirler matrisinde hedeften geriye do�ru giderek
		 * en k�sa yol List'ini olu�turur ve sonucu d�nd�r�r.
		 */
		List<Sehir> enKisaYol = new ArrayList<>();
		int siradakiSehir = hedef.getPlaka() - 1;
		enKisaYol.add(hedef);
		while (ugrananSehirler[siradakiSehir] != kaynak) {
			if (ugrananSehirler[siradakiSehir] == null)
				return null;
			enKisaYol.add(0, ugrananSehirler[siradakiSehir]);
			siradakiSehir = ugrananSehirler[siradakiSehir].getPlaka() - 1;
		}
		enKisaYol.add(0, kaynak);
		return enKisaYol;
	}
}