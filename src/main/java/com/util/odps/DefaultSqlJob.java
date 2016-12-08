package com.util.odps;

import java.util.Map;

import com.aliyun.odps.Instance;
import com.aliyun.odps.Odps;
import com.aliyun.odps.OdpsException;
import com.aliyun.odps.account.Account;
import com.aliyun.odps.account.AliyunAccount;
import com.aliyun.odps.task.SQLTask;
import com.aliyun.odps.utils.StringUtils;
import com.youku.addata.odps.entry.ArgsConf;
import com.youku.addata.odps.entry.ClientJobInfo;
import com.youku.addata.odps.exception.JobErrorException;
import com.youku.addata.odps.job.SqlJob;

class DefaultSqlJob extends SqlJob {
	private static String project = "ytad";
	private static String accessID = "LTAIFrUDfktMojoH";
	private static String accessKey = "yhSRmoHWZRxbCxr1oQ17jVpJjoS5Ul";
	private static String odpsURL = "http://service-corp.odps.aliyun-inc.com/api";

	@Override
	public ClientJobInfo initClientJobInfo() {
		/*
		 * 这个id 和key 在编译的时候在填写，不能再git中的源代码上体现。
		 */
		// return new ClientJobInfo("accessId", "accessKey", project);
		return new ClientJobInfo(accessID, accessKey, project);
	}

	@Override
	public String generateSql(ArgsConf argsConf) {
		// argsConf 可以传你sql 需要的动态参数,多个sql用逗号分隔
		String sql = "select log_version,log_time,visitor_ip,ad_cast_id from " + argsConf.get("tableName") + 
				" where dt='" + argsConf.get("dt") + "' and ad_type ='" + argsConf.get("ad_type") + 
				"' and log_version='" + argsConf.get("log_version") + 
//				"' and log_time='" + argsConf.get("log_time") + 
//				"' and visitor_ip='" + argsConf.get("visitor_ip") + 
				"' group by dt,log_version,log_time,visitor_ip,ad_cast_id limit 3;";
		System.out.println(sql);
		return sql;
	}

	public void getResult(ArgsConf argsConf) throws OdpsException {
		ClientJobInfo clientJobInfo = this.initClientJobInfo();
		Account account = new AliyunAccount(clientJobInfo.getAccessId(), clientJobInfo.getAccessKey());
		Odps odps = new Odps(account);
//		odps.setEndpoint(Constants.OdpsEndPoint);//正式环境地址
		odps.setEndpoint(odpsURL);
		odps.setDefaultProject(clientJobInfo.getProject());
		String[] sqls = splitSql(this.generateSql(argsConf));
		for (String sql : sqls) {
			if (StringUtils.isBlank(sql))
				continue;
			Instance instance = SQLTask.run(odps, sql + ";");

			System.out.println("execute sql : " + sql);
			instance.waitForSuccess();
			Map<String, String> map = instance.getTaskResults();
			for (String key : map.keySet()) {
				System.out.println("key=" + key + ";value=" + map.get(key));
			}
			if (!instance.isSuccessful()) {
				throw new JobErrorException("SQLTASK JOB ERR: sql [" + sql + "]");
			}
		}
	}

	public static void main(String[] args) {
		DefaultSqlJob defaultSqlJob = new DefaultSqlJob();
		ArgsConf argsConf = new ArgsConf();
		argsConf.put("tableName", "ods_atm_show_invideo_d");
		argsConf.put("dt", "20161205");
		argsConf.put("ad_type", "yvalf");
		argsConf.put("log_version", "6");
//		argsConf.put("log_time", "1480867199921");
//		argsConf.put("visitor_ip", "61.171.191.208");

		try {
			defaultSqlJob.getResult(argsConf);
		} catch (Exception e) {
			throw new JobErrorException(e.getMessage());
		}
	}
}
