package com.seuic.hayao.device;

public class Appconfig {
	
//	private static final String SETTING_FILENAME = "setting.xml";
//
//	private static String configFile ;
//
//	private static final String LOG_TAG = "Appconfig";
//
//	private static Appconfig appconfig ;
//
//	private static Context mContext;
//
//	private boolean isplaysound = true;										//Play sound
//	private boolean isviberate = false;										//Viberate
//	private boolean isclear = false;										//Clear text on board
//	private boolean iscontinue = false;										//Decoding on loop
//	private int interval = 1000;											//Looping decode interval(ms)
//	private boolean isbootstart = true;										//App starts on boot
//	private boolean isAppend = false;										//Is barcode append previous barcode in Edittext
//	private boolean isShowType = false;										//Show barcode type
//	private SendMode sendMode = SendMode.BROADCAST;							//Barcode send mode
//	private String bcName = Scanner.ACTION_SEND_BARCODE;					//Broadcast barcode action
//	private String bcKey = Scanner.ACTION_SEND_KEY;							//Broadcast barcode key
//	private String bcStartScan = ScannerActionReceiver.ACTION_START_SCAN; 	//Broadcast start scan
//	private String bcStopScan = ScannerActionReceiver.ACTION_STOP_SCAN; 	//Broadcast stop scan
//	private String prefix = "";												//Prefix
//	private String suffix = "";												//Suffix
//	private EndChar endChar = EndChar.ENTER;								//Suffix char
//	private boolean isUpToStopScan = true;									//Action up to stop scan
//	private boolean isEndCharOnEmu = false;
//	private boolean isAddEnterEvent = true;									//Add enter event after barcode
//
//	public static Appconfig getInstance(Context context){
//		if(appconfig == null){
//			synchronized (Appconfig.class) {
//				if(appconfig == null){
//					appconfig = new Appconfig(context);
//				}
//			}
//		}
//
//		return appconfig;
//	}
//
//	private Appconfig(Context context){
//
//		init(context);
//
//		initSettings();
//	}
//
//	private void init(Context context){
//
//		mContext = context.getApplicationContext();
//
//		configFile = mContext.getFilesDir() + "/" + SETTING_FILENAME;
//	}
//
//	private void initSettings(){
//
//		try {
//
//				XmlPullParserFactory factory;
//
//				XmlPullParser pullParser;
//				try {
//					factory = XmlPullParserFactory.newInstance();
//					pullParser = factory.newPullParser();
//					pullParser.setInput(new FileInputStream(new File(configFile)),
//							null);
//
//					int eventType;
//					String configName = null;
//					while ((eventType = pullParser.next()) != XmlPullParser.END_DOCUMENT) {
//						switch(eventType){
//						case XmlPullParser.START_TAG:
//							if(pullParser.getAttributeCount() != 0){
//								configName = pullParser.getAttributeValue(0);
//								continue;
//							}
//							if(!TextUtils.isEmpty(configName)){
//								if("PlaySound".equals(configName)){
//									isplaysound = Integer.parseInt(pullParser.nextText()) != 0;
//								}else if("AutoClear".equals(configName)){
//									isclear = Integer.parseInt(pullParser.nextText()) != 0;
//								}else if("Viberate".equals(configName)){
//									isviberate = Integer.parseInt(pullParser.nextText()) != 0;
//								}else if("ScanContinue".equals(configName)){
//									if("param1".equals(pullParser.getName())){
//										iscontinue = Integer.parseInt(pullParser.nextText()) != 0;
//									}else if("param2".equals(pullParser.getName())){
//										interval = Integer.parseInt(pullParser.nextText());
//									}
//								}else if("Bootstart".equals(configName)){
//									isbootstart = Integer.parseInt(pullParser.nextText()) != 0;
//								}else if("Append".equals(configName)){
//									if("param1".equals(pullParser.getName())){
//										isAppend = Integer.parseInt(pullParser.nextText()) != 0;
//									}else if("param2".equals(pullParser.getName())){
//										suffix = pullParser.nextText();
//									}else if("param3".equals(pullParser.getName())){
//										prefix = pullParser.nextText();
//									}else if("param4".equals(pullParser.getName())){
//										endChar = Enum.valueOf(EndChar.class, pullParser.nextText());
//									}
//								}else if("SendMode".equals(configName)){
//									sendMode = Enum.valueOf(SendMode.class, pullParser.nextText());
//								}else if("BcName".equals(configName)){
//									if("param1".equals(pullParser.getName())){
//										bcName = pullParser.nextText();
//									}else if("param2".equals(pullParser.getName())){
//										bcKey = pullParser.nextText();
//									}else if("param3".equals(pullParser.getName())){
//										bcStartScan = pullParser.nextText();
//									}else if("param4".equals(pullParser.getName())){
//										bcStopScan = pullParser.nextText();
//									}else if("param5".equals(pullParser.getName())){
//										isAddEnterEvent = Boolean.parseBoolean(pullParser.nextText());
//									}
//								}else if("ShowType".equals(configName)){
//									isShowType = Integer.parseInt(pullParser.nextText()) != 0;
//								}else if("UpToStopScan".equals(configName)){
//									isUpToStopScan = Integer.parseInt(pullParser.nextText()) != 0;
//								}else if("IsEndCharOnEmu".equals(configName)){
//									isEndCharOnEmu = Integer.parseInt(pullParser.nextText()) != 0;
//								}
//							}
//							break;
//						case XmlPullParser.END_TAG:
//							if(pullParser.getName().equals(configName)){
//								configName = null;
//							}
//							break;
//						}
//						}
//				} catch (IOException e) {
//					Log.e(LOG_TAG, "getSettings Exception:" + e.getMessage());
//				} catch (XmlPullParserException e) {
//					Log.e(LOG_TAG, "getSettings Exception:" + e.getMessage());
//				}
//
//		} catch (Exception e) {
//		}
//	}
//
//	public void reset(){
//		initSettings();
//	}
//
//	public boolean isShowType() {
//		return isShowType;
//	}
//
//	public void setShowType(boolean isShowType) {
//		this.isShowType = isShowType;
//	}
//
//	public String getBcName() {
//		return bcName;
//	}
//
//	public void setBcName(String bcName) {
//		this.bcName = bcName;
//	}
//
//	public String getBcKey() {
//		return bcKey;
//	}
//
//	public void setBcKey(String bcKey) {
//		this.bcKey = bcKey;
//	}
//
//	public SendMode getSendMode() {
//		return sendMode;
//	}
//
//	public void setSendMode(SendMode sendMode) {
//		this.sendMode = sendMode;
//	}
//
//	public String getSuffix() {
//		return suffix;
//	}
//
//	public void setSuffix(String suffix) {
//		this.suffix = suffix;
//	}
//
//	public String getPrefix(){
//		return prefix;
//	}
//
//	public void setPrefix(String prefix){
//		this.prefix = prefix;
//	}
//
//	public boolean isAppend() {
//		return isAppend;
//	}
//
//	public void setAppend(boolean isAppend) {
//		this.isAppend = isAppend;
//	}
//
//	public EndChar getEndChar(){
//		return endChar;
//	}
//
//	public void setEndChar(EndChar endChar){
//		this.endChar = endChar;
//	}
//
//	public String getBCStartScan(){
//		return this.bcStartScan;
//	}
//
//	public void setBCStartScan(String bcStartScan){
//		this.bcStartScan = bcStartScan;
//	}
//
//	public String getBCStopScan(){
//		return this.bcStopScan;
//	}
//
//	public void setBCStopScan(String bcStopScan){
//		this.bcStopScan = bcStopScan;
//	}
//
//	public boolean isContinue() {
//		return iscontinue;
//	}
//
//	public void setIscontinue(boolean iscontinue) {
//		this.iscontinue = iscontinue;
//	}
//
//	public boolean isPlaysound() {
//		return isplaysound;
//	}
//
//	public void setIsplaysound(boolean isplaysound) {
//		this.isplaysound = isplaysound;
//	}
//
//	public boolean isViberate() {
//		return isviberate;
//	}
//
//	public void setIsviberate(boolean isviberate) {
//		this.isviberate = isviberate;
//	}
//
//	public boolean isClear() {
//		return isclear;
//	}
//
//	public void setIsclear(boolean isclear) {
//		this.isclear = isclear;
//	}
//
//	public boolean isUpToStopScan(){
//		return isUpToStopScan;
//	}
//
//	public void setUpToStopScan(boolean up){
//		this.isUpToStopScan = up;
//	}
//
//	public void save(){
//		saveSettings();
//	}
//
//	public int getInterval() {
//		return interval;
//	}
//
//	public void setInterval(int interval) {
//		this.interval = interval;
//	}
//
//	public boolean isEndCharOnEmu(){
//		return isEndCharOnEmu;
//	}
//
//	public void setEndCharOnEmu(boolean onEmu){
//		this.isEndCharOnEmu = onEmu;
//	}
//
//	public boolean isAddEnterEvent(){
//		return isAddEnterEvent;
//	}
//
//	public void setAddEnterEvent(boolean addEnterEvent){
//		this.isAddEnterEvent = addEnterEvent;
//	}
//
//	private void saveSettings(){
//			XmlSerializer serializer = null;
//
//			FileOutputStream fos = null;
//			try {
//				serializer = Xml.newSerializer();
//
//				File file = new File(configFile);
//
//				file.createNewFile();
//
//				fos = new FileOutputStream(file);
//
//				serializer.setOutput(fos, "UTF-8");
//				serializer.startDocument("UTF-8", false);
//
//				serializer.startTag(null, "configs");
//					// PlaySound Tags
//					serializer.startTag(null, "config");
//					serializer.attribute(null, "name", "PlaySound");
//					serializer.startTag(null, "param1");
//					serializer.text((isplaysound ? 1 : 0) + "");
//					serializer.endTag(null, "param1");
//					serializer.endTag(null, "config");
//
//					// Viberation Tags
//					serializer.startTag(null, "config");
//					serializer.attribute(null, "name", "Viberate");
//					serializer.startTag(null, "param1");
//					serializer.text((isviberate ? 1 : 0) + "");
//					serializer.endTag(null, "param1");
//					serializer.endTag(null, "config");
//
//					// Result Tags
//					serializer.startTag(null, "config");
//					serializer.attribute(null, "name", "AutoClear");
//					serializer.startTag(null, "param1");
//					serializer.text((isclear ? 1 : 0) + "");
//					serializer.endTag(null, "param1");
//					serializer.endTag(null, "config");
//
//					// ScanContinue Tags
//					serializer.startTag(null, "config");
//					serializer.attribute(null, "name", "ScanContinue");
//					serializer.startTag(null, "param1");
//					serializer.text((iscontinue ? 1 : 0) + "");
//					serializer.endTag(null, "param1");
//					serializer.startTag(null, "param2");
//					serializer.text(interval + "");
//					serializer.endTag(null, "param2");
//					serializer.endTag(null, "config");
//
//
//					// BootStart Tags
//					serializer.startTag(null, "config");
//					serializer.attribute(null, "name", "Bootstart");
//					serializer.startTag(null, "param1");
//					serializer.text((isbootstart ? 1 : 0) + "");
//					serializer.endTag(null, "param1");
//					serializer.endTag(null, "config");
//
//
//					// Append Tags
//					serializer.startTag(null, "config");
//					serializer.attribute(null, "name", "Append");
//					serializer.startTag(null, "param1");
//					serializer.text((isAppend ? 1 : 0) + "");
//					serializer.endTag(null, "param1");
//					serializer.startTag(null, "param2");
//					serializer.text(suffix + "");
//					serializer.endTag(null, "param2");
//					serializer.startTag(null, "param3");
//					serializer.text(prefix + "");
//					serializer.endTag(null, "param3");
//					serializer.startTag(null, "param4");
//					serializer.text(endChar + "");
//					serializer.endTag(null, "param4");
//					serializer.endTag(null, "config");
//
//
//					// SendMode Tags
//					serializer.startTag(null, "config");
//					serializer.attribute(null, "name", "SendMode");
//					serializer.startTag(null, "param1");
//					serializer.text(sendMode + "");
//					serializer.endTag(null, "param1");
//					serializer.endTag(null, "config");
//
//					// Broadcast Name Tags
//					serializer.startTag(null, "config");
//					serializer.attribute(null, "name", "BcName");
//					serializer.startTag(null, "param1");
//					serializer.text(bcName);
//					serializer.endTag(null, "param1");
//					serializer.startTag(null, "param2");
//					serializer.text(bcKey);
//					serializer.endTag(null, "param2");
//					serializer.startTag(null, "param3");
//					serializer.text(bcStartScan);
//					serializer.endTag(null, "param3");
//					serializer.startTag(null, "param4");
//					serializer.text(bcStopScan);
//					serializer.endTag(null, "param4");
//					serializer.startTag(null, "param5");
//					serializer.text((isAddEnterEvent ? 1 : 0) + "");
//					serializer.endTag(null, "param5");
//					serializer.endTag(null, "config");
//
//					// Broadcast Name Tags
//					serializer.startTag(null, "config");
//					serializer.attribute(null, "name", "ShowType");
//					serializer.startTag(null, "param1");
//					serializer.text((isShowType ? 1 : 0) + "");
//					serializer.endTag(null, "param1");
//					serializer.endTag(null, "config");
//
//					// Action Up To Stop Scan
//					serializer.startTag(null, "config");
//					serializer.attribute(null, "name", "UpToStopScan");
//					serializer.startTag(null, "param1");
//					serializer.text((isUpToStopScan ? 1 : 0) + "");
//					serializer.endTag(null, "param1");
//					serializer.endTag(null, "config");
//
//					// Action Up To Stop Scan
//					serializer.startTag(null, "config");
//					serializer.attribute(null, "name", "IsEndCharOnEmu");
//					serializer.startTag(null, "param1");
//					serializer.text((isEndCharOnEmu ? 1 : 0) + "");
//					serializer.endTag(null, "param1");
//					serializer.endTag(null, "config");
//
//				serializer.endTag(null, "configs");
//
//				serializer.endDocument();
//
//				serializer.flush();
//
//			} catch (FileNotFoundException e) {
//				Log.i(LOG_TAG, "generateResultFile:" + e.getMessage());
//			} catch (IOException e) {
//				Log.i(LOG_TAG, "generateResultFile:" + e.getMessage());
//			} finally {
//				if (fos != null) {
//					try {
//						fos.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//
//	}
//
//	public boolean isBootstart() {
//		return isbootstart;
//	}
//
//	public void setBootstart(boolean isbootstart) {
//		this.isbootstart = isbootstart;
//	}
//
//	public enum SendMode{
//		FOCUS,
//		BROADCAST,
//		EMUKEY,
//		CLIPBOARD
//	}
//
//	public enum EndChar{
//		ENTER,
//		TAB,
//		SPACE,
//		NONE
//	}
}
