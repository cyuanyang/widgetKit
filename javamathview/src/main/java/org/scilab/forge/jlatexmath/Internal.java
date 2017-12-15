package org.scilab.forge.jlatexmath;

/**
 * Created by cyy on 17/10/19.
 *
 *
 * 兼容android做的修改
 *
 */

public interface Internal {


    Internal DEFAULT = new Internal() {

        @Override
        public String getPackage() {
            return Internal.class.getPackage().getName();
        }
    };

    String getPackage();

}
