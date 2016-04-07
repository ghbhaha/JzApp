package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(3, "com.suda.jzapp.dao.greendao");
        addAccount(schema);
        addRecord(schema);
        addAccountType(schema);
        addRecordType(schema);
        addUser(schema);
        addConfig(schema);
        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }

    private static void addAccount(Schema schema) {
        Entity account = schema.addEntity("Account");
        account.addIdProperty().primaryKey().autoincrement();
        account.addLongProperty("AccountID");
        account.addIntProperty("AccountTypeID");
        account.addStringProperty("AccountName");
        account.addDoubleProperty("AccountMoney");
        account.addStringProperty("AccountRemark");
        account.addStringProperty("AccountColor");
        account.addBooleanProperty("SyncStatus");
        account.addBooleanProperty("isDel");
    }

    private static void addRecord(Schema schema) {
        Entity record = schema.addEntity("Record");
        record.implementsSerializable();
        record.addIdProperty().primaryKey().autoincrement();
        record.addLongProperty("RecordId");
        record.addDoubleProperty("RecordMoney");
        record.addLongProperty("RecordTypeID");
        record.addIntProperty("RecordType");
        record.addLongProperty("AccountID");
        record.addDateProperty("RecordDate");
        record.addStringProperty("Remark");
        record.addBooleanProperty("SyncStatus");
        record.addBooleanProperty("isDel");
    }

    private static void addAccountType(Schema schema) {
        Entity record = schema.addEntity("AccountType");
        record.addIdProperty();
        record.addIntProperty("AccountTypeID");
        record.addStringProperty("AccountDesc");
        record.addIntProperty("AccountIcon");
    }

    private static void addRecordType(Schema schema) {
        Entity record = schema.addEntity("RecordType");
        record.addIdProperty();
        record.addLongProperty("RecordTypeID");
        record.addStringProperty("RecordDesc");
        record.addIntProperty("RecordType");
        record.addBooleanProperty("SysType");
        record.addIntProperty("RecordIcon");
        record.addIntProperty("Index");
        record.addIntProperty("SexProp");
        record.addIntProperty("Occupation");
        record.addBooleanProperty("SyncStatus");
        record.addBooleanProperty("isDel");
    }

    private static void addUser(Schema schema) {
        Entity user = schema.addEntity("User");
        user.addIdProperty().autoincrement();
        user.addStringProperty("userId");
        user.addStringProperty("userName");
        user.addStringProperty("headImage");
    }

    private static void addConfig(Schema schema) {
        Entity user = schema.addEntity("Config");
        user.addIdProperty().autoincrement();
        user.addStringProperty("key");
        user.addStringProperty("value");
    }


}
