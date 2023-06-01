import static java.lang.Integer.parseInt;
import static java.lang.System.*;
import java.util.*;

public class MaxiYatzyMain {

	private static final int ANTALLTERNINGER = 6;
	private static Terning[] terning = new Terning[ANTALLTERNINGER];
	private static Spiller[] spiller;
	private static final Scanner sc = new Scanner(in).useDelimiter(lineSeparator());
	private static int nåværendeSpiller;

	public static void main(String[] args) {
		// Fyll terningArray med terningobjekter.
		for (int i = 0; i < ANTALLTERNINGER; i++)
			terning[i] = new Terning();
		// Introskjerm.
		clearScreen();
		out.println("Velkommen til NJOTERS MAXI YATZY EXTRAVAGAZZI BONAZZI!\n");
		lagSpillerObjekt();
		out.println("Trykk ENTER for å starte spillet");
		sc.next();

		// Spillet starter her.
		boolean ferdig = false;
		do {
			for (int i = 0; i < spiller.length; i++) {
				nåværendeSpiller = i;
				clearScreen();

				out.println(spiller[i].getNavn() + " sin tur.");
				kast();
				int rundePoeng = velgKombinasjon();
				spiller[i].setTotalPoeng(rundePoeng);

				clearScreen();

				out.println(spiller[i].getNavn() + " fikk " + rundePoeng + " poeng.");
				spiller[i].sjekkOmFerdig();
				if (alleSpillereFerdig()) {
					out.println("Alle spillere er ferdige.");
					ferdig = true;
					break;
				} else
					trykkEnter("Trykk ENTER for å fortsette til neste spiller.");
			}
		} while (!ferdig);
	}

	private static void lagSpillerObjekt() {
		int antallSpillere = promptInt("Antall spillere: ");
		spiller = new Spiller[antallSpillere];

		for (int i = 0; i < antallSpillere; i++) {
			out.print("Spiller " + (i+1) + " sitt navn: ");
			String navn = sc.next();
			spiller[i] = new Spiller(navn);
		}
	}

	private static void kast() {
		// Spiller får tre kast, samt muligheten til å beholde terninger de to første kastene.
		// Dersom spiller beholder alle terningene, trengs det ikke å kastes mer.
		// De resterende kastene blir beholdt som 'ekstra kast' til senere.
		for (int i = 0; i < 3; i++) {
			if (alleTerningerBeholdt()) {
				setAlleTerningerBeholdt(false);
				printTerninger(false);
				int antallEkstraKast = 3 - i;
				spiller[nåværendeSpiller].setEkstraKast(antallEkstraKast);
				out.println("\nDu får " + antallEkstraKast + " ekstra kast som du kan bruke senere.");
				vent(2000);
				return;
			} else {
				rullTerninger(i);
				if (i < 2)
					beholdTerninger(true);
			}
		}
		setAlleTerningerBeholdt(false);
		printTerninger(true);

		// Hvis spiller kaster alle tre kastene, kan hen velge å bruke av sine ekstra kast.
		while (spiller[nåværendeSpiller].getEkstraKast() != 0) {
			if (brukEkstraKast()) {
				beholdTerninger(false);
				if (alleTerningerBeholdt()) {
					break;
				} else {
					rullTerninger(3);
					setAlleTerningerBeholdt(false);
					printTerninger(true);
					spiller[nåværendeSpiller].setEkstraKast(-1);
				}
			} else
				break;
		}
		setAlleTerningerBeholdt(false);
		printTerninger(false);
	}

	private static void rullTerninger(int kastNr) {

		String[] kastNrString = {"Første", "Andre", "Tredje", "Ekstra"};
		out.println(kastNrString[kastNr] + " kast.");
		trykkEnter("Trykk ENTER for å kaste terninger.");

		for (int i = 0; i < ANTALLTERNINGER; i++) {
			if (!terning[i].getBeholdt())
				terning[i].rull();
		}
	}

	private static boolean brukEkstraKast() {
		// Spør spiller om hen vil bruke et ekstra kast.
		int valg;
		do {
			out.println("Vil du bruke et ekstra kast?\n" +
						"1 for ja, 0 for nei.");
			do {
				while (!sc.hasNextInt())
					sc.nextLine();
				valg = sc.nextInt();
			} while (valg != 1 && valg != 0);

			if (valg == 1) {
				return true;
			}
		} while (valg != 0);
		return false;
	}

	private static void beholdTerninger(boolean nyttKast) {
		setAlleTerningerBeholdt(false);
		boolean avslutt	= false;
		do {
			printTerninger(nyttKast);
			int valg = promptInt("-----------------------------------------------------------\n" +
									   "1-6:   Beholde/ikke beholde terning.\n" +
								       "8:     Beholde alle/ingen terninger.\n" +
								       "9:     Vis poengark.\n" +
								       "0:     Ferdig.\n");

			if (valg == 8) {
				if (alleTerningerBeholdt())
					setAlleTerningerBeholdt(false);
				else
					setAlleTerningerBeholdt(true);
			} else if (valg == 9) {
				spiller[nåværendeSpiller].visPoengArk();
				trykkEnter("Trykk ENTER for å gå tilbake.");
			} else if (valg >= 1 && valg <= 6) {
				boolean bool = terning[valg-1].getBeholdt();
				terning[valg-1].setBeholdt(!bool);
			} else if (valg == 0)
				avslutt = true;

			nyttKast = false;

		} while (!avslutt);
	}

