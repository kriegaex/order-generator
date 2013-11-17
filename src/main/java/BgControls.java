public class BgControls {
	public static void forecast(long customer, String month)
		throws BestellException
	{
		System.out.printf("Erzeuge Forecast für Kunde Nr. %d, Monat %s ...%n", customer, month);
	}

	public static void calculation(long customer, String month)
		throws BestellException
	{
		System.out.printf("Erzeuge Kalkulation für Kunde Nr. %d, Monat %s ...%n", customer, month);
	}
}
