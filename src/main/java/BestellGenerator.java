/**
 * Haupteinstiegspunkt des Bestellgenerators zur 
 * Generierung von Bestellungen für das Gesamtsystem.
 */
public class BestellGenerator {

	/**
	 * Main Methode - liest die Kommandozeilenparameter und
	 * startet den Bestellgenerator
	 */
	public static void main(String[] args) {
		int useCase = GemeinsameKonstanten.CALCULATION_TYPE;
		long customer = Util.UNDEFINED;
		String month = "";

		try {
			if (args.length == 0) {
				System.out.println("Starte Bg fuer alle Kunden.");
			} else if (args.length == 1) {
				if (GemeinsameKonstanten.FORECAST_PARAM.equalsIgnoreCase(args[0])) {
					useCase = GemeinsameKonstanten.FORECAST_TYPE;
					System.out.println("Starte Vorhersage fuer alle Kunden.");
				} else {
					useCase = GemeinsameKonstanten.CALCULATION_TYPE;
					System.out.println("Starte Bg fuer alle Kunden.");
				}
			} else if (args.length == 2) {
				if("-customer".equals(args[0])) {
					customer = Integer.parseInt(args[1]);
					if (customer < 0) {
						throw new NumberFormatException();
					}
					useCase = GemeinsameKonstanten.CALCULATION_TYPE;
					System.out.println("Starte Bg fuer Kunden: " + customer);
				} else if("-month".equals(args[0])) {
					month = args[1];
					useCase = GemeinsameKonstanten.CALCULATION_TYPE;
					System.out.println("Starte Bg fuer alle Kunden.");
				} else {
					throw new NumberFormatException();
				}
			} else if (args.length == 3) {
				if("-customer".equals(args[0])) {
					customer = Integer.parseInt(args[1]);
					if (customer < 0) {
						throw new NumberFormatException();
					}
					if (GemeinsameKonstanten.FORECAST_PARAM.equalsIgnoreCase(args[2])) {
						useCase = GemeinsameKonstanten.FORECAST_TYPE;
						System.out.println("Starte Vorhersage fuer Kunden: " + customer);
					} else {
						useCase = GemeinsameKonstanten.CALCULATION_TYPE;
						System.out.println("Starte Bg fuer Kunden: " + customer);
					}
				} else if("-month".equals(args[0])) {
					month = args[1];
					if (GemeinsameKonstanten.FORECAST_PARAM.equalsIgnoreCase(args[2])) {
						useCase = GemeinsameKonstanten.FORECAST_TYPE;
						System.out.println("Starte Vorhersage fuer alle Kunden.");
					} else {
						useCase = GemeinsameKonstanten.CALCULATION_TYPE;
						System.out.println("Starte Bg fuer alle Kunden.");
					}
				}
			} else if(args.length == 4) {
				if("-customer".equals(args[0]) && "-month".equals(args[2])) {
					customer = Integer.parseInt(args[1]);
					if (customer < 0) {
						throw new NumberFormatException();
					}
					month = args[3];
					System.out.println("Starte Bg fuer Kunden: " + customer);
				} else if("-customer".equals(args[2]) && "-month".equals(args[0])) {
					customer = Integer.parseInt(args[3]);
					if (customer < 0) {
						throw new NumberFormatException();
					}
					month = args[1];
					System.out.println("Starte Bg fuer Kunden: " + customer);
				}
			} else if (args.length == 5) {
				if("-customer".equals(args[2]) && "-month".equals(args[0])) {
					month = args[1];
					customer = Integer.parseInt(args[3]);
					if (customer < 0) {
						throw new NumberFormatException();
					}
					if (GemeinsameKonstanten.FORECAST_PARAM.equalsIgnoreCase(args[4])) {
						useCase = GemeinsameKonstanten.FORECAST_TYPE;
						System.out.println("Starte Vorhersage fuer Kunden: " + customer);
					} else {
						useCase = GemeinsameKonstanten.CALCULATION_TYPE;
						System.out.println("Starte Bg fuer Kunden: " + customer);
					}
				}
			} else {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			System.out.println("usage: run [-customer customernr] [" + GemeinsameKonstanten.FORECAST_PARAM + "] \n");
			System.exit(1);
		}

		try {
			if (useCase == GemeinsameKonstanten.FORECAST_TYPE) {
				BgControls.forecast(customer, month);
			} else {
				BgControls.calculation(customer, month);
			}
		} catch (BestellException e) {
			System.out.println(e);
		}
		System.out.println("Bg-Lauf beendet.");
	}
}
