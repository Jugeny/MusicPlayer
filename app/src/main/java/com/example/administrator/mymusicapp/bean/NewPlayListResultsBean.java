package com.example.administrator.mymusicapp.bean;

import android.os.Parcelable;

import java.util.List;

/**
 * Created by Jugeny on 2017/6/11.
 */

public class NewPlayListResultsBean  {
        /**
         * fileUrl : {"name":"张学友 - 讲你知.mp3","url":"http://ac-kCFRDdr9.clouddn.com/hJhoiRmzIBs4Cvo4mJyxiYpyFBoMfntpf4Q5kasO.mp3","objectId":"59398dd38d6d81005855ca04","__type":"File","provider":"qiniu"}
         * albumId : 119
         * displayName : 张学友 - 讲你知.mp3
         * objectId : 59398ddcac502e006b1d4fd9
         * duration : 224261
         * artist : 张学友
         * albumPic : {"name":"下载.jpg","url":"http://ac-kCFRDdr9.clouddn.com/af344b23e5d20df528ab.jpg","objectId":"59425e0bac502e006b78af3f","__type":"File","provider":"qiniu"}
         * size : 3590288
         * title : 讲你知
         * id : 47851
         * album : Life Is Like A Dream
         * lrc : {"name":"讲你知.lrc","url":"http://ac-kCFRDdr9.clouddn.com/e11ffb6bc99dc5c755f6.lrc","objectId":"59494c96128fe1006a5f45ef","__type":"File","provider":"qiniu"}
         */

        private FileUrlBean fileUrl;
        private int albumId;
        private String displayName;
        private String objectId;
        private int duration;
        private String artist;
        private AlbumPicBean albumPic;
        private int size;
        private String title;
        private int id;
        private String album;
        private LrcBean lrc;

        public FileUrlBean getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(FileUrlBean fileUrl) {
            this.fileUrl = fileUrl;
        }

        public int getAlbumId() {
            return albumId;
        }

