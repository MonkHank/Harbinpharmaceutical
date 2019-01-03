package com.seuic.hayao.util;

import android.os.Environment;

import com.seuic.hayao.HYApplication;
import com.seuic.hayao.data.bean.Barcode;
import com.seuic.hayao.data.bean.Bill;
import com.seuic.hayao.data.bean.SmartCorpInfo;
import com.seuic.hayao.data.bean.StoreTypeInfo;
import com.seuic.hayao.data.local.AppCache;
import com.seuic.hayao.enums.ExportType;
import com.seuic.hayao.enums.StoreKind;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class XmlGenerater {

    public static final String DEFUALT_PATH = Environment.getExternalStorageDirectory() + File.separator + "hayao" + File.separator + "export";

    public static boolean generateXMLFile(Bill bill, ArrayList<Barcode> barcodes, StoreTypeInfo info, SmartCorpInfo corpInfo) {

        try {

            File dir = new File(DEFUALT_PATH);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(DEFUALT_PATH + File.separator + info.getStoreSort().name() + "_" + bill.getBillNumber() + ".xml");
            FileOutputStream fileos = new FileOutputStream(file);

            Element document = new Element("Document");
            Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            Namespace xsd = Namespace.getNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
            document.addNamespaceDeclaration(xsi);
            document.addNamespaceDeclaration(xsd);
            document.setAttribute("SN", AndroidInfoGetter.getDeviceSN());
            document.setAttribute("License", "A9手持终端");
            document.setAttribute("Version", AndroidInfoGetter.getVersion(HYApplication.getApplication()));

            Document Doc = new Document(document);

            Element events = new Element("Events");
            document.addContent(events);
            Element event = new Element("Event");

            // 销售出库 采购入库  退货出库  退货入库  只有这四个之一
            if (info.getStoreTypeText().equals(ExportType.SalesWareHouseOut.getDescription())) {
                event.setAttribute("Name", ExportType.SalesWareHouseOut.name());
            } else if (info.getStoreTypeText().equals(ExportType.PurchaseWareHouseIn.getDescription())) {
                event.setAttribute("Name", ExportType.PurchaseWareHouseIn.name());
            }else if (info.getStoreTypeText().equals(ExportType.ReturnWareHouseOut.getDescription())) {
                event.setAttribute("Name", ExportType.ReturnWareHouseOut.name());
            }else if (info.getStoreTypeText().equals(ExportType.ReturnWareHouseIn.getDescription())){
                event.setAttribute("Name", ExportType.ReturnWareHouseIn.name());
            }
            event.setAttribute("MainAction", info.getStoreKind() == StoreKind.In ? "WareHouseIn" : "WareHouseOut");
            events.addContent(event);
            Element dataField = new Element("DataField");
            event.addContent(dataField);
            for (Barcode code : barcodes) {
                Element data = new Element("Data");
                data.setAttribute("Code", code.getBarcode());
                data.setAttribute("Actor", AppCache.getInstance().getLoginInfo().getUserName());
                data.setAttribute("ActDate", code.getActDate());
                data.setAttribute("CorpOrderID", corpInfo.getCorpId() + "");
                data.setAttribute("CorpProductID", "00");
                data.setAttribute("CorpBatchNo", "");
                data.setAttribute("ProduceDate", "");
                dataField.addContent(data);
            }

            Format format = Format.getPrettyFormat();
            XMLOutputter XMLOut = new XMLOutputter(format);
            XMLOut.output(Doc, fileos);
            fileos.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            File dir = new File(DEFUALT_PATH);
//            if (!dir.exists()) {
//                dir.mkdir();
//            }
//            File file = new File(DEFUALT_PATH + File.separator + info.getStoreSort().name() + "_" + bill.getBillNumber() + ".xml");
//            FileOutputStream fileos = new FileOutputStream(file);
//
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//
//            Document doc = builder.newDocument();
//            doc.setXmlStandalone(true);
//            Element document = doc.createElement("Document");
//            document.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
//            document.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
//            document.setAttribute("SN", AndroidInfoGetter.getDeviceSN());
//            document.setAttribute("License", "A9手持终端");
//            document.setAttribute("Version", AndroidInfoGetter.getVersion(HYApplication.getApplication()));
//
//            Element events = doc.createElement("Events");
//
//            Element event = doc.createElement("Event");
//            event.setAttribute("Name", info.getStoreSort().name());
//            event.setAttribute("MainAction", info.getStoreSort().name());
//
//            Element dataField = doc.createElement("DataField");
//
//            for (Barcode code : barcodes) {
//                Element data = doc.createElement("Data");
//                data.setAttribute("Code", code.getBarcode());
//                data.setAttribute("Actor", AppCache.getInstance().getLoginInfo().getUserName());
//                data.setAttribute("ActDate", code.getActDate());
//                data.setAttribute("CorpOrderID", corpInfo.getCorpId() + "");
//                data.setAttribute("CorpProductID", "00");
//                data.setAttribute("CorpBatchNo", "");
//                data.setAttribute("ProduceDate", "");
//                dataField.appendChild(data);
//            }
//            event.appendChild(dataField);
//            events.appendChild(event);
//            document.appendChild(events);
//
//            doc.appendChild(document);
//
//            DOMSource src = new DOMSource(doc);
//            StreamResult sr = new StreamResult(new OutputStreamWriter(fileos, "utf-8"));
//            TransformerFactory tf = TransformerFactory.newInstance();
//            Transformer t = tf.newTransformer();
//            t.setOutputProperty(OutputKeys.INDENT, "yes");
//            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//            t.setOutputProperty(OutputKeys.METHOD, "xml");
//            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//            t.transform(src, sr);
//            fileos.close();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        try {
//            File dir = new File(DEFUALT_PATH);
//            if (!dir.exists()) {
//                dir.mkdir();
//            }
//            File file = new File(DEFUALT_PATH + File.separator + info.getStoreSort().name() + "_" + bill.getBillNumber() + ".xml");
//            FileOutputStream fileos = new FileOutputStream(file);
//            XmlSerializer serializer = Xml.newSerializer();
//            serializer.setOutput(fileos, "UTF-8");
//            serializer.startDocument("UTF-8", null);
//            changeLine(serializer, enter);
//            serializer.startTag(null, "Document");
//            serializer.attribute(null, "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
//            serializer.attribute(null, "xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
//            serializer.attribute(null, "SN", AndroidInfoGetter.getDeviceSN());
//            serializer.attribute(null, "License", "A9手持终端");
//            serializer.attribute(null, "Version", AndroidInfoGetter.getVersion(HYApplication.getApplication()));
//
//            serializer.startTag(null, "Events");
//            serializer.startTag(null, "Event");
//            serializer.attribute(null, "Name", info.getStoreSort().name());
//            serializer.attribute(null, "MainAction", info.getStoreSort().name());
//
//            serializer.startTag(null, "DataField");
//            for (Barcode code : barcodes) {
//                serializer.startTag(null, "Data");
//                serializer.attribute(null, "Code", code.getBarcode());
//                serializer.attribute(null, "Actor", AppCache.getInstance().getLoginInfo().getUserName());
//                serializer.attribute(null, "ActDate", code.getActDate());
//                serializer.attribute(null, "CorpOrderID", corpInfo.getCorpId() + "");
//                serializer.attribute(null, "CorpProductID", "00");
//                serializer.attribute(null, "CorpBatchNo", "");
//                serializer.attribute(null, "ProduceDate", "");
//                serializer.endTag(null, "Data");
//                changeLine(serializer, enter);
//            }
//            serializer.endTag(null, "DataField");
//
//            serializer.endTag(null, "Event");
//            serializer.endTag(null, "Events");
//            serializer.endTag(null, "Document");
//            serializer.endDocument();
//            serializer.flush();
//            fileos.close();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return false;

    }
}
