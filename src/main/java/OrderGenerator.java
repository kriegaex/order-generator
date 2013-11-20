import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

/**
 * Command line interface (CLI) for generating calculations and forecasts
 * per customer and per month
 */
public class OrderGenerator {
	private Vector<String> args;
	private OrderAggregator orderAggregator;

	static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM");
	static final String CURRENT_MONTH = DATE_FORMAT.format(new Date());

	OrderGenerator(String[] args, OrderAggregator orderAggregator) {
		this.args = new Vector<>(args.length);
		Collections.addAll(this.args, args);
		this.orderAggregator = orderAggregator;
	}

	public static void main(String[] args) throws OrderException {
		new OrderGenerator(args, new OrderAggregator()).processArgs();
	}

	void processArgs() throws OrderException {
		boolean forecastMode = args.contains(Constants.FORECAST_FLAG);
		long customer = Constants.ALL_CUSTOMERS;
		String month = CURRENT_MONTH;

		args.remove(Constants.FORECAST_FLAG);
		try {
			int optionIndex;
			if ((optionIndex = args.indexOf(Constants.CUSTOMER_FLAG)) != -1) {
				customer = Integer.parseInt(args.get(1 + optionIndex));
				if (customer < 1)
					throw new NumberFormatException("customer ID must be > 0");
				args.removeElementAt(optionIndex);
				args.removeElementAt(optionIndex);
			}
			if ((optionIndex = args.indexOf(Constants.MONTH_FLAG)) != -1) {
				DATE_FORMAT.setLenient(false);
				month = DATE_FORMAT.format(DATE_FORMAT.parse(args.get(1 + optionIndex)));
				args.removeElementAt(optionIndex);
				args.removeElementAt(optionIndex);
			}
			if (args.size() > 0)
				throw new IllegalArgumentException("unknown arguments on command line");
		}
		catch (IllegalArgumentException | ParseException e) {
			printUsageAndExit(1);
		}

		System.out.println("Starte Bestellgenerator ...");
		if (forecastMode)
			orderAggregator.forecast(customer, month);
		else
			orderAggregator.calculation(customer, month);
		System.out.println("Bestellgenerator-Lauf erfolgreich beendet ...");
	}

	private void printUsageAndExit(int exitCode) {
		System.out.printf("Usage: %s [%s <customer ID>] [%s <month>] [%s]%n",
			this.getClass().getSimpleName().replaceFirst("\\$.*", ""),
			Constants.CUSTOMER_FLAG,
			Constants.MONTH_FLAG,
			Constants.FORECAST_FLAG
		);
		System.out.println("  * parameter order is variable");
		System.out.println("  * customer ID must be > 0");
		System.out.println("  * month format is yyyy-MM, range is not checked");
		System.exit(exitCode);
	}
}
