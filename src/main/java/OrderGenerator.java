import java.util.Collections;
import java.util.Vector;

/**
 * Command line interface (CLI) for generating calculations and forecasts
 * per customer and per month
 */
public class OrderGenerator {
	private Vector<String> args;
	private OrderAggregator orderAggregator;

	OrderGenerator(String[] args, OrderAggregator orderAggregator) {
		this.args = new Vector<>(args.length);
		Collections.addAll(this.args, args);
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
			if (args.size() == 0) {
				System.out.println("Starte Bg fuer alle Kunden.");
			} else if (args.size() == 1) {
				if (Constants.FORECAST_FLAG.equalsIgnoreCase(args.get(0))) {
					aggregatorMode = Constants.FORECAST_MODE;
					System.out.println("Starte Vorhersage fuer alle Kunden.");
				} else {
					aggregatorMode = Constants.CALCULATION_MODE;
					System.out.println("Starte Bg fuer alle Kunden.");
				}
			} else {
				if (args.size() == 2) {
					if(Constants.CUSTOMER_FLAG.equals(args.get(0))) {
						customer = Integer.parseInt(args.get(1));
						if (customer < 0) {
							throw new NumberFormatException();
						}
						aggregatorMode = Constants.CALCULATION_MODE;
						System.out.println("Starte Bg fuer Kunden: " + customer);
					} else if(Constants.MONTH_FLAG.equals(args.get(0))) {
						month = args.get(1);
						aggregatorMode = Constants.CALCULATION_MODE;
						System.out.println("Starte Bg fuer alle Kunden.");
					} else {
						throw new NumberFormatException();
					}
				} else if (args.size() == 3) {
					if(Constants.CUSTOMER_FLAG.equals(args.get(0))) {
						customer = Integer.parseInt(args.get(1));
						if (customer < 0) {
							throw new NumberFormatException();
						}
						if (Constants.FORECAST_FLAG.equalsIgnoreCase(args.get(2))) {
							aggregatorMode = Constants.FORECAST_MODE;
							System.out.println("Starte Vorhersage fuer Kunden: " + customer);
						} else {
							aggregatorMode = Constants.CALCULATION_MODE;
							System.out.println("Starte Bg fuer Kunden: " + customer);
						}
					} else if(Constants.MONTH_FLAG.equals(args.get(0))) {
						month = args.get(1);
						if (Constants.FORECAST_FLAG.equalsIgnoreCase(args.get(2))) {
							aggregatorMode = Constants.FORECAST_MODE;
							System.out.println("Starte Vorhersage fuer alle Kunden.");
						} else {
							aggregatorMode = Constants.CALCULATION_MODE;
							System.out.println("Starte Bg fuer alle Kunden.");
						}
					}
				} else if(args.size() == 4) {
					if(Constants.CUSTOMER_FLAG.equals(args.get(0)) && Constants.MONTH_FLAG.equals(args.get(2))) {
						customer = Integer.parseInt(args.get(1));
						if (customer < 0) {
							throw new NumberFormatException();
						}
						month = args.get(3);
						System.out.println("Starte Bg fuer Kunden: " + customer);
					} else if(Constants.CUSTOMER_FLAG.equals(args.get(2)) && Constants.MONTH_FLAG.equals(args.get(0))) {
						customer = Integer.parseInt(args.get(3));
						if (customer < 0) {
							throw new NumberFormatException();
						}
						month = args.get(1);
						System.out.println("Starte Bg fuer Kunden: " + customer);
					}
				} else if (args.size() == 5) {
					if(Constants.CUSTOMER_FLAG.equals(args.get(2)) && Constants.MONTH_FLAG.equals(args.get(0))) {
						month = args.get(1);
						customer = Integer.parseInt(args.get(3));
						if (customer < 0) {
							throw new NumberFormatException();
						}
						if (Constants.FORECAST_FLAG.equalsIgnoreCase(args.get(4))) {
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
