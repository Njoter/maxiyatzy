import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import static java.lang.Integer.parseInt;
import static java.lang.System.exit;
import static java.lang.System.out;

public class Spiller {
	private String navn;
	private int totalPoeng = 0;
	private int ekstraKast = 0;
	private boolean bonus = false;
	private boolean ferdig = false;
	public Kombinasjoner kombinasjoner = new Kombinasjoner();

	public Spiller(String navn) {
		this.navn = navn;
	}

	public String getNavn() {
		return this.navn;
	}

	public int getTotalPoeng() {
		return this.totalPoeng;
	}

	public void setTotalPoeng(int poeng) {
		this.totalPoeng += poeng;
	}

	public int getEkstraKast() {
		return ekstraKast;
	}

	public void setEkstraKast(int ekstraKast) {
		this.ekstraKast += ekstraKast;
	}

	public void setBonus(boolean bonus) {
		this.bonus = bonus;
	}

	public boolean erFerdig() {
		return this.ferdig;
	}

	public boolean sjekkOmFerdig() {
		for (int i = 0; i < Kombinasjoner.ANTALLKOMBINASJONER; i++) {
			if (kombinasjoner.getKombinasjonPoeng(i) == 0) {
				if (!kombinasjoner.isKombinasjonOfret(i))
					return false;
			}
		}
		this.ferdig = true;
		return true;
	}

	public void visPoengArk() {
		// Åpne filen 'poengark.txt' som inneholder malen for poengarket.
		File poengark = new File("poengark.txt");
		Scanner filscan = null;
		try {
			filscan = new Scanner(poengark);
		} catch (FileNotFoundException e) {
			out.println("Finner ikke poengark.txt");
			exit(1);
		}

		out.println("\n--------------------|");
		int index; // Brukes som index til getKombinasjonPoeng.
		int sumOgBonusIndex = 0; // Brukes kun på radene for 'sum' og 'bonus'.
		while (filscan.hasNextLine()) {
			// Lagre hver rad i String. Hver rad, bortsett fra 'bonus' og 'sum', har linjenummer.
			// Linjenumrene kan brukes som index ved hjelp av split og parseInt.
			String rad = filscan.nextLine();
			String[] finnIndex = rad.split("(\\.)");
			try {
				index = parseInt(finnIndex[0]) - 1;
			} catch (NumberFormatException nfe) {
				index = 99;
			}
			// Radene 'bonus' og 'sum' har ikke linjenummer og må derfor hoppes over av index.
			// index settes derfor til 99.
			out.print(rad);
			if (index == 99) {
				sumOgBonusIndex++;
				if (sumOgBonusIndex == 2) {
					if (kombinasjoner.poengOverStreken() != 0) {
						out.print(" " + kombinasjoner.poengOverStreken());
					}
				} else if (sumOgBonusIndex == 3) {
					if (bonus)
						out.print(" 100");
				}
				out.println();
			} else {
				if (kombinasjoner.isKombinasjonOfret(index))
					out.print(" --");
				else if (kombinasjoner.getKombinasjonPoeng(index) != 0)
					out.print(" " + kombinasjoner.getKombinasjonPoeng(index));
				out.println();
			}
		}
		out.println("--------------------|");
		out.println("Ekstra kast: " + this.ekstraKast);
		out.println();
	}
}
