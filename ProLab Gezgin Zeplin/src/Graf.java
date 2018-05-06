import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Graf {

	/*
	 * Graf yap�s�n�n olu�turuldu�u statik s�n�ft�r. �ocuk �retmeyiniz. Do�ru
	 * kullan�m i�in fonksiyonlar bu s�ra ile �al��t�r�lmal�:
	 * kaynak_hedefBelirle, sehirleriEkle, egimAyarla
	 */
	
	static String[] sehirIsimleri = { "---�ehir Se�iniz---", "Adana", "Ad�yaman", "Afyon", "A�r�", "Amasya", "Ankara",
			"Antalya", "Artvin", "Ayd�n", "Bal�kesir", "Bilecik", "Bing�l", "Bitlis", "Bolu", "Burdur", "Bursa",
			"�anakkale", "�ank�r�", "�orum", "Denizli", "Diyarbak�r", "Edirne", "Elaz��", "Erzincan", "Erzurum",
			"Eski�ehir", "Gaziantep", "Giresun", "G�m��hane", "Hakkari", "Hatay", "Isparta", "Mersin", "�stanbul",
			"�zmir", "Kars", "Kastamonu", "Kayseri", "K�rklareli", "K�r�ehir", "Kocaeli", "Konya", "K�tahya", "Malatya",
			"Manisa", "Kahramanmara�", "Mardin", "Mu�la", "Mu�", "Nev�ehir", "Ni�de", "Ordu", "Rize", "Sakarya",
			"Samsun", "Siirt", "Sinop", "Sivas", "Tekirda�", "Tokat", "Trabzon", "Tunceli", "�anl�urfa", "U�ak", "Van",
			"Yozgat", "Zonguldak", "Aksaray", "Bayburt", "Karaman", "K�r�kkale", "Batman", "��rnak", "Bart�n",
			"Ardahan", "I�d�r", "Yalova", "Karab�k", "Kilis", "Osmaniye", "D�zce" };

	static List<Sehir> sehirler = new ArrayList<>();
	static List<Yol> yollar = new ArrayList<>();
	private static Sehir kaynak, hedef;
	private static int plk_kaynak, plk_hedef;
	
	static void kaynak_hedefBelirle(int kaynak_plaka, int hedef_plaka){
		plk_kaynak = kaynak_plaka;
		plk_hedef = hedef_plaka;
	}
	
	static void sehirleriEkle(){
		sehirler = getSehirler();
	}
	
	static void egimAyarla(double egim) {
		// Verilen e�im de�erine g�re �ehirlerin kom�uluk bilgilerini ve yollar� g�nceller.
		for (Sehir sehir : sehirler) {
				sehir.komsulukGuncelle(egim, kaynak, hedef);		
		}
		yollar = yolAyarla();
	}
	
	private static List<Sehir> getSehirler() {
		/*
		 * Bu fonksiyon t�m �ehirleri; bilgileri ve kom�ular� ile birlikte metin
		 * dosyas�ndan �ekmek ve �ekilen �ehirleri i�eren bir List d�nd�rmek
		 * i�in kullan�l�r.
		 */
		List<Sehir> sehirler = new ArrayList<>();
		BufferedReader bufferedReader;
		try {
			// De�erlerin �ekimi ve yeni �ehir olu�turup List'e eklenmesi.
			bufferedReader = new BufferedReader(new FileReader(new File("degerler.txt")));
			String satir;
			String[] degerler;
			double[] _degerler = new double[4];
			bufferedReader.readLine();
			while ((satir = bufferedReader.readLine()) != null) {
				degerler = satir.split(",");
				for (int i = 0; i < degerler.length; i++)
					_degerler[i] = Double.parseDouble(degerler[i]);
				Sehir sehir = new Sehir(_degerler[0], _degerler[1], (int) _degerler[2], (int) _degerler[3]);
				if ((int) _degerler[2] == plk_kaynak) {
					kaynak = sehir;
				} else if ((int) _degerler[2] == plk_hedef) {
					hedef = sehir;
				}
				sehirler.add(sehir);
			}
			bufferedReader.close();

			// Varolan List i�indeki t�m �ehirlere kom�uluk bilgilerinin
			// eklenmesi.
			bufferedReader = new BufferedReader(new FileReader(new File("komsuluk.txt")));
			List<Integer> plakalar = new ArrayList<>();
			bufferedReader.readLine();
			int sayac = 0;
			while ((satir = bufferedReader.readLine()) != null) {
				degerler = satir.split(",");
				for (int i = 1; i < degerler.length; i++)
					plakalar.add(Integer.parseInt(degerler[i]));

				for (Integer plaka : plakalar)
					sehirler.get(sayac).komsulukEkle(sehirler.get(plaka - 1), kaynak, hedef);

				plakalar.clear();
				sayac++;
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sehirler;
	}

	private static List<Yol> yolAyarla() {
		// Bu fonksiyon Graf i�in yollar� olu�turur.
		List<Yol> yollar = new ArrayList<>();
		for (Sehir sehir : sehirler)
			for (Sehir komsu : sehir.getKomsular())
				yollar.add(new Yol(sehir, komsu, sehir.getMesafe().get(sehir.getKomsular().indexOf(komsu))));
		return yollar;
	}

	static void grafYazdir() {
		/*
		 * Bu fonksiyon graf yap�s�ndaki t�m �ehirleri; kom�ular� ve aralar�ndaki
		 * mesafe ile birlikte konsola yazar. Test ama�l�d�r.
		 */
		for (Sehir sehir : sehirler) {
			System.out.println("*****ANA �ehir" + sehir.getPlaka() + "*****");
			for (int i = 0; i < sehir.getKomsular().size(); i++) {
				System.out.println("KOM�U �ehir " + sehir.getKomsular().get(i).getPlaka() + " Mesafe: "
						+ sehir.getMesafe().get(i));
			}
			System.out.println("****************************");

		}
	}

}
