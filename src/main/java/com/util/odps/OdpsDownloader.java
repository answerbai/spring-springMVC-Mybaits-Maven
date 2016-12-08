package com.util.odps;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.aliyun.odps.Odps;
import com.aliyun.odps.PartitionSpec;
import com.aliyun.odps.account.Account;
import com.aliyun.odps.account.AliyunAccount;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.RecordReader;
import com.aliyun.odps.tunnel.TableTunnel;
import com.aliyun.odps.tunnel.TableTunnel.DownloadSession;
import com.aliyun.odps.tunnel.TunnelException;
import com.util.PropertiesUtil;

/**
 * 从ODPS上下载数据
 * 
 * @author tangjiang
 *
 */
@Component
public class OdpsDownloader {
    
    private static String accessID = "LTAI4JzvNwzg1XNx";
    private static String accessKey = "Jm2ew84AemnWvoE8KpkKSDTMxltxZp";
    private static String odpsURL = "http://service-corp.odps.aliyun-inc.com/api";//"http://service.odps.aliyun-inc.com/api";
    private static String tunnelURL = "http://dt-corp.odps.aliyun-inc.com";//"http://dt.odps.aliyun.com";
    private static String project = "ytad";
    private static String localOdpsResultDir = PropertiesUtil.I_CENTER.get("localOdpsResultDir");// "D:/odpstest/result/";
    private static String localOdpsOverDir = PropertiesUtil.I_CENTER.get("localOdpsOverDir"); //"D:/odpstest/over/";


    public static void main(String args[]) {
        
        for (int i = 0; i < 3; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    (new OdpsDownloader()).downloadFromOdps("burying_showratehw", "20161126_15", "");
                }
            }).start();
        }
        while(true);
        //readFile();
    }
    
    /**
     * 
     * @param taskName
     * @param date
     * @param hour
     */
    public void downloadFromOdps(String taskName, String date, String param) {

        try {
            // 下载文件
            downloadResultFile(taskName, date, param);
            // 下载文件之后写标记文件
            writeFlagFile(taskName + "_" + date);
        } catch (TunnelException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void downloadResultFile(String taskName, String date, String param)
            throws TunnelException, IOException, FileNotFoundException {
        Account account = new AliyunAccount(accessID, accessKey);
        Odps odps = new Odps(account);
        odps.setEndpoint(odpsURL);
        odps.setDefaultProject(project);
        TableTunnel tunnel = new TableTunnel(odps);
        tunnel.setEndpoint(tunnelURL);
        String dateStr = date, hourStr = "";
        if (date.indexOf("_") != -1) {
            dateStr = date.substring(0, date.indexOf("_"));
            hourStr = date.substring(date.indexOf("_") + 1, date.length());
        }
        String partition = "ad_type=ymf,dt=" + dateStr;// + ",hour=" + hourStr;
//        String odpsTable = OdpsConstants.odps_table.get(taskName);
        String odpsTable ="";
        DownloadSession downloadSession = tunnel.createDownloadSession(project, odpsTable, new PartitionSpec(partition));
        long count = downloadSession.getRecordCount();
        RecordReader recordReader = downloadSession.openRecordReader(0, count);
        Record record;
        FileOutputStream outfile = new FileOutputStream(localOdpsResultDir + taskName + "_" + date + ".txt");
        OutputStreamWriter out = new OutputStreamWriter(outfile);
        String line = "";
        int i = 0;
        while ((record = recordReader.read()) != null) {
            long columnCount = record.getColumnCount();
            for (int j = 0; j < columnCount; j++) {
                line += record.getString(j) + "\t";
            }
            line += "\n";
            out.write(line);
            // TODO 删除条数限制
            if (i++ > 100) {
                break;
            }
        }
        recordReader.close();
        out.close();
    }
    
    private static void writeFlagFile(String fileName) throws IOException {
        FileOutputStream outfile=new FileOutputStream(localOdpsOverDir + fileName);
        OutputStreamWriter out=new OutputStreamWriter(outfile);
        out.write("ok!");
        out.close();
    }
    
    private static void readFile() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        DateFormat dh = new SimpleDateFormat("HH");
        try {
            BufferedReader br1 = new BufferedReader(new FileReader("D:/" + "text3.txt"));
            String line = "";
            long count = 0;
            while ((line = br1.readLine()) != null) {
                String[] fields = line.split("\t");
                if (fields.length < 10) {
                    continue;
                }
                try {
                    long time = Long.parseLong(fields[1]);
                    Date dt = new Date(time);
                    String date = df.format(dt);
                    String hour = dh.format(dt);
                    System.out.println(date+"_"+hour);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
  }