class Yol {

	// İki şehir arasındaki yolu, ağırlık matrisi için tutan sınıftır.
	
	private Sehir kaynak, hedef;
	private double agirlik;

	Yol(Sehir kaynak, Sehir hedef, double agirlik) {
		this.kaynak = kaynak;
		this.hedef = hedef;
		this.agirlik = agirlik;
	}

	Sehir getKaynak() {
		return kaynak;
	}

	Sehir getHedef() {
		return hedef;
	}

	double getAgirlik() {
		return agirlik;
	}
}
