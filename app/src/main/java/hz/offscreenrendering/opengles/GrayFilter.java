/*
 *
 * GrayFilter.java
 * 
 * Created by Wuwang on 2016/12/14
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package hz.offscreenrendering.opengles;

/**
 * Description:
 */
public class GrayFilter extends AFilter {

    public GrayFilter() {
        super();
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile();
    }

}
