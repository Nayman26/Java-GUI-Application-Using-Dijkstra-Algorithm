import java.util.*;

class Dijkstra {
	private static final double SONSUZ = Double.MAX_VALUE;

	private static double[][] matrisOlustur(List<Sehir> sehirler, List<Yol> yollar) {
		// Bu fonksiyon Dijkstra aðýrlýklar matrisini oluþturur.
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
		// En kýsa güzergâh Dijkstra algoritmasý ile hesaplanýr.
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

		// Adaylarý ve þehir aðýrlýklarýný ayarlar.
		for (int i = 0; i < sehirler.size(); i++) {
			adaylar.add(sehirler.get(i));
			agirliklar_kaynak[i] = agirliklar[kaynak.getPlaka() - 1][i];
			if (agirliklar_kaynak[i] != SONSUZ) {
				ugrananSehirler[i] = kaynak;
			}
		}

		// Bu döngü aðýrlýk matrisini kullanarak grafiði oluþturur.
		for (int i = 0; i < sehirler.size() - 1; i++) {
			double enKisa = SONSUZ;
			Sehir sehir = kaynak;
			// En yakýn aday þehri bul.
			for (Sehir aday : adaylar) {
				if (agirliklar_kaynak[aday.getPlaka() - 1] < enKisa) {
					sehir = aday;
					enKisa = agirliklar_kaynak[aday.getPlaka() - 1];
				}
			}
			adaylar.remove(sehir);

			/*
			 * Aday komþularýný tek tek tarar ve daha düþük aðýrlýk saðlýyorsa
			 * þehrin aðýrlýðýný günceller. Anlamasý çok kolay deðil. Yorum
			 * satýrlarý koda eklenirse kolayca anlaþýlýr.
			 */
			for (int j = 0; j < sehirler.size() - 1; j++) {
				if (agirliklar_kaynak[sehir.getPlaka() - 1] != SONSUZ && agirliklar[sehir.getPlaka() - 1][j] != SONSUZ
						&& agirliklar_kaynak[sehir.getPlaka() - 1]
								+ agirliklar[sehir.getPlaka() - 1][j] < agirliklar_kaynak[j]) {
					//System.out.println(kaynak.getSehirIsmi() + "-->" + sehir.getSehirIsmi() + " " + agirliklar_kaynak[sehir.getPlaka() - 1]);
					//System.out.println(sehir.getSehirIsmi() + " --> " + Graf.sehirIsimleri[j+1] + " " + agirliklar[sehir.getPlaka() - 1][j]);
					//System.out.println(kaynak.getSehirIsmi() + " --> " + Graf.sehirIsimleri[j+1] + " " + agirliklar_kaynak[j]);
					agirliklar_kaynak[j] = agirliklar_kaynak[sehir.getPlaka() - 1] + agirliklar[sehir.getPlaka() - 1][j];
					//System.out.println("Artýk " + kaynak.getSehirIsmi() + " --> " + Graf.sehirIsimleri[j+1] + " " + agirliklar_kaynak[j]);
					ugrananSehirler[j] = sehir;
					//System.out.println("ugrananSehirler["+j+"] = " + sehir.getSehirIsmi() + "\n");
				}
			}
		}
		adaylar.clear();

		/* 
		 * Uðranan þehirler matrisinde hedeften geriye doðru giderek
		 * en kýsa yol List'ini oluþturur ve sonucu döndürür.
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