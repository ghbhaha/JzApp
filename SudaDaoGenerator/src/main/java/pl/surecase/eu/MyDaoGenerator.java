package pl.surecase.eu;


import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(10, "com.suda.jzapp.dao.greendao");
        addAccount(schema);
        addRecord(schema);
        addAccountType(schema);
        addRecordType(schema);
        addUser(schema);
        addConfig(schema);
        addRemarkTip(schema);
        addBudget(schema);

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
        account.addStringProperty("ObjectID");
        account.addIntProperty("Index");
        account.addDateProperty("createdAt");
        account.addDateProperty("updatedAt");
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
        record.addStringProperty("ObjectID");
        record.addIntProperty("year");
        record.addIntProperty("month");
        record.addIntProperty("day");
        record.addDateProperty("createdAt");
        record.addDateProperty("updatedAt");
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
        record.implementsSerializable();
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
        record.addStringProperty("ObjectID");
        record.addDateProperty("createdAt");
        record.addDateProperty("updatedAt");
    }


    private static void addUser(Schema schema) {
        Entity user = schema.addEntity("User");
        user.addIdProperty().autoincrement();
        user.addStringProperty("userId");
        user.addStringProperty("userName");
        user.addStringProperty("headImage");
        user.addLongProperty("userCode");
    }

    private static void addConfig(Schema schema) {
        Entity user = schema.addEntity("Config");
        user.addIdProperty().autoincrement();
        user.addStringProperty("key");
        user.addStringProperty("value");
        user.addStringProperty("ObjectID");
    }

    private static void addRemarkTip(Schema schema) {
        Entity remark = schema.addEntity("RemarkTip");
        remark.addIdProperty().autoincrement();
        remark.addIntProperty("useTimes");
        remark.addStringProperty("remark");
        remark.addBooleanProperty("SyncStatus");
        remark.addBooleanProperty("isDel");
    }

    private static void addBudget(Schema schema) {
        Entity budget = schema.addEntity("Budget");
        budget.addIdProperty().autoincrement();
        budget.addDoubleProperty("budgetMoney");
        budget.addDateProperty("createdAt");
        budget.addDateProperty("updatedAt");
    }

}
