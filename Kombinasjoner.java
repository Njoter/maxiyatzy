import java.util.Arrays;
import static java.lang.System.out;

public class Kombinasjoner {
	public static final int ANTALLKOMBINASJONER = 20;
	private int[] kombinasjonPoeng = new int[ANTALLKOMBINASJONER];
	private boolean[] kombinasjonOfret = new boolean[ANTALLKOMBINASJONER];
	private static final String[] kombinasjonNavn = {"enere", "toere", "treere", "firere", "femmere",
							 "seksere", "et par", "to par", "tre par", "tre like",
							 "fire like", "fem like", "liten straight", "stor straight",
							 "full straight", "hytte", "hus", "tårn", "sjanse", "yatzy"};

	private static final String[] kombinasjonBeskrivelse = {"", "", "", "", "", "", "", "", "", "", "", "",
									"1,2,3,4,5 + hva som helst", "2,3,4,5,6, + hva som helst",
									"1,2,3,4,5,6", "2 + 3 like", "3 like + 3 like", "2 like + 4 like",
									"seks like"};

	public Kombinasjoner() {
		for (int i = 0; i < kombinasjonPoeng.length; i++) {
			kombinasjonPoeng[i] = 0;
			kombinasjonOfret[i] = false;
		}
	}

	public static String getKombinasjonNavn(int index) {
		return kombinasjonNavn[index];
	}

	public static String getKombinasjonBeskrivelse(int index) {
		return kombinasjonBeskrivelse[index];
	}

	public int getKombinasjonPoeng(int index) {
		return this.kombinasjonPoeng[index];
	}

	public void setKombinasjonPoeng(int poeng, int index) {
		this.kombinasjonPoeng[index -1] = poeng;
	}

	public boolean isKombinasjonOfret(int index) {
		return this.kombinasjonOfret[index];
	}

	public void setKombinasjonOfret(int index, boolean bool) {
		this.kombinasjonOfret[index] = bool;
	}

	public int poengOverStreken() {
		int sum = 0;
		for (int i = 0; i < 5; i++) {
			sum += kombinasjonPoeng[i];
		}
		return sum;
	}

	public static int sjekkKombinasjon(int kombinasjon, Terning[] terning) {
		int poeng = 0;

		switch (kombinasjon) {
			case 1, 2, 3, 4, 5, 6 -> poeng = kombinasjonerOverStreken(kombinasjon, terning);
			case 7 ->  poeng = par(terning, 1);
			case 8 ->  poeng = par(terning, 2);
			case 9 ->  poeng = par(terning, 3);
			case 10 -> poeng = like(terning, 3);
			case 11 -> poeng = like(terning, 4);
			case 12 -> poeng = like(terning, 5);
			case 13 -> poeng = straight(terning, "liten");
			case 14 -> poeng = straight(terning, "stor");
			case 15 -> poeng = straight(terning, "full");
			case 16 -> poeng = bygning(terning, "hytte");
			case 17 -> poeng = bygning(terning, "hus");
			case 18 -> poeng = bygning(terning, "tårn");
			case 19 -> poeng = sjanse(terning);
			case 20 -> poeng = maxiYatzy(terning);
		}
		return poeng;
	}

	private static int kombinasjonerOverStreken(int tall, Terning[] terning) {
		int counter = 0;
		int poeng = 0;
		for (Terning t : terning) {
			if (t.getVerdi() == tall) {
				poeng += tall;
				counter++;
			}
		}
		if (counter == 0) {
			out.println("Du fikk ingen " + kombinasjonNavn[tall - 1] + ".");
		} else
			out.println("Du fikk " + counter + " " + kombinasjonNavn[tall - 1] + ".");
		return poeng;
	}

	private static int par(Terning[] terning, int antallPar) {
		int[] forekomsterAvTall = forekomsterAvTalliTerningArray(terning);
		int[] tallTab = new int[antallPar];
		Arrays.fill(tallTab, 0);
		String[] antallParTekst = {"et par", "to par", "tre par"};

		int index = forekomsterAvTall.length - 1;
		for (int i = 0; i < antallPar; i++) {
			for (int j = index; j >= 0; j--) {
				if (forekomsterAvTall[j] > 1) {
					tallTab[i] = j+1;
					index = j - 1;
					break;
				}
			}
			if (tallTab[i] == 0) {
				out.println("Du har ikke " + antallParTekst[antallPar-1] + ".");
				return 0;
			}
		}
		int tall1 = tallTab[0], tall2 = tallTab[1], tall3 = tallTab[2];

		int poeng = 0;
		switch (antallPar) {
			case 1 -> {
				out.println("Du fikk par i " + kombinasjonNavn[tall1 - 1] + ".");
				poeng = tall1 * 2;
			}
			case 2 -> {
				out.println("Du fikk par i " + kombinasjonNavn[tall1 - 1] + " og "
											 + kombinasjonNavn[tall2 - 1] + ".");
				poeng = (tall1 * 2) + (tall2 * 2);
			}
			case 3 -> {
				out.println("du fikk par i " + kombinasjonNavn[tall1 - 1] + ", "
											 + kombinasjonNavn[tall2 - 1] + " og "
											 + kombinasjonNavn[tall3 - 1] + ".");
				poeng = (tall1 * 2) + (tall2 * 2) + (tall3 * 2);
			}
		}
		return poeng;
	}
	