        public void setAlbumId(int albumId) {
            this.albumId = albumId;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public AlbumPicBean getAlbumPic() {
            return albumPic;
        }

        public void setAlbumPic(AlbumPicBean albumPic) {
            this.albumPic = albumPic;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }

        public LrcBean getLrc() {
            return lrc;
        }

        public void setLrc(LrcBean lrc) {
            this.lrc = lrc;
        }

        public static class FileUrlBean {
            /**
             * name : 张学友 - 讲你知.mp3
             * url : http://ac-kCFRDdr9.clouddn.com/hJhoiRmzIBs4Cvo4mJyxiYpyFBoMfntpf4Q5kasO.mp3
             * objectId : 59398dd38d6d81005855ca04
             * __type : File
             * provider : qiniu
             */

            private String name;
            private String url;
            private String objectId;
            private String __type;
            private String provider;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getObjectId() {
                return objectId;
            }

            public void setObjectId(String objectId) {
                this.objectId = objectId;
            }

            public String get__type() {
                return __type;
            }

            public void set__type(String __type) {
                this.__type = __type;
            }

            public String getProvider() {
                return provider;
            }

            public void setProvider(String provider) {
                this.provider = provider;
            }
        }

        public static class AlbumPicBean {
            /**
             * name : 下载.jpg
             * url : http://ac-kCFRDdr9.clouddn.com/af344b23e5d20df528ab.jpg
             * objectId : 59425e0bac502e006b78af3f
             * __type : File
             * provider : qiniu
             */

            private String name;
            private String url;
            private String objectId;
            private String __type;
            private String provider;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getObjectId() {
                return objectId;
            }

            public void setObjectId(String objectId) {
                this.objectId = objectId;
            }

            public String get__type() {
                return __type;
            }

            public void set__type(String __type) {
                this.__type = __type;
            }

            public String getProvider() {
                return provider;
            }

            public void setProvider(String provider) {
                this.provider = provider;
            }
        }

        public static class LrcBean {
            /**
             * name : 讲你知.lrc
             * url : http://ac-kCFRDdr9.clouddn.com/e11ffb6bc99dc5c755f6.lrc
             * objectId : 59494c96128fe1006a5f45ef
             * __type : File
             * provider : qiniu
             */

            private String name;
            private String url;
            private String objectId;
            private String __type;
            private String provider;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getObjectId() {
                return objectId;
            }

            public void setObjectId(String objectId) {
                this.objectId = objectId;
            }

            public String get__type() {
                return __type;
            }

            public void set__type(String __type) {
                this.__type = __type;
            }

            public String getProvider() {
                return provider;
            }

            public void setProvider(String provider) {
                this.provider = provider;
            }
        }
    }
//        /**
//         * fileUrl : {"name":"吴若希 - 爱我请留言.mp3","url":"http://ac-kCFRDdr9.clouddn.com/EpaoJkSd65rzul2488ZwKITP1Q7mxe5HIQR0kLfs.mp3","objectId":"593c3262ac502e006b37aebd","__type":"File","provider":"qiniu"}
//         * albumId : 221
//         * displayName : 吴若希 - 爱我请留言.mp3
//         * objectId : 593c326a128fe1006add1b6d
//         * duration : 233535
//         * artist : 吴若希
//         * albumPic : {"name":"6011030069315393.jpg","url":"http://ac-kCFRDdr9.clouddn.com/e3e80803c73a099d96a5.jpg","objectId":"593f9e8f61ff4b006caa656f","__type":"File","provider":"qiniu"}
//         * size : 3738638
//         * title : 爱我请留言
//         * id : 48337
//         * album : 爱我请留言
//         */
//        //播放状态  true 播放   false 未播放
//        private boolean playStatus = false;
//    private FileUrlBean fileUrl;
//    private int albumId;
//    private String displayName;
//    private String objectId;
//    private int duration;
//    private String artist;
//    private AlbumPicBean albumPic;
//    private int size;
//    private String title;
//    private int id;
//    private String album;
//
//
//    public boolean isPlayStatus() {
//        return playStatus;
//    }
//
//    public void setPlayStatus(boolean playStatus) {
//        this.playStatus = playStatus;
//    }
//
//    protected NewPlayListResultsBean(Parcel in) {
//        playStatus = in.readByte() != 0;
//        fileUrl = in.readParcelable(FileUrlBean.class.getClassLoader());
//        albumId = in.readInt();
//        displayName = in.readString();
//        objectId = in.readString();
//        duration = in.readInt();
//        artist = in.readString();
//        albumPic = in.readParcelable(AlbumPicBean.class.getClassLoader());
//        size = in.readInt();
//        title = in.readString();
//        id = in.readInt();
//        album = in.readString();
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeByte((byte) (playStatus ? 1 : 0));
//        dest.writeParcelable(fileUrl, flags);
//        dest.writeInt(albumId);
//        dest.writeString(displayName);
//        dest.writeString(objectId);
//        dest.writeInt(duration);
//        dest.writeString(artist);
//        dest.writeParcelable(albumPic, flags);
//        dest.writeInt(size);
//        dest.writeString(title);
//        dest.writeInt(id);
//        dest.writeString(album);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Creator<NewPlayListResultsBean> CREATOR = new Creator<NewPlayListResultsBean>() {
//        @Override
//        public NewPlayListResultsBean createFromParcel(Parcel in) {
//            return new NewPlayListResultsBean(in);
//        }
//
//        @Override
//        public NewPlayListResultsBean[] newArray(int size) {
//            return new NewPlayListResultsBean[size];
//        }
//    };
//
//    public FileUrlBean getFileUrl() {
//        return fileUrl;
//    }
//
//    public void setFileUrl(FileUrlBean fileUrl) {
//        this.fileUrl = fileUrl;
//    }
//
//    public int getAlbumId() {
//        return albumId;
//    }
//
//    public void setAlbumId(int albumId) {
//        this.albumId = albumId;
//    }
//
//    public String getDisplayName() {
//        return displayName;
//    }
//
//    public void setDisplayName(String displayName) {
//        this.displayName = displayName;
//    }
//
//    public String getObjectId() {
//        return objectId;
//    }
//
//    public void setObjectId(String objectId) {
//        this.objectId = objectId;
//    }
//
//    public int getDuration() {
//        return duration;
//    }
//
//    public void setDuration(int duration) {
//        this.duration = duration;
//    }
//
//    public String getArtist() {
//        return artist;
//    }
//
//    public void setArtist(String artist) {
//        this.artist = artist;
//    }
//
//    public AlbumPicBean getAlbumPic() {
//        return albumPic;
//    }
//
//    public void setAlbumPic(AlbumPicBean albumPic) {
//        this.albumPic = albumPic;
//    }
//
//    public int getSize() {
//        return size;
//    }
//
//    public void setSize(int size) {
//        this.size = size;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getAlbum() {
//        return album;
//    }
//
//    public void setAlbum(String album) {
//        this.album = album;
//    }
//
//    public static class FileUrlBean implements Parcelable{
//
//        private String name;
//        private String url;
//        private String objectId;
//        private String __type;
//        private String provider;
//
//        protected FileUrlBean(Parcel in) {
//            name = in.readString();
//            url = in.readString();
//            objectId = in.readString();
//            __type = in.readString();
//            provider = in.readString();
//        }
//
//        public static final Creator<FileUrlBean> CREATOR = new Creator<FileUrlBean>() {
//            @Override
//            public FileUrlBean createFromParcel(Parcel in) {
//                return new FileUrlBean(in);
//            }
//
//            @Override
//            public FileUrlBean[] newArray(int size) {
//                return new FileUrlBean[size];
//            }
//        };
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getUrl() {
//            return url;
//        }
//
//        public void setUrl(String url) {
//            this.url = url;
//        }
//
//        public String getObjectId() {
//            return objectId;
//        }
//
//        public void setObjectId(String objectId) {
//            this.objectId = objectId;
//        }
//
//        public String get__type() {
//            return __type;
//        }
//
//        public void set__type(String __type) {
//            this.__type = __type;
//        }
//
//        public String getProvider() {
//            return provider;
//        }
//
//        public void setProvider(String provider) {
//            this.provider = provider;
//        }
//
//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            dest.writeString(name);
//            dest.writeString(url);
//            dest.writeString(objectId);
//            dest.writeString(__type);
//            dest.writeString(provider);
//        }
//    }
//
//    public static class AlbumPicBean implements Parcelable {
//
//        private String name;
//        private String url;
//        private String objectId;
//        private String __type;
//        private String provider;
//
//        protected AlbumPicBean(Parcel in) {
//            name = in.readString();
//            url = in.readString();
//            objectId = in.readString();
//            __type = in.readString();
//            provider = in.readString();
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            dest.writeString(name);
//            dest.writeString(url);
//            dest.writeString(objectId);
//            dest.writeString(__type);
//            dest.writeString(provider);
//        }
//
//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        public static final Creator<AlbumPicBean> CREATOR = new Creator<AlbumPicBean>() {
//            @Override
//            public AlbumPicBean createFromParcel(Parcel in) {
//                return new AlbumPicBean(in);
//            }
//
//            @Override
//            public AlbumPicBean[] newArray(int size) {
//                return new AlbumPicBean[size];
//            }
//        };
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getUrl() {
//            return url;
//        }
//
//        public void setUrl(String url) {
//            this.url = url;
//        }
//
//        public String getObjectId() {
//            return objectId;
//        }
//
//        public void setObjectId(String objectId) {
//            this.objectId = objectId;
//        }
//
//        public String get__type() {
//            return __type;
//        }
//
//        public void set__type(String __type) {
//            this.__type = __type;
//        }
//
//        public String getProvider() {
//            return provider;
//        }
//
//        public void setProvider(String provider) {
//            this.provider = provider;
//        }
//    }


