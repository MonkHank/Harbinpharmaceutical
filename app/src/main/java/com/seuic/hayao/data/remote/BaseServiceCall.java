package com.seuic.hayao.data.remote;

import android.os.StrictMode;

import com.seuic.hayao.data.DataManager;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import java.util.List;
import java.util.UUID;

public class BaseServiceCall {

    //    protected String WEB_SERVICE_URL = "http://172.16.16.150/TTS/Services/SmartService.asmx?WSDL";
    protected final static String NAMESPACE = "http://tempuri.org/";

    static {

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());
    }

    protected SoapObject webServiceAccess(String methodName, SoapObject soapObject, List<Class<?>> mapClassList) throws Exception {
        String soapAction = NAMESPACE + methodName;
        SoapObject rpc = soapObject;

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = rpc;
        envelope.dotNet = true;
        envelope.headerOut = getHeader();
        envelope.setOutputSoapObject(soapObject);

        if (mapClassList != null) {
            for (Class<?> clazz : mapClassList) {
                envelope.addMapping(NAMESPACE, clazz.getSimpleName(), clazz);
            }
        }

        return webServiceCall(soapAction, envelope, 3);
    }


    /**
     * 访问webService的入口
     **/
    protected SoapObject webServiceAccess(String methodName, SoapObject soapObject) throws Exception {

        String soapAction = NAMESPACE + methodName;
        SoapObject rpc = soapObject;

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = rpc;
        envelope.dotNet = true;
        envelope.headerOut = getHeader();
        envelope.setOutputSoapObject(rpc);

        SoapObject resultObject = webServiceCall(soapAction, envelope, 3);
        return resultObject;
    }

    private SoapObject webServiceCall(String soapAction, SoapSerializationEnvelope envelope, int callTimes) throws Exception {
        HttpTransportSE transportSE = null;
        if (soapAction.contains("GetProfileInfo") || soapAction.contains("GetStoreTypes")) {
            transportSE = new HttpTransportSE(DataManager.getInstance().getAddress() + "/Services/SmartService.asmx?WSDL", 1000 * 20);
        } else {
            transportSE = new HttpTransportSE(DataManager.getInstance().getAddress() + "/Services/SmartService.asmx?WSDL", 1000 * 60 * 15);
        }
        transportSE.debug = true;
        SoapObject resultObject = null;
        try {
            transportSE.call(soapAction, envelope);
            if (envelope.bodyIn instanceof SoapFault) {
                throw (SoapFault) envelope.bodyIn;
            }
            resultObject = (SoapObject) envelope.bodyIn;
            return resultObject;
        } catch (Exception e) {
            if (callTimes > 3) {
                throw e;
            } else {
                callTimes++;
                Thread.sleep(200);
                resultObject = webServiceCall(soapAction, envelope, callTimes);
            }
        }
        return resultObject;
    }


    private String userName;
    private String password;

    public void initHeader(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    private Element[] getHeader() {

        Element[] headerElements = new Element[1];
        headerElements[0] = new Element().createElement(NAMESPACE, "SmartHeader ");

        Element snE = new Element().createElement(NAMESPACE, "SN");//sn 暂时固定
        snE.addChild(Node.TEXT, "123");
//        snE.addChild(Node.TEXT, AndroidInfoGetter.getDeviceSN());
        headerElements[0].addChild(Node.ELEMENT, snE);

        Element sessionIdE = new Element().createElement(NAMESPACE, "SessionId");//SessionId 暂时固定
        sessionIdE.addChild(Node.TEXT, UUID.randomUUID().toString());
        headerElements[0].addChild(Node.ELEMENT, sessionIdE);

        Element userNameE = new Element().createElement(NAMESPACE, "UserName");
        userNameE.addChild(Node.TEXT, userName);
        headerElements[0].addChild(Node.ELEMENT, userNameE);

        Element passwordE = new Element().createElement(NAMESPACE, "Password");
        passwordE.addChild(Node.TEXT, password);
        headerElements[0].addChild(Node.ELEMENT, passwordE);

        return headerElements;
    }
}
