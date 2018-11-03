package com.medbank.greendao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * 「greenDAO Generator」模块
 **/
public class MedBankDaoGenerator {

    public static void main(String[] args) throws Exception {
        // 正如你所见的，你创建了一个用于添加实体（Entity）的模式（Schema）对象。
        // 两个参数分别代表：数据库版本号与自动生成代码的包路径。
        //Schema schema = new Schema(1, "com.medbanks.greendao");
//      当然，如果你愿意，你也可以分别指定生成的 Bean 与 DAO 类所在的目录，只要如下所示：
        Schema schema = new Schema(2, "com.cartoon.bean");
        schema.setDefaultJavaPackageDao("com.cartoon.greendao");

        // 模式（Schema）同时也拥有两个默认的 flags，分别用来标示 entity 是否是 activie 以及是否使用 keep sections。
        // schema2.enableActiveEntitiesByDefault();
        schema.enableKeepSectionsByDefault();

        // 一旦你拥有了一个 Schema 对象后，你便可以使用它添加实体（Entities）了。
        //addNote(schema);
        addCartoonDownload(schema);
        addListener(schema);

        // 最后我们将使用 DAOGenerator 类的 generateAll() 方法自动生成代码，此处你需要根据自己的情况更改输出目录（既之前创建的 java-gen)。
        // 其实，输出目录的路径可以在 build.gradle 中设置，有兴趣的朋友可以自行搜索，这里就不再详解。
        new DaoGenerator().generateAll(schema, "/Users/jinbangzhu/AndroidStudioProjects/Cartoon/cartoon/src/main/java-gen");
    }

    private static void addListener(Schema schema) {
        Entity entity = schema.addEntity("Listener");
        entity.addStringProperty("id").primaryKey().javaDocField("听书ID");
        entity.addStringProperty("approve_num");
        entity.addStringProperty("remote_connect");
        entity.addIntProperty("is_approve");
        entity.addStringProperty("book_id");
        entity.addStringProperty("listener_num");
        entity.addStringProperty("size");
        entity.addStringProperty("title");
        entity.addStringProperty("cover_pic");
        entity.addIntProperty("time_num");
        entity.addIntProperty("local_connect");

        entity.addStringProperty("domain");
        entity.addStringProperty("create_time");
        entity.addStringProperty("comment_num");
        entity.addStringProperty("program_id").javaDocField("点播节目ID");
        entity.addStringProperty("small_pic");
        entity.addStringProperty("collect");
        entity.addStringProperty("data");
        entity.addStringProperty("previous_id");
        entity.addStringProperty("next_id");
        entity.addIntProperty("state").javaDocField("下载状态");
        entity.addIntProperty("progress").javaDocField("下载进度");
        entity.addStringProperty("path").javaDocField("下载路径");
    }

//    private static void addListenerDownLoad(Schema schema) {
//        Entity entity = schema.addEntity("QTProgram");
//        entity.addStringProperty("id").primaryKey().javaDocField("点播节目ID");
//        entity.addIntProperty("state").javaDocField("下载状态");
//        entity.addIntProperty("progress").javaDocField("下载进度");
//        entity.addStringProperty("path").javaDocField("下载路径");
//    }

    /**
     * "id": 1,
     * "sort": 1,
     * "remote_connect": "o8e95vq7x.bkt.clouddn.com/1466150587162_-1.jpg",
     * "cartoon_id": 1,
     * "content_pic": "/upload/image/-1/1466150587162_-1.jpg"
     *
     * @param schema
     */

    private static void addCartoonDownload(Schema schema) {
        Entity entity = schema.addEntity("CartoonDown");
        entity.addStringProperty("id").primaryKey().javaDocField("漫画ID");
        entity.addStringProperty("title").javaDocField("漫画标题");
        entity.addStringProperty("cover_pic").javaDocField("封面");
        entity.addStringProperty("collect").javaDocField("漫画集数");
        entity.addStringProperty("catalog").javaDocField("第几集");
//        entity.addStringProperty("content").javaDocField("内容");

        Entity note = schema.addEntity("CartoonItem");
        note.addStringProperty("id").primaryKey();
        Property property = note.addStringProperty("cartoon_id").getProperty();
        note.addIntProperty("sort");
        note.addStringProperty("remote_connect").javaDocField("图片地址");
        note.addStringProperty("content_pic").javaDocField("本地地址");

        entity.addToMany(note, property, "content");
    }

