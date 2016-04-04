# BertisBruteForceIntegrationTesting
BfiTesting verifies results persisted in a database by a black box, e.g.
results of an ancient cobol program called by a special library within a webservice.
 
You can define a TESTCASE described in a xml-file, which will be loaded and exectuted:
- define insert, update or delete sql commands for INITIALISATION
- ASSERT BEFORE blackbox action
- RUN your magic BLACKBOX ...
- ASSERT AFTER blackbox action
- define insert, update or delete sql commands for RESTORING the intial state
- use $MyVariable and set it as key-value map PARAMETER calling process, assert or
generate methods

Some Hints:
- The TestCaseProcessor has a method called 'generateExampleTestCase', which 
you can use for easily CREATE A NEW TESTCASE named MyTestCase.xml
- This version comes with an PostgresDBConnector, but you very easily create
your own DB2, MySql, ... Connector implementing DBConnector.
- Just run HelloWorld.java or even better TestCaseProcessorTest
to generate a first TestCase.

Don't forget: there are much better testing methods, brude force should only be
used for testing ancient technics which couldn't easily replaced or extended 
with modern technics !


