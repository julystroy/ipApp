package com.cartoon.data;

import java.util.List;

/**
 * Created by cc on 18-1-19.
 */
public class FightQuestion {

    /**
     * code : 0
     * msg : SUCCESS
     * rs : [{"position":1,"option1":"五毒门","id":65,"option2":"八卦门","des":"来源于粉丝【城管】","option3":"六扇门","option4":"七玄门","book":"凡人修仙传","question":"韩立加入的门派叫什么？","type":"周边类"},{"position":2,"option1":"黄元子 ","id":189,"option2":"韩老魔","des":"来源于粉丝【感觉身体被掏空】","option3":"虚灵","option4":"金焰","book":"凡人修仙传","question":"青元子渡劫时，躲在远处不怀好意，想浑水摸鱼的是哪个老怪？","type":"周边类"},{"position":3,"option1":"百锻崖","id":360,"option2":"神手谷","des":"来源于《凡人修仙传》第四章","option3":"落日峰","option4":"炼骨崖","book":"凡人修仙传","question":"韩立加入七玄门的内门弟子考核在哪里举行？","type":"新增"},{"position":4,"option1":"五十几","id":401,"option2":"三十几","des":"来源于《凡人修仙传》第五十三章","option3":"四十几","option4":"六十几","book":"凡人修仙传","question":"墨大夫真实年龄是多少？","type":"新增"},{"position":5,"option1":"贾天龙","id":491,"option2":"李化元","des":"来源于《凡人修仙传》第一百四十四章","option3":"钟灵道","option4":"韩立","book":"凡人修仙传","question":"黄枫谷的掌门是谁？","type":"新增"},{"position":6,"option1":"蜘蛛 ","id":598,"option2":"螳螂","des":"来源于《凡人修仙传》第二百六十九章","option3":"蚂蚁","option4":"飞蛾","book":"凡人修仙传","question":"钟吾死在了什么妖兽手上？","type":"新增"},{"position":7,"option1":"亡猴","id":734,"option2":"鬼啼","des":"来源于《凡人修仙传》第四百三十七章","option3":"啼魂","option4":"鬼猴","book":"凡人修仙传","question":"魔道祭炼出来的介于灵兽和妖魂之间的一种奇特小猴叫什么？","type":"新增"},{"position":8,"option1":"浩然宗","id":993,"option2":"水影宗","des":"来源于《凡人修仙传》第七百四十七章","option3":"古剑门","option4":"鸾鸣宗","book":"凡人修仙传","question":"可以力敌元婴后期修士的双修夫妇龙晗与凤冰是哪个门派的？","type":"新增"},{"position":9,"option1":"凝光镜","id":1010,"option2":"天雷竹","des":"来源于《凡人修仙传》第七百五十七章","option3":"灵烛果","option4":"妖兽材料","book":"凡人修仙传","question":"韩立用的什么换取的庚精？","type":"新增"},{"position":10,"option1":"金焰石","id":1100,"option2":"姜黄晶","des":"来源于《凡人修仙传》第九百四十八章","option3":"寒髓","option4":"万年玄玉","book":"凡人修仙传","question":"只有在千尺之下的沙的中才有可能找到的材料是什么？","type":"新增"},{"position":11,"option1":"冰魄仙子","id":1164,"option2":"寒俪上人","des":"来源于《凡人修仙传》第一千零九十三章","option3":"空玄丹士","option4":"乾老魔","book":"凡人修仙传","question":"谁在飞升前，利用莫大神通冰封冰渊岛上百年？","type":"新增"},{"position":12,"option1":"千宝上人","id":1372,"option2":"天魔","des":"来源于《凡人修仙传》第一千三百四十六章","option3":"空间风暴","option4":"玉骨人魔","book":"凡人修仙传","question":"在韩立等众人逃出洞府附近的神秘空间后,空间里出现了什么？","type":"新增"},{"position":13,"option1":"通天果实","id":1491,"option2":"破阶丹","des":"来源于《凡人修仙传》第一千五百三十一章","option3":"破虚丹","option4":"玄天果实","book":"凡人修仙传","question":"韩立在离开岛上洞府后遇到血云缠身，血云是为了韩立身上的何种东西而缠他的？","type":"新增"},{"position":14,"option1":"十二轮","id":1615,"option2":"八轮","des":"来源于《凡人修仙传》第一千六百五十六章","option3":"十六轮","option4":"九轮","book":"凡人修仙传","question":"合体级以上修士是渡过几轮以上天劫的？","type":"新增"},{"position":15,"option1":"元刹","id":1915,"option2":"花树","des":"来源于《凡人修仙传》第一千九百五十七章","option3":"血光","option4":"六极","book":"凡人修仙传","question":"在韩立逃亡途中谁的分身在圣界追杀雷云子以夺取宝物？","type":"新增"},{"position":16,"option1":"天魔","id":1955,"option2":"心魔","des":"来源于《凡人修仙传》第一千九百八十三章","option3":"天鬼","option4":"黄泉","book":"凡人修仙传","question":"在圣界韩立冲击后期瓶颈遇到了什么？ ","type":"新增"},{"position":17,"option1":"两尺","id":2070,"option2":"三尺","des":"来源于《凡人修仙传》第二千一百零三章","option3":"一尺","option4":"四尺","book":"凡人修仙传","question":"韩立在洗灵池脱胎换骨后身材比原先高了几尺有余？","type":"新增"},{"position":18,"option1":"火焰  ","id":2129,"option2":"水系  ","des":"来源于《凡人修仙传》第二千一百六十一章","option3":"风系  ","option4":"雷电  ","book":"凡人修仙传","question":"蟹道人擅长的神通是什么？  ","type":"新增"},{"position":19,"option1":"冰凤之体","id":2164,"option2":"冰晶之体","des":"来源于《凡人修仙传》第二千一百九十七章","option3":"冰灵之体","option4":"冰髓之体","book":"凡人修仙传","question":"白果儿是什么体质？","type":"新增"}]
     */

    private int code;
    private String       msg;
    private List<CardItem> rs;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<CardItem> getRs() {
        return rs;
    }

    public void setRs(List<CardItem> rs) {
        this.rs = rs;
    }

    @Override
    public String toString() {
        return "FightQuestion{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", rs=" + rs +
                '}';
    }
}
