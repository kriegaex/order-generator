public class BgControls {
	public static void forecast(long customer, String month)
		throws BestellException
	{
		if (customer == Util.UNDEFINED)
			System.out.printf("Erzeuge Forecast für alle Kunden, Monat %s ...%n", month);
		else
			System.out.printf("Erzeuge Forecast für Kunde Nr. %d, Monat %s ...%n", customer, month);
	}

	public static void calculation(long customer, String month)
		throws BestellException
	{
		if (customer == Util.UNDEFINED)
			System.out.printf("Erzeuge Kalkulation für alle Kunden, Monat %s ...%n", month);
		else
			System.out.printf("Erzeuge Kalkulation für Kunde Nr. %d, Monat %s ...%n", customer, month);
	}
}
