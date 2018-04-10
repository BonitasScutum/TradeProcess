package com.ht.tradeprocess.service;

import com.ht.tradeprocess.entity.Report;

import java.io.*;
import java.util.List;

public class ReportGeneratorService {
    private static final String REPORT_HEADER = "Account,Instrument,Quantity,Trades";

    public static void exportCsv(File outputFile, List<Report> reportList){

        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            if(!outputFile.exists()){
                outputFile.createNewFile();
            }
            fileOutputStream = new FileOutputStream(outputFile);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.append(REPORT_HEADER);
            bufferedWriter.append("\n");
            for(int i = 0;i<reportList.size();i++){
                bufferedWriter.append(reportList.get(i).getAccount()).append(",")
                        .append(reportList.get(i).getInstrument()).append(",")
                        .append(Integer.toString(reportList.get(i).getQuantity())).append(",")
                        .append("\"")
                        .append(reportList.get(i).getTrades())
                        .append("\"").append("\r");

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
                try{
                    if(bufferedWriter!=null) bufferedWriter.close();
                    if(outputStreamWriter!=null) outputStreamWriter.close();
                    if(fileOutputStream!=null) fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

}
