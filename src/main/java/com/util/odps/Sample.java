package com.util.odps;

import java.io.IOException;
import java.util.Date;

import com.aliyun.odps.Column;
import com.aliyun.odps.Odps;
import com.aliyun.odps.PartitionSpec;
import com.aliyun.odps.TableSchema;
import com.aliyun.odps.account.Account;
import com.aliyun.odps.account.AliyunAccount;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.RecordReader;
import com.aliyun.odps.tunnel.TableTunnel;
import com.aliyun.odps.tunnel.TableTunnel.DownloadSession;
import com.aliyun.odps.tunnel.TunnelException;


public class Sample {

	private static String accessID = "LTAIFrUDfktMojoH";
	private static String accessKey = "yhSRmoHWZRxbCxr1oQ17jVpJjoS5Ul";
//	private static String accessID = "LTAI4JzvNwzg1XNx";
//    private static String accessKey = "Jm2ew84AemnWvoE8KpkKSDTMxltxZp";
	private static String odpsURL = "http://service-corp.odps.aliyun-inc.com/api";
	private static String tunnelURL = "http://dt-corp.odps.aliyun-inc.com";
	
	private static String project = "ytad";
	private static String partition = "ad_type=yvalf,dt=20161205";
	private static String table = "ods_atm_show_invideo_d";

	public static void main(String args[]) {
		Account account = new AliyunAccount(accessID, accessKey);
		Odps odps = new Odps(account);
		odps.setEndpoint(odpsURL);
		odps.setDefaultProject(project);

		TableTunnel tunnel = new TableTunnel(odps);
		tunnel.setEndpoint(tunnelURL);
		 PartitionSpec partitionSpec = new PartitionSpec(partition);
		 
		try {
			DownloadSession downloadSession = tunnel.createDownloadSession(project, table,partitionSpec);
			long count = downloadSession.getRecordCount();
			RecordReader recordReader = downloadSession.openRecordReader(0, count);
			Record record;

			// UploadSession uploadSession = tunnel.createUploadSession(project,
			// table2);
			// RecordWriter recordWriter = uploadSession.openRecordWriter(0);

			while ((record = recordReader.read()) != null) {
				// recordWriter.write(record);
				consumeRecord(record, downloadSession.getSchema());
			}

			recordReader.close();

			// recordWriter.close();
			// uploadSession.commit(new Long[] { 0L });
		} catch (TunnelException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private static void consumeRecord(Record record, TableSchema schema) {
		for (int i = 0; i < schema.getColumns().size(); i++) {
			Column column = schema.getColumn(i);
			String colValue = null;
			switch (column.getType()) {
				case BIGINT: {
					Long v = record.getBigint(i);
					colValue = v == null ? null : v.toString();
					break;
				}
				case BOOLEAN: {
					Boolean v = record.getBoolean(i);
					colValue = v == null ? null : v.toString();
					break;
				}
				case DATETIME: {
					Date v = record.getDatetime(i);
					colValue = v == null ? null : v.toString();
					break;
				}
				case DOUBLE: {
					Double v = record.getDouble(i);
					colValue = v == null ? null : v.toString();
					break;
				}
				case STRING: {
					String v = record.getString(i);
					colValue = v == null ? null : v.toString();
					break;
				}
				default:
					throw new RuntimeException("Unknown column type: " + column.getType());
			}
			System.out.print(colValue == null ? "null" : colValue);
			if (i != schema.getColumns().size())
				System.out.print("\t");
		}
		System.out.println();
	}
}