package excelSwing;

public class Personel {
	
	String adiSoyadi;
	String kimlikNumarasi;
	String yuzOkumaNumarasi;

	public Personel(String adiSoyadi, String kimlikNumarasi, String yuzOkumaNumarasi) {
		super();
		this.adiSoyadi = adiSoyadi;
		this.kimlikNumarasi = kimlikNumarasi;
		this.yuzOkumaNumarasi = yuzOkumaNumarasi;
	}
	
	@Override
	public String toString() {
		return "Personel [adiSoyadi=" + adiSoyadi + ", kimlikNumarasi=" + kimlikNumarasi + ", yuzOkumaNumarasi="
				+ yuzOkumaNumarasi + "]";
	}
	
	public String getAdiSoyadi() {
		return adiSoyadi;
	}

	public void setAdiSoyadi(String adiSoyadi) {
		this.adiSoyadi = adiSoyadi;
	}
	public String getKimlikNumarasi() {
		return kimlikNumarasi;
	}
	public void setKimlikNumarasi(String kimlikNumarasi) {
		this.kimlikNumarasi = kimlikNumarasi;
	}
	public String getYuzOkumaNumarasi() {
		return yuzOkumaNumarasi;
	}
	public void setYuzOkumaNumarasi(String yuzOkumaNumarasi) {
		this.yuzOkumaNumarasi = yuzOkumaNumarasi;
	}

}
