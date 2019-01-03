package com.seuic.update;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

public class DownloadXMLParser {

	public static UpdateInfo parse(InputStream inputStream) {
	    UpdateInfo update = null;
	    ArrayList<String> description = null;
		XmlPullParser xmlParser = Xml.newPullParser();
		try {
			xmlParser.setInput(inputStream, "UTF-8");
			int evtType = xmlParser.getEventType();
			while (evtType != XmlPullParser.END_DOCUMENT) {
				String tag = xmlParser.getName();
				switch (evtType) {
				case XmlPullParser.START_TAG:
					if (tag.equalsIgnoreCase("update")) {
					    update = new UpdateInfo();
					} else if (update != null) {
						if (tag.equalsIgnoreCase("version")) {
						    update.setVersion(Integer.parseInt(xmlParser.nextText()));
						} else if (tag.equalsIgnoreCase("name")) {
						    update.setName(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("description")) {
						    description = new ArrayList<String>();
						} else if (tag.equalsIgnoreCase("item")) {
						    description.add(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("size")){
						    update.setSize(xmlParser.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG:
				    if (tag.equalsIgnoreCase("description")) {
				        update.setDescription(description);
				    }
					break;
				}
				evtType = xmlParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return update;
	}
}
