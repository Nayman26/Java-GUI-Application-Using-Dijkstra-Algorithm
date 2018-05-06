import java.util.ArrayList;
import java.util.List;

class Sehir {

	private double lat, longt, rakim;
	private int plaka;
	private List<Sehir> tumKomsular = new ArrayList<>(), komsular = new ArrayList<>();
	private List<Double> tumMesafeler = new ArrayList<>(), tumEgimler = new ArrayList<>(),
			mesafeler = new ArrayList<>();

	Sehir(double lat, double longt, int plaka, double rakim) {
		this.lat = lat;
		this.longt = longt;
		this.plaka = plaka;
		this.rakim = rakim / 1000;
	}

	void komsulukEkle(Sehir komsu, Sehir kaynak, Sehir hedef) {
		/*
		 * Þehrin tüm komþularýný, aradaki mesafe ile birlikte ekleyen
		 * fonksiyon.
		 */
		double uzaklik2D = mesafeHesapla(lat, longt, komsu.lat, komsu.longt);
		double uzaklik3D;
		if (this.equals(kaynak)) {
			uzaklik3D = Math.sqrt(Math.pow(uzaklik2D, 2) + Math.pow(rakim - (komsu.rakim + 0.05), 2));
		} else if (komsu.equals(hedef)) {
			uzaklik3D = Math.sqrt(Math.pow(uzaklik2D, 2) + Math.pow((rakim + 0.05) - komsu.rakim, 2));
		} else {
			uzaklik3D = Math.sqrt(Math.pow(uzaklik2D, 2) + Math.pow(rakim - komsu.rakim, 2));
		}
		tumMesafeler.add(uzaklik3D);
		tumKomsular.add(komsu);
	}

	private void komsulukSifirla() {
		/*
		 * Komsuluklarý güncellemeden önce eski eðim deðerinden kurtulmak için
		 * çalýþtýrýlýr.
		 */
		mesafeler.clear();
		mesafeler.addAll(tumMesafeler);
		komsular.clear();
		komsular.addAll(tumKomsular);
		tumEgimler.clear();
	}

	void komsulukGuncelle(double egim, Sehir kaynak, Sehir hedef) {
		/*
		 * Verilen eðim deðerine göre komþuluklarý güncelleyen fonksiyon.
		 */
		List<Integer> silinecekIndexler = new ArrayList<>();
		komsulukSifirla();
		for (Sehir komsu : komsular) {
			double uzaklik2D = mesafeHesapla(lat, longt, komsu.lat, komsu.longt);
			double _egim;
			if (this.equals(kaynak)) {
				_egim = Math.toDegrees(Math.atan(Math.abs(rakim * 1000 - (komsu.rakim * 1000 + 50)) / uzaklik2D));
			} else if (komsu.equals(hedef)) {
				_egim = Math.toDegrees(Math.atan(Math.abs((rakim * 1000 + 50) - (komsu.rakim * 1000)) / uzaklik2D));
			} else {
				_egim = Math.toDegrees(Math.atan(Math.abs(rakim * 1000 - (komsu.rakim * 1000)) / uzaklik2D));
			}
			tumEgimler.add(_egim);
			if (_egim > egim)
				silinecekIndexler.add(komsular.indexOf(komsu));
		}

		int sayac = 0;
		for (Integer index : silinecekIndexler) {
			komsular.remove((int) index - sayac);
			mesafeler.remove((int) index - (sayac++));
		}
	}

	double komsuyaMesafe(Sehir komsu) {
		// Þehrin verilen komþuya mesafesini döndürür.
		int index = -1, i = 0;
		for (Sehir sehir : tumKomsular) {
			if (sehir.equals(komsu)) {
				index = i;
				break;
			}
			i++;
		}
		return tumMesafeler.get(index);
	}

	double komsuyaEgim(Sehir komsu) {
		// Þehrin verilen komþuya eðimini döndürür.
		int index = -1, i = 0;
		for (Sehir sehir : tumKomsular) {
			if (sehir.equals(komsu)) {
				index = i;
				break;
			}
			i++;
		}
		return tumEgimler.get(index);
	}

	String getSehirIsmi() {
		return Graf.sehirIsimleri[getPlaka()];
	}

	private final static int aciKM = 6371;

	private static double mesafeHesapla(double lat, double lon, double komsuLat, double komsuLon) {

		/*
		 * Haversine formülü ile iki konum arasýndaki mesafeyi hesaplayan
		 * fonksiyon. Alýntýdýr: https://stackoverflow.com/a/12600225/9347492
		 */

		double latAciklik = Math.toRadians(lat - komsuLat);
		double lonAciklik = Math.toRadians(lon - komsuLon);

		double a = Math.sin(latAciklik / 2) * Math.sin(latAciklik / 2) + Math.cos(Math.toRadians(lat))
				* Math.cos(Math.toRadians(komsuLat)) * Math.sin(lonAciklik / 2) * Math.sin(lonAciklik / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return aciKM * c;
	}

	int getPlaka() {
		return plaka;
	}

	List<Double> getMesafe() {
		return mesafeler;
	}

	List<Sehir> getKomsular() {
		return komsular;
	}

	
	double getLat() {
		return lat;
	}

	
	double getLongt() {
		return longt;
	}

}
