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

The three central components are:
- TestCaseGenerator: creates testcases, so get a full template filled with the results of your selects
- TestCaseHandler: loads and writes testcase
- TestCaseProcessor: processes actions and asserts

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
- use filters to ignore by regular expressions
- use replacements 

Some Hints:
- This version comes with an PostgresDBConnector, but you very easily create
your own DB2, MySql, ... Connector implementing DBConnector.
- For quitckstart run HelloWorldWithArgs.java to generate a first TestCase and play with the features of bfi.





