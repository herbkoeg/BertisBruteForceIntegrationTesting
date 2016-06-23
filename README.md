# BertisBruteForceIntegrationTesting
- BfiTesting is a very lightweight framework for testing applications using
  a jdbc-driver to create connections to db
- Aim of BfiTesting is to verify results persisted in a database by a black box, for example
   + results of an ancient cobol program called by a special library within a webservice.
   + you run a bpm and want to assert, that all components - which are peristing
     their datas in different databases - work correctly together: just define
     more DBConnectors ... 
   + you can run and assert some actions over different databases with your 
     countinous integration system

You can define a TESTCASE described in a xml-file, which will be loaded and exectuted:
- define insert, update or delete sql commands for INITIALISATION running 
'TestCaseProcessor.processInitAction ...'
- ASSERT BEFORE blackbox action running 'TestCaseProcessor.assertBefore ...'
- RUN your magic BLACKBOX -...
- ASSERT AFTER blackbox action running 'TestCaseProcessor.assertAfter ...'
- define insert, update or delete sql commands for RESTORING the intial state
running 'TestCaseProcessor.processResetAction ...'

You can parameterize your testcase:
- use $MyVariable and set it as key-value map PARAMETER calling process, assert or
generate methods

Some Hints:
- The TestCaseProcessor has a method called 'generateExampleTestCase', which 
you can use for easily CREATE A NEW TESTCASE named MyTestCase.xml
- This version comes with an PostgresDBConnector, but you very easily create
your own DB2, MySql, ... Connector implementing DBConnector.
- Just run HelloWorld.java or even better TestCaseProcessorTest
to generate a first TestCase.