	private static void printTerninger(boolean nyttKast) {
		clearScreen();
		out.println("Du kastet");
		out.println("-----------------------------");

		// Print første rad (terningNr).
		out.print("TerningNr: ");
		for (int i = 0; i < ANTALLTERNINGER; i++) {
			out.print(" " + (i+1) + " ");
		}
		out.println();

		// Print andre rad (terning verdi).
		out.print("Verdi:     ");
		for (int i = 0; i < ANTALLTERNINGER; i++) {
			out.print(terning[i].toString());
			if (nyttKast)
				vent(70);
		}

		// Kun print 'beholdt:' hvis en eller flere terninger har blitt beholdt.
		if (!ingenTerningerBeholdt()) {
			out.println();
			out.print("Beholdt:   ");
		}

		// Print tredje rad (pil som peker på beholdte terninger).
		for (int i = 0; i < ANTALLTERNINGER; i++) {
			if (terning[i].getBeholdt())
				out.print(" ^ ");
			else
				out.print("   ");
		}
		out.println();
	}

	private static void setAlleTerningerBeholdt(boolean beholdt) {
		for (int i = 0; i < ANTALLTERNINGER; i++)
			terning[i].setBeholdt(beholdt);
	}

	private static boolean ingenTerningerBeholdt() {
		for (int i = 0; i < ANTALLTERNINGER; i++) {
			if (terning[i].getBeholdt()) {
				return false;
			}
		}
		return true;
	}

	private static boolean alleTerningerBeholdt() {
		for (int i = 0; i < ANTALLTERNINGER; i++) {
			if (!terning[i].getBeholdt())
				return false;
		}
		return true;
	}

	private static int velgKombinasjon() {
		spiller[nåværendeSpiller].visPoengArk();
		do {
			int poeng = 0;
			int kombinasjon = promptInt("Skriv 1-20 for å velge kombinasjon.\n" +
											  "0 for å ofre kombinasjon.\n");

			if (kombinasjon == 0) {
				if (ofreKombinasjon())
					return 0;
			} else if (kombinasjon >= 1 && kombinasjon <= Kombinasjoner.ANTALLKOMBINASJONER)
				poeng = Kombinasjoner.sjekkKombinasjon(kombinasjon, terning);

			if (poeng != 0) {
				out.println("Er du sikker på valget ditt?\n" +
							"1 for ja, 0 for nei.");
				int valg;
				do {
					while (!sc.hasNextInt())
						sc.nextLine();
					valg = sc.nextInt();
				} while (valg != 1 && valg != 0);
				if (valg == 1) {
					spiller[nåværendeSpiller].kombinasjoner.setKombinasjonPoeng(poeng, kombinasjon);
					return poeng;
				}
			}
		} while(true);
	}

	private static boolean ofreKombinasjon() {
		int kombinasjon;
		do {
			kombinasjon = promptInt("Skriv 1-20 for å ofre en kombinasjon.\n" +
										  "0 for å gå tilbake.\n");

			if (kombinasjon >= 1 && kombinasjon <= Kombinasjoner.ANTALLKOMBINASJONER) {
				if (spiller[nåværendeSpiller].kombinasjoner.isKombinasjonOfret(kombinasjon - 1))
					out.println("Kombinasjonen er allerede ofret.");
				else {
					out.println("Er du sikker på at du vil ofre " + Kombinasjoner.getKombinasjonNavn(kombinasjon-1) + ".\n" +
								"1 for ja, 0 for nei.");
					int valg;
					do {
						while (!sc.hasNextInt())
							sc.nextLine();
						valg = sc.nextInt();
					} while (valg != 1 && valg != 0);
					if (valg == 1) {
						spiller[nåværendeSpiller].kombinasjoner.setKombinasjonOfret((kombinasjon - 1), true);
						return true;
					}
				}
			}
		} while (kombinasjon != 0);
		return false;
	}

	private static boolean alleSpillereFerdig() {
		for (int i = 0; i < spiller.length; i++) {
			if (!spiller[nåværendeSpiller].erFerdig())
				return false;
		}
		return true;
	}

	private static void trykkEnter(String setning) {
		out.println(setning);
		sc.next();
	}

	private static void vent(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}
	}

	private static void clearScreen() {
		out.print("\033[H\033[2J");
		out.flush();
	}

	private static int promptInt(String str) {
		out.print(str);
		while (!sc.hasNextInt())
			sc.nextLine();
		return sc.nextInt();
	}
}