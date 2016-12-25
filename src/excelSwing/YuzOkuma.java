package excelSwing;

import java.util.Date;

public class YuzOkuma {
	
	String cihaz;
	String adiSoyadi;
	String kimlikNumarasi;
	Date okumaTarihi;
	String saat;
	String yuzOkumaNumarasi;
	//TODO: Burada girisSayisi dedigim kisim o gun icin kacinci okuma oldugu
	//		min ve maxi alirim diye dusundum
	//int girisSayisi;		//TODO bunu degistirebilirim
	
	public YuzOkuma() {
		
	}

	public YuzOkuma(String cihaz, String adiSoyadi, String kimlikNumarasi, Date okumaTarihi, String saat,
			String yuzOkumaNumarasi
			//, int girisSayisi
			) {
		super();
		this.cihaz = cihaz;
		this.adiSoyadi = adiSoyadi;
		this.kimlikNumarasi = kimlikNumarasi;
		this.okumaTarihi = okumaTarihi;
		this.saat = saat;
		this.yuzOkumaNumarasi = yuzOkumaNumarasi;
		//this.girisSayisi = girisSayisi;
	}

	@Override
	public String toString() {
		return "Kayit [cihaz=" + cihaz + ", adiSoyadi=" + adiSoyadi + ", kimlikNumarasi=" + kimlikNumarasi
				+ ", okumaTarihi=" + okumaTarihi + ", saat=" + saat + ", yuzOkumaNumarasi=" + yuzOkumaNumarasi + "]";
	}
	
	public String getCihaz() {
		return cihaz;
	}

	public void setCihaz(String cihaz) {
		this.cihaz = cihaz;
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

	public Date getOkumaTarihi() {
		return okumaTarihi;
	}

	public void setOkumaTarihi(Date okumaTarihi) {
		this.okumaTarihi = okumaTarihi;
	}

	public String getSaat() {
		return saat;
	}

	public void setSaat(String saat) {
		this.saat = saat;
	}

	public String getYuzOkumaNumarasi() {
		return yuzOkumaNumarasi;
	}

	public void setYuzOkumaNumarasi(String yuzOkumaNumarasi) {
		this.yuzOkumaNumarasi = yuzOkumaNumarasi;
	}
	
}
