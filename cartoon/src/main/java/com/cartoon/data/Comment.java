package com.cartoon.data;

import java.io.Serializable;
import java.util.List;

public class Comment  extends CommentInfo implements Serializable, Cloneable{

    public int replyCount;

    //记录本地列表评论状态是否折叠，默认折叠状态，展开后为false
    //每次数据更新需将该字段以id为基准进行平移
    public boolean isFold = true;

    public List<CommentReply> replies;

    @Override
    public Comment clone()  {
        Comment test=null;
        try {
            test=(Comment)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return test;
    }
}

