package com.xym.jmetest.myselftest.UI;

import com.jme3.app.SimpleApplication;
import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.*;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public class MouseGUITest extends SimpleApplication {

    private static  final String printMouse="printMouse";
    protected Vector3f position=new Vector3f();//鼠标位置
    //protected  boolean isPresses=false;

    @Override
    public void simpleInitApp() {
        // 改变摄像机摆动方式，显示鼠标。
        flyCam.setDragToRotate(true);
        //显示鼠标光标是否可见
        inputManager.setCursorVisible(true);
        inputManager.getCursorPosition();
        changeMouseCursor();
        inputManager.addRawInputListener(inputListener);
    }

    /**
     * 光标移动监听器
     */

    private RawInputListener inputListener=new RawInputListener() {
        private float x;
        private float y;
        @Override
        public void beginInput() {

        }

        @Override
        public void endInput() {

        }

        @Override
        public void onJoyAxisEvent(JoyAxisEvent evt) {

        }

        @Override
        public void onJoyButtonEvent(JoyButtonEvent evt) {

        }

        @Override
        public void onMouseMotionEvent(MouseMotionEvent evt) {
            // 获得当前鼠标的坐标
            x = evt.getX();
            y = evt.getY();

            // 处理屏幕边缘
            x = FastMath.clamp(x, 0, cam.getWidth());
            y = FastMath.clamp(y, 0, cam.getHeight());

            position.x = x;
            position.y = y - 32;
        }

        @Override
        public void onMouseButtonEvent(MouseButtonEvent evt) {
            if (evt.isPressed()) {
                // 显示鼠标位置
                System.out.printf("MousePosition:(%.0f, %.0f)\n", position.x, position.y + 32f);
            }
        }

        @Override
        public void onKeyEvent(KeyInputEvent evt) {

        }

        @Override
        public void onTouchEvent(TouchEvent evt) {

        }
    };


    /**
     * 改鼠标图标
     */
    private void changeMouseCursor(){
        JmeCursor cur = (JmeCursor) assetManager.loadAsset("Textures/Cursors/meme.cur");
        inputManager.setMouseCursor(cur);
    }

    public static void main(String[] args) {
        MouseGUITest app=new MouseGUITest();
        app.start();
    }
}
