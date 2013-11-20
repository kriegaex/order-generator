import org.junit.Rule
import org.junit.contrib.java.lang.system.ExpectedSystemExit
import org.junit.contrib.java.lang.system.internal.CheckExitCalled
import spock.lang.Specification
import spock.lang.Unroll

class OrderGeneratorTest extends Specification {
	OrderAggregator bgControls = Mock()
	OrderGenerator orderGenerator;

	@Rule
	ExpectedSystemExit exitRule = ExpectedSystemExit.none();

	final static def CURRENT_MONTH = OrderGenerator.CURRENT_MONTH
	final static def ALL_CUSTOMERS = Constants.ALL_CUSTOMERS
	final static def CUSTOMER_FLAG = Constants.CUSTOMER_FLAG
	final static def MONTH_FLAG = Constants.MONTH_FLAG
	final static def FORECAST_FLAG = Constants.FORECAST_FLAG

	@Unroll
	def "Calculation #args"(String[] args, long customer, String month) {
		setup:
		orderGenerator = new OrderGenerator(args, bgControls)
		when:
		orderGenerator.processArgs()
		then:
		1 * bgControls.calculation(customer, month)
		0 * bgControls.forecast(_)
		where:
		args                                               | customer      | month
		[]                                                 | ALL_CUSTOMERS | CURRENT_MONTH
		[CUSTOMER_FLAG, "-1111"]                               | 1             | CURRENT_MONTH //#####
		[CUSTOMER_FLAG, "1"]                               | 1             | CURRENT_MONTH
		[CUSTOMER_FLAG, "1", MONTH_FLAG, "2013-05"]        | 1             | "2013-05"
		[MONTH_FLAG, "2013-05", CUSTOMER_FLAG, "1"]        | 1             | "2013-05"
		[MONTH_FLAG, "2013-05"]                            | ALL_CUSTOMERS | "2013-05"
		[""]                                               | ALL_CUSTOMERS | CURRENT_MONTH
		["foo"]                                            | ALL_CUSTOMERS | CURRENT_MONTH
		[CUSTOMER_FLAG, "1", "foo"]                        | 1             | CURRENT_MONTH
		[CUSTOMER_FLAG, "1", "foo", MONTH_FLAG, "2013-05"] | 1             | "2013-05"
		[MONTH_FLAG, "2013-05", "foo", CUSTOMER_FLAG, "1"] | 1             | "2013-05"
		["foo", MONTH_FLAG, "2013-05", "bar"]              | ALL_CUSTOMERS | "2013-05"
		["foo", "bar", "zot", "baz", "bla", "moo"]         | ALL_CUSTOMERS | CURRENT_MONTH

	}

	@Unroll
	def "Forecast #args"(String[] args, long customer, String month) {
		setup:
		orderGenerator = new OrderGenerator(args, bgControls)
		when:
		orderGenerator.processArgs()
		then:
		1 * bgControls.forecast(customer, month)
		0 * bgControls.calculation(_)
		where:
		args                                                              | customer      | month
		[FORECAST_FLAG]                                                   | ALL_CUSTOMERS | CURRENT_MONTH
		[CUSTOMER_FLAG, "1", FORECAST_FLAG]                               | 1             | CURRENT_MONTH
		[MONTH_FLAG, "2013-05", CUSTOMER_FLAG, "1", FORECAST_FLAG]        | 1             | "2013-05"
		[MONTH_FLAG, "2013-05", FORECAST_FLAG]                            | ALL_CUSTOMERS | "2013-05"
		["foo", FORECAST_FLAG, "bar"]                                     | ALL_CUSTOMERS | CURRENT_MONTH
		[CUSTOMER_FLAG, "1", "foo", FORECAST_FLAG]                        | 1             | CURRENT_MONTH
		[MONTH_FLAG, "2013-05", "foo", CUSTOMER_FLAG, "1", FORECAST_FLAG] | 1             | "2013-05"
		["foo", MONTH_FLAG, "2013-05", "bar", FORECAST_FLAG, "zot"]       | ALL_CUSTOMERS | "2013-05"
	}

