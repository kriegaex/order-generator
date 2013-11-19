/**
 * Command line interface (CLI) for generating calculations and forecasts
 * per customer and per month
 */
public class OrderGenerator {
	private String[] args;
	private OrderAggregator orderAggregator;

	OrderGenerator(String[] args, OrderAggregator orderAggregator) {
		this.args = args;
		this.orderAggregator = orderAggregator;
	}

	public static void main(String[] args) {
		new OrderGenerator(args, new OrderAggregator()).processArgs();
	}

	void processArgs() {
		int aggregatorMode = Constants.CALCULATION_MODE;
		long customer = Constants.UNDEFINED;
		String month = "";

		try {
			if (args.length == 0) {
				System.out.println("Starte Bg fuer alle Kunden.");
			} else if (args.length == 1) {
				if (Constants.FORECAST_FLAG.equalsIgnoreCase(args[0])) {
					aggregatorMode = Constants.FORECAST_MODE;
					System.out.println("Starte Vorhersage fuer alle Kunden.");
				} else {
					aggregatorMode = Constants.CALCULATION_MODE;
					System.out.println("Starte Bg fuer alle Kunden.");
				}
			} else if (args.length == 2) {
				if("-customer".equals(args[0])) {
					customer = Integer.parseInt(args[1]);
					if (customer < 0) {
						throw new NumberFormatException();
					}
					aggregatorMode = Constants.CALCULATION_MODE;
					System.out.println("Starte Bg fuer Kunden: " + customer);
				} else if("-month".equals(args[0])) {
					month = args[1];
					aggregatorMode = Constants.CALCULATION_MODE;
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
					if (Constants.FORECAST_FLAG.equalsIgnoreCase(args[2])) {
						aggregatorMode = Constants.FORECAST_MODE;
						System.out.println("Starte Vorhersage fuer Kunden: " + customer);
					} else {
						aggregatorMode = Constants.CALCULATION_MODE;
						System.out.println("Starte Bg fuer Kunden: " + customer);
					}
				} else if("-month".equals(args[0])) {
					month = args[1];
					if (Constants.FORECAST_FLAG.equalsIgnoreCase(args[2])) {
						aggregatorMode = Constants.FORECAST_MODE;
						System.out.println("Starte Vorhersage fuer alle Kunden.");
					} else {
						aggregatorMode = Constants.CALCULATION_MODE;
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
					if (Constants.FORECAST_FLAG.equalsIgnoreCase(args[4])) {
						aggregatorMode = Constants.FORECAST_MODE;
						System.out.println("Starte Vorhersage fuer Kunden: " + customer);
					} else {
						aggregatorMode = Constants.CALCULATION_MODE;
						System.out.println("Starte Bg fuer Kunden: " + customer);
					}
				}
			} else {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			System.out.println("usage: run [-customer customernr] [" + Constants.FORECAST_FLAG + "] \n");
			System.exit(1);
		}

		try {
			if (aggregatorMode == Constants.FORECAST_MODE) {
				orderAggregator.forecast(customer, month);
			} else {
				orderAggregator.calculation(customer, month);
			}
		} catch (OrderException e) {
			System.out.println(e);
		}
		System.out.println("Bg-Lauf beendet.");
	}
}