    /**
     * @param schema
     */
    private static void addNote(Schema schema) {
        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Note」（既类名）
        Entity note = schema.addEntity("Note");
        // 你也可以重新给表命名
        // note.setTableName("NODE");

        // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
        // 接下来你便可以设置表中的字段：
        note.addIdProperty();
        note.addStringProperty("text").notNull();
        // 与在 Java 中使用驼峰命名法不同，默认数据库中的命名是使用大写和下划线来分割单词的。
        // For example, a property called “creationDate” will become a database column “CREATION_DATE”.
        note.addStringProperty("comment");
        note.addDateProperty("date");
    }

    /***
     * 添加患者病历
     **/
    private static void addMedicalRecords(Schema schema) {

        Entity entity = schema.addEntity("MedicalRecords");
        Property property = entity.addStringProperty("p_id").primaryKey().javaDocField("病历ID").getProperty();
        entity.addStringProperty("wx_img").javaDocField("微信头像");
        entity.addStringProperty("wx_pid").javaDocField("患者ID");
        entity.addStringProperty("sort_key");//拼音
        entity.addStringProperty("first_key");//首字母
        entity.addStringProperty("initials_key"); //首字母
        entity.addStringProperty("name").javaDocField("患者姓名");
        entity.addStringProperty("sex").javaDocField("性别");
        entity.addStringProperty("age").javaDocField("年龄");
        entity.addStringProperty("bl").javaDocField("病理");
        entity.addStringProperty("lq").javaDocField("期数");
        entity.addLongProperty("update_time").javaDocField("更新时间");
        entity.addStringProperty("patient_doctor").javaDocField("主治大夫");
        entity.addStringProperty("update_time_str");
        entity.addIntProperty("is_input").javaDocField("是否录入数据");
        entity.addStringProperty("database").javaDocField("当前病例库名称");

        Entity label = schema.addEntity("PatientLabel");
        label.addStringProperty("pid").javaDocField("病历ID");
        label.addStringProperty("label_name").javaDocField("标签");

        entity.addToMany(label, property, "labels");

    }

    /***
     * 添加患者分组
     **/
    private static void addWxPatient(Schema schema) {

        Entity group = schema.addEntity("PatientGroup");
        group.addStringProperty("sub_gid").primaryKey().getProperty();
        group.addStringProperty("user_id").javaDocField("患者分组所属医生userid");
        group.addStringProperty("sort_key");//拼音
        group.addStringProperty("first_key");//首字母
        group.addStringProperty("initials_key"); //首字母
        group.addStringProperty("sub_gname").javaDocField("患者分组名称(未分组不能编辑，客户端请做处理)");
        group.addStringProperty("sub_num").javaDocField("该分组下患者数目");
        group.addBooleanProperty("in_subgroup").javaDocField("用于判断患者是否在该组里 Y为在该组里 N为不在该组里");
        group.addBooleanProperty("is_selected").javaDocField("标识该组是否被选中");

        Entity entity = schema.addEntity("WxPatient");
        entity.addStringProperty("wx_pid").primaryKey().javaDocField("患者微信ID");
        Property property = entity.addStringProperty("sub_gid").javaDocField("分组ID").getProperty();
        entity.addStringProperty("sort_key");//拼音
        entity.addStringProperty("first_key");//首字母
        entity.addStringProperty("initials_key"); //首字母
        entity.addStringProperty("name").javaDocField("患者微信昵称");
        entity.addIntProperty("sex").javaDocField("性别1为男2为女");
        entity.addStringProperty("disease").javaDocField("描述");
        entity.addStringProperty("img").javaDocField("头像");
        entity.addStringProperty("add_time_str").javaDocField("添加时间");
        entity.addStringProperty("state").javaDocField("患者的状态描述");
        entity.addBooleanProperty("qxguanzhu").javaDocField("患者是否已经取消关注  为Y的时候 要在姓名后面 加括号(患者已取消关注");
        entity.addBooleanProperty("haspid").javaDocField("Y是跳转到患者登记的信息的详情和可以匹配的病例列表  N是跳转到患者选择库的页面");

        ToMany customerToOrders = group.addToMany(entity, property);
        customerToOrders.setName("patients");

    }

}