	@Unroll
	def "Usage #args"(String[] args) {
		setup:
		orderGenerator = Spy(OrderGenerator, constructorArgs: [args, bgControls])
		exitRule.expectSystemExitWithStatus(1);
		when:
		orderGenerator.processArgs()
		then:
		0 * bgControls._(_)
		thrown(CheckExitCalled)
		where:
		args << [
			[CUSTOMER_FLAG, "foo"],
			[CUSTOMER_FLAG, "2.5"],
			[CUSTOMER_FLAG, "2,5"],
			[CUSTOMER_FLAG, "-11"],
			[CUSTOMER_FLAG, "foo", MONTH_FLAG, "05/2013"],
			[MONTH_FLAG, "05-2013", CUSTOMER_FLAG, "foo"],
			[MONTH_FLAG, "2013/05", CUSTOMER_FLAG, "foo", FORECAST_FLAG],
			[MONTH_FLAG, "2013/05", CUSTOMER_FLAG, "-1", FORECAST_FLAG],
			[MONTH_FLAG, "my_month"],
			[MONTH_FLAG, "May"],
			[MONTH_FLAG, "5"],
			[MONTH_FLAG, "05"],
			[MONTH_FLAG, "05/2013"],
			[MONTH_FLAG, "05-2013"],
			[MONTH_FLAG, "2013/05"],
			[CUSTOMER_FLAG, "1", MONTH_FLAG, "my_month"],
			[CUSTOMER_FLAG, "1", MONTH_FLAG, "May"],
			[CUSTOMER_FLAG, "1", MONTH_FLAG, "5"],
			[CUSTOMER_FLAG, "1", MONTH_FLAG, "05"],
			[CUSTOMER_FLAG, "1", MONTH_FLAG, "05/2013"],
			[CUSTOMER_FLAG, "1", MONTH_FLAG, "05-2013"],
			[CUSTOMER_FLAG, "1", MONTH_FLAG, "2013/05"],
			[MONTH_FLAG, "my_month", CUSTOMER_FLAG, "1"],
			[MONTH_FLAG, "May", CUSTOMER_FLAG, "1"],
			[MONTH_FLAG, "5", CUSTOMER_FLAG, "1"],
			[MONTH_FLAG, "05", CUSTOMER_FLAG, "1"],
			[MONTH_FLAG, "05/2013", CUSTOMER_FLAG, "1"],
			[MONTH_FLAG, "05-2013", CUSTOMER_FLAG, "1"],
			[MONTH_FLAG, "2013/05", CUSTOMER_FLAG, "1"],
			[MONTH_FLAG, "my_month", CUSTOMER_FLAG, "1", FORECAST_FLAG],
			[MONTH_FLAG, "May", CUSTOMER_FLAG, "1", FORECAST_FLAG],
			[MONTH_FLAG, "5", CUSTOMER_FLAG, "1", FORECAST_FLAG],
			[MONTH_FLAG, "05", CUSTOMER_FLAG, "1", FORECAST_FLAG],
			[MONTH_FLAG, "05/2013", CUSTOMER_FLAG, "1", FORECAST_FLAG],
			[MONTH_FLAG, "05-2013", CUSTOMER_FLAG, "1", FORECAST_FLAG],
			[MONTH_FLAG, "2013/05", CUSTOMER_FLAG, "1", FORECAST_FLAG],
			[MONTH_FLAG, "my_month", FORECAST_FLAG],
			[MONTH_FLAG, "May", FORECAST_FLAG],
			[MONTH_FLAG, "5", FORECAST_FLAG],
			[MONTH_FLAG, "05", FORECAST_FLAG],
			[MONTH_FLAG, "05/2013", FORECAST_FLAG],
			[MONTH_FLAG, "05-2013", FORECAST_FLAG],
			[MONTH_FLAG, "2013/05", FORECAST_FLAG]
		]
	}
}