package com.example.administrator.mymusicapp.bean;

import java.util.List;

/**
 * Created by Jugeny on 2017/6/11.
 */

public class NewPlayListResponse {

    /**
     * results : [{"fileUrl":{"name":"黄磊 - 边走边唱.mp3","url":"http://ac-kCFRDdr9.clouddn.com/VMHagjvDcgXkYQpjlte4AfjNIkCXh8Mksd4q0t5s.mp3","objectId":"593839f3570c35005b7f791d","__type":"File","provider":"qiniu"},"albumId":35,"displayName":"黄磊 - 边走边唱.mp3","objectId":"593839fd570c35005b7f794c","duration":313208,"artist":"黄磊","size":5013407,"title":"边走边唱","id":46471,"album":"边走边唱"},{"fileUrl":{"name":"王菲 - 暗涌.mp3","url":"http://ac-kCFRDdr9.clouddn.com/nMREBy9W3xumEUbT5u0azlOj4GiMaJ0a1kt9Ti5G.mp3","objectId":"593839fd2f301e006b30780f","__type":"File","provider":"qiniu"},"albumId":47,"displayName":"王菲 - 暗涌.mp3","objectId":"59383a07570c35005b7f7972","duration":261355,"artist":"王菲","size":4183730,"title":"暗涌","id":46484,"album":"玩具"}]
     * className : Song
     */

    private String className;
    private List<NewPlayListResultsBean> results;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<NewPlayListResultsBean> getResults() {
        return results;
    }

    public void setResults(List<NewPlayListResultsBean> results) {
        this.results = results;
    }

}
