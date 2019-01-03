package com.seuic.hayao.data.remote;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Hashtable;

public class SmartStoreCodeInfoSerializableList implements KvmSerializable {

    ArrayList<SmartStoreCodeInfo> list = new ArrayList<SmartStoreCodeInfo>();

    @Override
    public int getPropertyCount() {
        return list.size();
    }

    @Override
    public Object getProperty(int i) {
        return list.get(i);
    }


    @Override
    public void setProperty(int i, Object o) {

        if (!(o instanceof SmartStoreCodeInfo)) {
            throw new RuntimeException("not the right type for" + o.getClass().getName());
        }

        list.add(i, (SmartStoreCodeInfo) o);
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {

        propertyInfo.type = SmartStoreCodeInfo.class;
        propertyInfo.name = "SmartStoreCodeInfo";

    }


    public class SmartStoreCodeInfo implements KvmSerializable {

        private boolean ByParent;
        private int ParentLevel;
        private boolean IsParentCode;
        private String CodeId;
        private String SavedCodeId;
        private int SavedCount;
        private int SavedCodeLevel;
        private String ActTime;
        private String Actor;

        @Override
        public int getPropertyCount() {
            return 9;
        }

        @Override
        public Object getProperty(int i) {
            switch (i) {
                case 0:
                    return ByParent;
                case 1:
                    return ParentLevel;
                case 2:
                    return IsParentCode;
                case 3:
                    return CodeId;
                case 4:
                    return SavedCodeId;
                case 5:
                    return SavedCount;
                case 6:
                    return SavedCodeLevel;
                case 7:
                    return ActTime;
                case 8:
                    return Actor;
                default:
                    break;
            }
            return null;
        }

        @Override
        public void setProperty(int i, Object o) {
            if (o == null) {
                return;
            }
            switch (i) {
                case 0:
                    ByParent = (Boolean) o;
                    break;
                case 1:
                    ParentLevel = (int) o;
                    break;
                case 2:
                    IsParentCode = (Boolean) o;
                    break;
                case 3:
                    CodeId = o.toString();
                    break;
                case 4:
                    SavedCodeId = o.toString();
                    break;
                case 5:
                    SavedCount = (int) o;
                    break;
                case 6:
                    SavedCodeLevel = (int) o;
                    break;
                case 7:
                    ActTime = o.toString();
                    break;
                case 8:
                    Actor = o.toString();
                    break;
                default:
                    break;
            }

        }

        @Override
        public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
            switch (i) {
                case 0:
                    propertyInfo.type = Boolean.class;
                    propertyInfo.name = "ByParent";
                    break;
                case 1:
                    propertyInfo.type = Integer.class;
                    propertyInfo.name = "ParentLevel";
                    break;
                case 2:
                    propertyInfo.type = Boolean.class;
                    propertyInfo.name = "IsParentCode";
                    break;
                case 3:
                    propertyInfo.type = String.class;
                    propertyInfo.name = "CodeId";
                    break;
                case 4:
                    propertyInfo.type = String.class;
                    propertyInfo.name = "SavedCodeId";
                    break;
                case 5:
                    propertyInfo.type = Integer.class;
                    propertyInfo.name = "SavedCount";
                    break;
                case 6:
                    propertyInfo.type = Integer.class;
                    propertyInfo.name = "SavedCodeLevel";
                    break;
                case 7:
                    propertyInfo.type = String.class;
                    propertyInfo.name = "ActTime";
                    break;
                case 8:
                    propertyInfo.type = String.class;
                    propertyInfo.name = "Actor";
                    break;
                default:
                    break;
            }
        }
    }
}
