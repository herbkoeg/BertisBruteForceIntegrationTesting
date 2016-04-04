# BertisBruteForceIntegrationTesting
BfiTesting verifies results persisted in a database by a black box, e.g.
results of an ancieant cobol program called by a special library within a webservice.
 
You can define a TESTCASE described in a xml-file, which will be loaded and exectuted:
- define insert, update or delete sql commands for INITIALISATION
- ASSERT BEFORE blackbox action
- RUN your magic BLACKBOX ...
- ASSERT AFTER blackbox action
- define insert, update or delete sql commands for RESTORING the intial state
- use $MyVariable and set it as key-value map PARAMETER calling process, assert or
generate methods

The TestCaseProcessor has a method called 'generateExampleTestCase', which 
you can use for easily CREATE A NEW TESTCASE named MyTestCase.xml:

Just run HelloWorld.java to generate a first TestCase.

Have fun :-) !


