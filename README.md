# TradeProcess

Using JDK1.8 and the following command to start the service:
--spring.profile.active=local --spring.config.location=classpath:application.yml

This service use apache camel to monitor files, which include trade info.

Inbound folder is (must modify):
/Users/Leslie/Documents/workspace/tradeprocess/src/main/resources/inbound

Once the service is up, and the input trigger file (/test/resource/inbound/input.csv) is copied to inbound folder. The service will process and generate a report under the outbound file with the given name
/Users/Leslie/Documents/workspace/tradeprocess/src/main/resources/outbound/outputReport.csv

The above inbound & outbound info is setup in application.yml

TradeRoute.java is an endpoint
The main process logic is in TradeProcessor.java 
*Once get the string body of the trigger file
 ->validate trigger file content is empty or not
  ->if no, split the content into records. Assume 1st row of trigger file is header
   ->Split each record, and verify whether it is valid trade info
    ->if so, convert the record info to Trade, and get the Trade List
     ->for each trade id, keep the quantity value when the trade version is highest
      ->aggerate trades
       ->export the csv

Report.java & Trade.java are 2 models
ReportGeneratorService.java is for export the csv
VerificationService.java is to verify the record can be valid trade
TradeProcessApplication.java is the entrance

/test/resource/inbound/input.csv & /test/resource/outbound/output.csv are used to do the whold flow test.