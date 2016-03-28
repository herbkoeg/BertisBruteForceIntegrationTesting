# BertisBruteForceIntegrationTesting
BfiTesting verifies results perstised in a database by a black box, e.g.
a Cobol Program called by a special library within a webservice.
 
You can define a TestCase described in a xml-file, which will be loaded and exectuted:
- define insert, update or delete sql commands for initialisation
- assert state BEFORE
- run your magic blackbox ...
- assert state AFTER
- define insert, update or delete sql commands for restoring the intial state
- use $MyVariable and set it as key-value map parameter calling process, assert or
genrate methods

The TestCaseProcessor has a method called 'generateExampleTestCase', which 
you can use for easily create a new TestCase.xml:

Just run HelloWorld.java to generate a first TestCase.

Have fun :-) !