	private static int like(Terning[] terning, int antallLike) {
		int[] forekomsterAvTall = forekomsterAvTalliTerningArray(terning);
		String[] tallStr = {"tre", "fire", "fem"};
		int i;
		int poeng = 0;

		for (i = (forekomsterAvTall.length - 1); i >= 0; i--) {
			if (forekomsterAvTall[i] >= antallLike) {
				poeng = (i+1) * antallLike;
				break;
			}
		}

		if (poeng != 0) {
			out.println("Du fikk " + tallStr[antallLike-1] + " like av " + kombinasjonNavn[i] + ".");
		} else
			out.println("Du fikk ikke " + tallStr[antallLike-1] + " like.");
		return poeng;
	}

	private static int straight(Terning[] terning, String straightType) {
		int startPos = 0, sluttPos = 0;
		int poeng = 0, ekstraPoeng = 0;

		switch (straightType) {
			case "liten" -> {
				sluttPos = terning.length - 1;
				poeng = 15;
			}
			case "stor" -> {
				startPos = 1;
				sluttPos = terning.length;
				poeng = 20;
			}
			case "full" -> {
				sluttPos = terning.length;
				poeng = 30;
			}
		}

		int[] forekomsterAvTall = forekomsterAvTalliTerningArray(terning);
		int counter = 0;
		for (int i = startPos; i < sluttPos; i++) {
			if (forekomsterAvTall[i] > 0)
				counter++;
			if (forekomsterAvTall[i] > 1) {
				ekstraPoeng = i+1;
			}
		}

		if (counter != sluttPos) {
			out.println("Du fikk ikke " + straightType + " straight.");
			return 0;
		}
		if (ekstraPoeng == 0 && straightType.equals("liten"))
			ekstraPoeng = 6;
		else if (ekstraPoeng == 0 && straightType.equals("stor"))
			ekstraPoeng = 1;

		out.println("Du fikk " + straightType + " straigth.");
		return poeng + ekstraPoeng;
	}

	private static int bygning(Terning[] terning, String bygningType) {
		int antallLike1 = 0, antallLike2 = 0;
		switch (bygningType) {
			case "hytte" -> {
				antallLike1 = 2;
				antallLike2 = 3;
			}
			case "hus" -> {
				antallLike1 = 3;
				antallLike2 = 3;
			}
			case "tårn" -> {
				antallLike1 = 2;
				antallLike2 = 4;
			}
		}
		int tall1 = like(terning, antallLike1) / antallLike1;
		int tall2 = like(terning, antallLike2) / antallLike2;

		if (tall1 == 0 || tall2 == 0) {
			out.println("Du har ikke " + bygningType + ".");
			return 0;
		}
		out.println("Du fikk " + bygningType + " i " + kombinasjonNavn[tall1-1] + " og "
													 + kombinasjonNavn[tall2-1] + ".");
		return (tall1 * antallLike1) + (tall2 * antallLike2);
	}

	private static int sjanse(Terning[] terning) {
		int poeng = 0;
		for (Terning t : terning) {
			poeng += t.getVerdi();
		}
		return poeng;
	}

	private static int maxiYatzy(Terning[] terning) {
		int poeng = 0;
		int[] forekomsterAvTall = forekomsterAvTalliTerningArray(terning);
		for (int j : forekomsterAvTall) {
			if (j == terning.length) {
				poeng = 100;
				break;
			}
		}

		if (poeng == 0)
			out.println("Du har ikke yatzy.");
		else
			out.println("Gratulerer! Du fikk yatzy!");
		return poeng;
	}

	private static int[] forekomsterAvTalliTerningArray(Terning[] terning) {
		int[] forekomsterAvTall = new int[Terning.getSider()];
		Arrays.fill(forekomsterAvTall, 0);
		for (Terning t : terning)
			forekomsterAvTall[t.getVerdi() - 1]++;

		return forekomsterAvTall;
	}
}
