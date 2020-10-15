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

The three central classes are:
- TestCaseGenerator
- TestCaseHandler
- TestCaseProcessor

You can define a TESTCASE described in a xml-file, which will be loaded and exectuted:
- define insert, update or delete sql commands for INITIALISATION running 
'TestCaseProcessor.processInitAction ...'
- ASSERT BEFORE blackbox action running 'TestCaseProcessor.assertBefore ...'
- RUN your magic BLACKBOX -...
- ASSERT AFTER blackbox action running 'TestCaseProcessor.assertAfter ...'
- define insert, update or delete sql commands for RESTORING the intial state
running 'TestCaseProcessor.processResetAction ...'

You can parameterize your testcase:
- use variaables: $MyVariable and set it as key-value map PARAMETER calling process, assert or
generate methods
- use filters to ignore regular expressions
- use replacements to avoid for example encoding problems 

Some Hints:
- There is a TestCaseGenerator, which 
you can use for easily CREATE A NEW TESTCASE with the results of your select commands
- This version comes with an PostgresDBConnector, but you very easily create
your own DB2, MySql, ... Connector implementing DBConnector.
- Just run HelloWorld.java to generate a first TestCase.

MyToDos:
- add execSql to Processor
- add processCheck to testcase : i.e. select count(*) from xyz -> 
    continuous monitoring of a process which doesn't throw a exception




