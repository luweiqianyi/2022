package vizpower.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.Vector;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import vizpower.imeeting.MeetingMgr;
import vizpower.imeeting.iMeetingApp;


/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler
{

	//public String 	m_strServerURL = "http://192.168.1.10:8080/upload_file";
	public String 	m_strServerURL = "";
	public String 	m_strWebURL = "_WebURL_";
	public String	m_strUserID = "_UserID_";

	public static final String TAG = "CrashHandler";

	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// CrashHandler实例
	private static CrashHandler INSTANCE = new CrashHandler();
	// 程序的Context对象
	private Context mContext;
	// 用来存储设备信息和异常信息
	private Map<String, String> mInfos = new TreeMap<String, String>();

	private File mcrashLogFile;
	private String mVersionName;
	private String mVersionCode;

	// 用于格式化日期,作为日志文件名的一部分
	// private DateFormat formatter = new
	// SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context.getApplicationContext();
		// 获取系统默认的UncaughtException处理器
		Thread.UncaughtExceptionHandler DefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		if(DefaultHandler != this)
		{
			mDefaultHandler = DefaultHandler;
			// 设置该CrashHandler为程序的默认处理器
			Thread.setDefaultUncaughtExceptionHandler(this);
		}
	}

	public void dismissCrashHandler() {
		Thread.UncaughtExceptionHandler DefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		if(DefaultHandler == this && mDefaultHandler != null)
		{
			// 将以前保存的句柄设置回去
			Thread.setDefaultUncaughtExceptionHandler(mDefaultHandler);
			mDefaultHandler = null;
		}
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
			// 退出程序
			// android.os.Process.killProcess(android.os.Process.myPid());
			// System.exit(1);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean m_bHandleException = false;
	private boolean handleException(Throwable ex)
	{
		if (m_bHandleException)
		{
			return false;
		}

		if (ex == null) {
			return false;
		}

		m_bHandleException = true;

		// 收集设备参数信息
		collectDeviceInfo(mContext);

		// 保存日志文件
		// Log.i(TAG, "an error occured while writing file...", ex);
		mcrashLogFile = saveCrashInfo2File(ex);

		// 上传崩溃日志
//		if (!m_strServerURL.isEmpty())
//		{
//			asyncUploadFile(mcrashLogFile);
//		}
		
		// 发送崩溃日志文件
		sendLogMailWithCrashInfo(ex);
		
//		new Thread(new Runnable()
//		{
//			public void run() 
//			{
//				CleanOldFile();
//			}
//		});

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			Log.e(TAG, "error : ", e);
		}

		android.os.Process.killProcess(Process.myPid());

		return false;
	}
	 
	    
 	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				mVersionName = pi.versionName == null ? "null"
						: pi.versionName;
				mVersionCode = pi.versionCode + "";
				mInfos.put("versionName", mVersionName);
				mInfos.put("versionCode", mVersionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				Object objValue = field.get(null);
				if (objValue.getClass().isArray())
				{
					Object[] objArray = (Object[])objValue;
					String strResult = new String("Array [ ");
					for (Object objValueOne : objArray)
					{
						strResult += objValueOne.toString();
						strResult += " ; ";
					}
					strResult += "]";

					mInfos.put(field.getName(), strResult);
					Log.d(TAG, field.getName() + " : " + strResult);
				}
				else
				{
					mInfos.put(field.getName(), objValue.toString());
					Log.d(TAG, field.getName() + " : " + objValue);
				}
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}

		int sdk_int = android.os.Build.VERSION.SDK_INT;
		mInfos.put("SDK_INT", String.valueOf(sdk_int));
		Log.d(TAG, "SDK_INT" + " : " + String.valueOf(sdk_int));

		mInfos.put("VP_VersionName", VPUtils.getAppVersionName(ctx).toString());
		Log.d(TAG, "VP_VersionName" + " : " + VPUtils.getAppVersionName(ctx));

		mInfos.put("VP_VersionCode", VPUtils.getAppVersionCode(ctx).toString());
		Log.d(TAG, "VP_VersionCode" + " : " + VPUtils.getAppVersionCode(ctx));

		mInfos.put("VP_isWxbAppEdition", String.valueOf(VPUtils.isWxbAppEdition()));
		Log.d(TAG, "VP_isWxbAppEdition" + " : " + String.valueOf(VPUtils.isWxbAppEdition()));
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private File saveCrashInfo2File(Throwable ex) {

		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : mInfos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			// long timestamp = System.currentTimeMillis();
			// String time = formatter.format(new Date());

			// long ltime;
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd_HH-mm-ss-SSS",Locale.getDefault());
			Calendar calendar = Calendar.getInstance();// 获取当前日历对象
			long unixTime = calendar.getTimeInMillis();// 获取当前时区下日期时间对应的时间戳
			long unixTimeGMT = unixTime - TimeZone.getDefault().getRawOffset();// 获取标准格林尼治时间下日期时间对应的时间戳
			String rawTime;
			rawTime = sdf.format(new Date(unixTimeGMT + 8 * 3600 * 1000));

			String fileName =  "AndroidMeetingCrash_" + mVersionCode + "_" +  rawTime + ".log";
			String strFullFilePath = VPUtils.getVPLocalDir("dump") + fileName;
			FileOutputStream fos = new FileOutputStream(strFullFilePath);
			fos.write(sb.toString().getBytes());
			fos.close();
			return new File(VPUtils.getVPLocalDir("dump"), fileName);
			// return sb.toString();
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}

 
	/**
	 * 清理崩溃日志
	 */
    public void cleanOldFile()
    {
        try
        {
            File file = new File(VPUtils.getVPLocalDir("dump"));
            // 获得当前文件夹下的所有子文件和子文件夹
            File[] fileList = file.listFiles();
            if(fileList == null)
            {
            	return;
            }
 
            int len = fileList.length;
            for (int i = 0; i < len; i++)
            {
            	//String Name = fileList[i].getName();
            	//String ExtName = Name.substring(Name.length()-4, Name.length());
 
            	fileList[i].delete();
             }
 
		}
        catch (SecurityException ex) 
		{
		    ex.printStackTrace();
		}
	}
    
	// 发送日志邮件线程
	class UploadLogMailThread extends Thread {
		
		public static final String MAILSENDER ="wxb_sender2@winupon.com";
		public static final String MAILSMTP ="smtp.winupon.com";
		public static final String MAILDOMAIN ="winupon.com";
		public static final String MAILUSERNAME ="wxb_sender2@winupon.com";
		public static final String MAILPASSWD ="WXB@20170207password";
		public static final String MAILRECVER ="wxb@winupon.com";

		private Throwable m_ex = null;

		public UploadLogMailThread(Throwable ex) {
			m_ex = ex;
		}
		
//		private Handler m_Handler = new Handler() {
//			@Override
//			// 当有消息发送出来的时候就执行Handler的这个方法
//			public void handleMessage(Message msg) {
//				super.handleMessage(msg);
//			}
//		};

		public void run() {
			boolean bSendOK = false;
			Mail m;
			try {
				m = new vizpower.common.Mail(MAILUSERNAME, MAILPASSWD);
			} catch (NoClassDefFoundError e1) {
				Message M;
				Handler handler = iMeetingApp.getInstance().getIMainActivity().getLoginResultHandler();
				M = handler.obtainMessage(0, MeetingMgr.MESSAGE_MAIL_SEND_FAIL, 0);
				handler.sendMessage(M);

				String strMessage = e1.getMessage();
				VPLog.logE("NoClassDefFoundError Could not send email msg=%s", ((strMessage!= null)?strMessage:""));

				if (m_ex != null) {
					android.os.Process.killProcess(Process.myPid());
				}

				return;
			}
			String[] toArr = { MAILRECVER };
			m.set_to(toArr);
			m.set_from(MAILSENDER);
			m.set_host(MAILSMTP);

			StringBuffer sb = new StringBuffer();
			sb.append("课堂信息---\n");
			sb.append("UserID:"+MeetingMgr.myUserID()+"\n");
			sb.append("WebUserID:"+MeetingMgr.myWebUserID()+"\n");
			sb.append("NickName="+MeetingMgr.myNickName()+"\n");
			sb.append("MeetingID="+MeetingMgr.meetingID()+"\n");
			sb.append("Subject="+MeetingMgr.meetingName()+"\n");
			sb.append("所在机构:"+MeetingMgr.projectName()+"\n");
			sb.append("连接服务器IP:"+MeetingMgr.serverDomainOrIP()+"\n");
			
			sb.append("\n");
			sb.append("环境信息---\n");
			for (Map.Entry<String, String> entry : mInfos.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				sb.append(key + "=" + value + "\n");
			}

			String strSubject = MeetingMgr.projectName()+"-" + MeetingMgr.myNickName() + "-"+ mVersionName;
			if (m_ex != null) 
			{
				Writer writer = new StringWriter();
				PrintWriter printWriter = new PrintWriter(writer);
				m_ex.printStackTrace(printWriter);
				Throwable cause = m_ex.getCause();
				while (cause != null) {
					cause.printStackTrace(printWriter);
					cause = cause.getCause();
				}
				printWriter.close();
				String result = writer.toString();
				sb.append(result);

				strSubject += ":AndroidCrash";
			}else
			{		
				strSubject += ":AndroidLog";
			}
			m.set_subject(strSubject);
			m.setBody(sb.toString());
			try {
				Vector<String> filenameVec = new Vector();
				VPLog.getLogFileNames(filenameVec);
				
				if (filenameVec.size()>0) {
					for (Iterator<String> iterator = filenameVec.iterator(); iterator
							.hasNext();) {
						String fileobject = (String) iterator.next();
						m.addAttachment(fileobject);
					}					
				}
				
				if (m.send()) {
					VPLog.logI("Email was sent successfully.");
					bSendOK = true;
				} else {
					VPLog.logE("Email was sent failed.");
				}
			} catch (Exception e) {
				// "There was a problem sending the email.",
				String strMessage = e.toString();
				VPLog.logE("Could not send email msg=%s", ((strMessage!= null)?strMessage:""));
			}

			//m_Handler.sendEmptyMessage(0);
			if (iMeetingApp.getInstance().getIMainActivity() != null && 
				iMeetingApp.getInstance().getIMainActivity().getLoginResultHandler()  != null)
			{
				if(bSendOK)
				{
					Message M;
					Handler handler = iMeetingApp.getInstance().getIMainActivity().getLoginResultHandler();
					M = handler.obtainMessage(0, MeetingMgr.MESSAGE_MAIL_SEND_OK, 0);
					handler.sendMessage(M);
				}
				else
				{
					Message M;
					Handler handler = iMeetingApp.getInstance().getIMainActivity().getLoginResultHandler();
					M = handler.obtainMessage(0, MeetingMgr.MESSAGE_MAIL_SEND_FAIL, 0);
					handler.sendMessage(M);
				}
			}

			if (m_ex != null) {
				android.os.Process.killProcess(Process.myPid());
			}

		}
	}

	public void sendLogMailWithCrashInfo(Throwable ex) {
		//先搜集环境信息
		collectDeviceInfo(mContext);
		
		UploadLogMailThread LThread = new UploadLogMailThread(ex);
		LThread.start();
	}
}
