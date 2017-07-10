package com.cyy.canvasview;

import android.graphics.Path;

import java.util.LinkedList;

/**
 * Created by chenyuanyang on 2017/6/24.
 *
 * 历史的笔迹
 */

public class HistoryPath {

    //历史笔迹
    private LinkedList<Path> historyPaths;

    //返回上一步Path
    public Path getPrePath(){

        //将最后一步移除
        historyPaths.pop();

        Path newPath = new Path();
        for (Path path : historyPaths) {
            newPath.addPath(path);
        }
        return newPath;
    }

    /**
     * 添加到历史记录当中
     * @param path //
     */
    public void addPath(Path path){
        if (historyPaths==null){
            historyPaths = new LinkedList<>();
        }
        historyPaths.push(path);
    }

    public boolean isEmpty(){
        return historyPaths.isEmpty();
    }

}
