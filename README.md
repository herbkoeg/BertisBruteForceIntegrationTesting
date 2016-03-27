# BertisBruteForceIntegrationTesting
BfiTesting verifies results perstised in a database by a black box, e.g.
a Cobol Program called by a special library within a webservice.
 
You can define a TestCase described in a xml-file, which will be loaded and exectuted:
- you can define insert, update or delete sql commands for initialisation
before assert
- you can define select commands with expected results for assert
- you can define insert, update or delete sql commands for reset after
 assert

The TestCaseProcessor has a method called 'generateExampleTestCase' , which 
you can use for easily create a new TestCase.xml:

Just run HelloWorld.java to generate a first TestCase.

Have fun :-) !


