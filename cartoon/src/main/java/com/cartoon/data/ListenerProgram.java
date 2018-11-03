package com.cartoon.data;

import java.util.List;

/**
 * 听书节目信息
 * <p>
 * Created by David on 16/7/19.
 */
public class ListenerProgram {

    /**
     * chatgroup_id : 0
     * description :
     * detail : {}
     * duration : 1180
     * id : 2112799
     * mediainfo : {"bitrates_url":[{"bitrate":24,"file_path":"vod/00/00/0000000000000000000024714128_24.m4a","qetag":"Fm2nx3_1N1ezKbkD-g0hHGF7vfnF"}],"duration":1180,"id":3132312}
     * sequence : 1
     * thumbs : null
     * title : 凶宅笔记第一部_001
     * type : program_ondemand
     * update_time : 2014-12-15 17:18:06
     */

    private int chatgroup_id;
    private String description;
    private DetailBean detail;
    private int duration;
    private String id;
    /**
     * bitrates_url : [{"bitrate":24,"file_path":"vod/00/00/0000000000000000000024714128_24.m4a","qetag":"Fm2nx3_1N1ezKbkD-g0hHGF7vfnF"}]
     * duration : 1180
     * id : 3132312
     */

    private MediainfoBean mediainfo;
    private int sequence;
    private Object thumbs;
    private String title;
    private String type;
    private String update_time;

    public int getChatgroup_id() {
        return chatgroup_id;
    }

    public void setChatgroup_id(int chatgroup_id) {
        this.chatgroup_id = chatgroup_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DetailBean getDetail() {
        return detail;
    }

    public void setDetail(DetailBean detail) {
        this.detail = detail;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MediainfoBean getMediainfo() {
        return mediainfo;
    }

    public void setMediainfo(MediainfoBean mediainfo) {
        this.mediainfo = mediainfo;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public Object getThumbs() {
        return thumbs;
    }

    public void setThumbs(Object thumbs) {
        this.thumbs = thumbs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public static class DetailBean {
    }

    public static class MediainfoBean {
        private int duration;
        private int id;
        /**
         * bitrate : 24
         * file_path : vod/00/00/0000000000000000000024714128_24.m4a
         * qetag : Fm2nx3_1N1ezKbkD-g0hHGF7vfnF
         */

        private List<BitratesUrlBean> bitrates_url;

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<BitratesUrlBean> getBitrates_url() {
            return bitrates_url;
        }

        public void setBitrates_url(List<BitratesUrlBean> bitrates_url) {
            this.bitrates_url = bitrates_url;
        }

        public static class BitratesUrlBean {
            private int bitrate;
            private String file_path;
            private String qetag;

            public int getBitrate() {
                return bitrate;
            }

            public void setBitrate(int bitrate) {
                this.bitrate = bitrate;
            }

            public String getFile_path() {
                return file_path;
            }

            public void setFile_path(String file_path) {
                this.file_path = file_path;
            }

            public String getQetag() {
                return qetag;
            }

            public void setQetag(String qetag) {
                this.qetag = qetag;
            }
        }
    }

    @Override
    public String toString() {
        return "ListenerProgram{" +
                "chatgroup_id=" + chatgroup_id +
                ", description='" + description + '\'' +
                ", detail=" + detail +
                ", duration=" + duration +
                ", id=" + id +
                ", mediainfo=" + mediainfo +
                ", sequence=" + sequence +
                ", thumbs=" + thumbs +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", update_time='" + update_time + '\'' +
                '}';
    }
}
