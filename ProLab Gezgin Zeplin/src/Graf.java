import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Graf {

	/*
	 * Graf yapýsýnýn oluþturulduðu statik sýnýftýr. Çocuk üretmeyiniz. Doðru
	 * kullaným için fonksiyonlar bu sýra ile çalýþtýrýlmalý:
	 * kaynak_hedefBelirle, sehirleriEkle, egimAyarla
	 */
	
	static String[] sehirIsimleri = { "---Þehir Seçiniz---", "Adana", "Adýyaman", "Afyon", "Aðrý", "Amasya", "Ankara",
			"Antalya", "Artvin", "Aydýn", "Balýkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa",
			"Çanakkale", "Çankýrý", "Çorum", "Denizli", "Diyarbakýr", "Edirne", "Elazýð", "Erzincan", "Erzurum",
			"Eskiþehir", "Gaziantep", "Giresun", "Gümüþhane", "Hakkari", "Hatay", "Isparta", "Mersin", "Ýstanbul",
			"Ýzmir", "Kars", "Kastamonu", "Kayseri", "Kýrklareli", "Kýrþehir", "Kocaeli", "Konya", "Kütahya", "Malatya",
			"Manisa", "Kahramanmaraþ", "Mardin", "Muðla", "Muþ", "Nevþehir", "Niðde", "Ordu", "Rize", "Sakarya",
			"Samsun", "Siirt", "Sinop", "Sivas", "Tekirdað", "Tokat", "Trabzon", "Tunceli", "Þanlýurfa", "Uþak", "Van",
			"Yozgat", "Zonguldak", "Aksaray", "Bayburt", "Karaman", "Kýrýkkale", "Batman", "Þýrnak", "Bartýn",
			"Ardahan", "Iðdýr", "Yalova", "Karabük", "Kilis", "Osmaniye", "Düzce" };

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
		// Verilen eðim deðerine göre þehirlerin komþuluk bilgilerini ve yollarý günceller.
		for (Sehir sehir : sehirler) {
				sehir.komsulukGuncelle(egim, kaynak, hedef);		
		}
		yollar = yolAyarla();
	}
	
	private static List<Sehir> getSehirler() {
		/*
		 * Bu fonksiyon tüm þehirleri; bilgileri ve komþularý ile birlikte metin
		 * dosyasýndan çekmek ve çekilen þehirleri içeren bir List döndürmek
		 * için kullanýlýr.
		 */
		List<Sehir> sehirler = new ArrayList<>();
		BufferedReader bufferedReader;
		try {
			// Deðerlerin çekimi ve yeni þehir oluþturup List'e eklenmesi.
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

			// Varolan List içindeki tüm þehirlere komþuluk bilgilerinin
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
		// Bu fonksiyon Graf için yollarý oluþturur.
		List<Yol> yollar = new ArrayList<>();
		for (Sehir sehir : sehirler)
			for (Sehir komsu : sehir.getKomsular())
				yollar.add(new Yol(sehir, komsu, sehir.getMesafe().get(sehir.getKomsular().indexOf(komsu))));
		return yollar;
	}

	static void grafYazdir() {
		/*
		 * Bu fonksiyon graf yapýsýndaki tüm þehirleri; komþularý ve aralarýndaki
		 * mesafe ile birlikte konsola yazar. Test amaçlýdýr.
		 */
		for (Sehir sehir : sehirler) {
			System.out.println("*****ANA Þehir" + sehir.getPlaka() + "*****");
			for (int i = 0; i < sehir.getKomsular().size(); i++) {
				System.out.println("KOMÞU þehir " + sehir.getKomsular().get(i).getPlaka() + " Mesafe: "
						+ sehir.getMesafe().get(i));
			}
			System.out.println("****************************");

		}
	}

}
