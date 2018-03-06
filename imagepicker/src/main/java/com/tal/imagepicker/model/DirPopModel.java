package com.tal.imagepicker.model;

/**
 * Created by cyy on 2016/7/7.
 *
 *
 */
public class DirPopModel {

    private String imagePath;
    private String dirName;
    private String imageCount;

    private String selectCount;//选择的数量

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getImageCount() {
        return imageCount;
    }

    public void setImageCount(String imageCount) {
        this.imageCount = imageCount;
    }

    public String getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(String selectCount) {
        this.selectCount = selectCount;
    }

    public String getImageParentDirPath(){
        if (imagePath != null){
            return imagePath.substring(0 , imagePath.lastIndexOf("/"));
        }
        return "";
    }
}
