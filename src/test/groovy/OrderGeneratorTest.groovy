import org.junit.Rule
import org.junit.contrib.java.lang.system.ExpectedSystemExit
import org.junit.contrib.java.lang.system.internal.CheckExitCalled
import spock.lang.Specification
import spock.lang.Unroll

class OrderGeneratorTest extends Specification {
	BgControls bgControls = Mock()
	BestellGenerator orderGenerator;

	@Rule
	ExpectedSystemExit exitRule = ExpectedSystemExit.none();

	final static def FORECAST_MODE = GemeinsameKonstanten.FORECAST_PARAM

	@Unroll
	def "Calculation #args"(String[] args, long customer, String month) {
		setup:
		orderGenerator = new BestellGenerator(args, bgControls)
		when:
		orderGenerator.processArgs()
		then:
		1 * bgControls.calculation(customer, month)
		0 * bgControls.forecast(_)
		where:
		// TODO: define what should happen if exactly one parameter != '-forecast' is given
		// TODO: define format for month parameter, currently anything is allowed
		args                                     | customer       | month
		[]                                       | Util.UNDEFINED | ""
		[""]                                     | Util.UNDEFINED | ""
		["foo"]                                  | Util.UNDEFINED | ""
		["-customer", "1"]                       | 1              | ""
		["-customer", "1", "-month", "my_month"] | 1              | "my_month"
		["-customer", "1", "-month", "May"]      | 1              | "May"
		["-customer", "1", "-month", "5"]        | 1              | "5"
		["-customer", "1", "-month", "05"]       | 1              | "05"
		["-customer", "1", "-month", "05/2013"]  | 1              | "05/2013"
		["-customer", "1", "-month", "05-2013"]  | 1              | "05-2013"
		["-customer", "1", "-month", "2013/05"]  | 1              | "2013/05"
		["-customer", "1", "-month", "2013-05"]  | 1              | "2013-05"
		["-month", "my_month", "-customer", "1"] | 1              | "my_month"
		["-month", "May", "-customer", "1"]      | 1              | "May"
		["-month", "5", "-customer", "1"]        | 1              | "5"
		["-month", "05", "-customer", "1"]       | 1              | "05"
		["-month", "05/2013", "-customer", "1"]  | 1              | "05/2013"
		["-month", "05-2013", "-customer", "1"]  | 1              | "05-2013"
		["-month", "2013/05", "-customer", "1"]  | 1              | "2013/05"
		["-month", "2013-05", "-customer", "1"]  | 1              | "2013-05"
		["-month", "my_month"]                   | Util.UNDEFINED | "my_month"
		["-month", "May"]                        | Util.UNDEFINED | "May"
		["-month", "5"]                          | Util.UNDEFINED | "5"
		["-month", "05"]                         | Util.UNDEFINED | "05"
		["-month", "05/2013"]                    | Util.UNDEFINED | "05/2013"
		["-month", "05-2013"]                    | Util.UNDEFINED | "05-2013"
		["-month", "2013/05"]                    | Util.UNDEFINED | "2013/05"
		["-month", "2013-05"]                    | Util.UNDEFINED | "2013-05"
	}

	@Unroll
	def "Forecast #args"(String[] args, long customer, String month) {
		setup:
		orderGenerator = new BestellGenerator(args, bgControls)
		when:
		orderGenerator.processArgs()
		then:
		1 * bgControls.forecast(customer, month)
		0 * bgControls.calculation(_)
		where:
		// TODO: for 3/5 parameters permit variable parameter order like for 4
		args                                                    | customer       | month
		[FORECAST_MODE]                                         | Util.UNDEFINED | ""
		["-customer", "1", FORECAST_MODE]                       | 1              | ""
		["-month", "my_month", "-customer", "1", FORECAST_MODE] | 1              | "my_month"
		["-month", "May", "-customer", "1", FORECAST_MODE]      | 1              | "May"
		["-month", "5", "-customer", "1", FORECAST_MODE]        | 1              | "5"
		["-month", "05", "-customer", "1", FORECAST_MODE]       | 1              | "05"
		["-month", "05/2013", "-customer", "1", FORECAST_MODE]  | 1              | "05/2013"
		["-month", "05-2013", "-customer", "1", FORECAST_MODE]  | 1              | "05-2013"
		["-month", "2013/05", "-customer", "1", FORECAST_MODE]  | 1              | "2013/05"
		["-month", "2013-05", "-customer", "1", FORECAST_MODE]  | 1              | "2013-05"
		["-month", "my_month", FORECAST_MODE]                   | Util.UNDEFINED | "my_month"
		["-month", "May", FORECAST_MODE]                        | Util.UNDEFINED | "May"
		["-month", "5", FORECAST_MODE]                          | Util.UNDEFINED | "5"
		["-month", "05", FORECAST_MODE]                         | Util.UNDEFINED | "05"
		["-month", "05/2013", FORECAST_MODE]                    | Util.UNDEFINED | "05/2013"
		["-month", "05-2013", FORECAST_MODE]                    | Util.UNDEFINED | "05-2013"
		["-month", "2013/05", FORECAST_MODE]                    | Util.UNDEFINED | "2013/05"
		["-month", "2013-05", FORECAST_MODE]                    | Util.UNDEFINED | "2013-05"
	}

	@Unroll
	def "Usage #args"(String[] args) {
		setup:
		orderGenerator = Spy(BestellGenerator, constructorArgs: [args, bgControls])
		exitRule.expectSystemExitWithStatus(1);
		when:
		orderGenerator.processArgs()
		then:
		0 * bgControls._(_)
		thrown(CheckExitCalled)
		where:
		// TODO: This should be forbidden:
		// ["-month", "2013-05", "-customer", "1", "foo"]
		// ["-month", "2013-05", "foo"]

		args << [
			["-customer", "foo"],
			["-customer", "2.5"],
			["-customer", "2,5"],
			["-customer", "-11"],
			["-customer", "foo", "-month", "05/2013"],
			["-month", "05-2013", "-customer", "foo"],
			["-month", "2013/05", "-customer", "foo", FORECAST_MODE],
			["-month", "2013/05", "-customer", "-1", FORECAST_MODE],
			["foo", "foo", "foo", "foo", "foo", "foo", "foo"]
		]
	}
}