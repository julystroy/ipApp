package com.cartoon.data.message;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;

/*
 * Created by troy379 on 04.04.17.
 */
@Entity
public class Message
         /*and this one is for custom content type (in this case - voice message)*/ {

    @Id(autoincrement = true)
    private Long   id;
    @Property
    private String user;//用户信息
    @Property
    private String uid;
    @Property
    private String text;
    @Property
    private Date   createdAt;
public Date getCreatedAt() {
         return this.createdAt;
}
public void setCreatedAt(Date createdAt) {
         this.createdAt = createdAt;
}
public String getText() {
         return this.text;
}
public void setText(String text) {
         this.text = text;
}
public String getUid() {
         return this.uid;
}
public void setUid(String uid) {
         this.uid = uid;
}
public String getUser() {
         return this.user;
}
public void setUser(String user) {
         this.user = user;
}
public Long getId() {
         return this.id;
}
public void setId(Long id) {
         this.id = id;
}
@Generated(hash = 1956353858)
public Message(Long id, String user, String uid, String text, Date createdAt) {
         this.id = id;
         this.user = user;
         this.uid = uid;
         this.text = text;
         this.createdAt = createdAt;
}
@Generated(hash = 637306882)
public Message() {
}

}


