# BertisBruteForceIntegrationTesting
BfiTesting verifies results perstised in a database by a black box. 
You can define a TestCase described in a xml-file, which will be loaded and exectuted:
- you can define insert, update or delete sql commands for initialisation
before assert
- you can define select commands with expected results for assert
- you can define insert, update or delete sql commands for reset after
 assert

The TestCaseProcessor has a method called 'generateExampleTestCase' , which 
you can use for easily create a MyTestCase.xml:

<?xml version="1.0" encoding="ISO-8859-1" standalone="yes"?>
<testCase>
    <description>This is a new Testcase created with TestCaseProcessor</description>
    <initAction>
        <description>define your init commands here</description>
        <rollBackOnError>false</rollBackOnError>
        <sqlCommands>insert into xyz ...</sqlCommands>
        <sqlCommands>update xyz where ...</sqlCommands>
        <sqlCommands>delete from xyz where ...</sqlCommands>
    </initAction>
    <referenceAction>
        <description>this is a new referenceAction</description>
        <selectAction>
            <description>describe me 2!</description>
            <result>1;</result>
            <result>2;</result>
            <select>select id from Person</select>
        </selectAction>
        <selectAction>
            <description>describe me 2!</description>
            <result>Mueller;</result>
            <result>Neuer;</result>
            <select>select name from Person</select>
        </selectAction>
    </referenceAction>
    <resetAction>
        <description>define your reset commands here</description>
        <rollBackOnError>true</rollBackOnError>
        <sqlCommands>delete from xyz where ...</sqlCommands>
        <sqlCommands>update xyz where ...</sqlCommands>
    </resetAction>
</testCase>

Have fun :-) !


