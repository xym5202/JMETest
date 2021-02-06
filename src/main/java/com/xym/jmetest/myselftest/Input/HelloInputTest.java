package com.xym.jmetest.myselftest.Input;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class HelloInputTest extends SimpleApplication {

    public final static String FIRE = "fire";
    public final static String SAY = "HAHA";
    public final static String WALK = "WALK";
    public final static String LOAD = "LOAD";
    MouseAxisTrigger mouseAxisTrigger;

    @Override
    public void simpleInitApp() {
//        KeyTrigger keySpace=new KeyTrigger(KeyInput.KEY_SPACE);
//        inputManager.addMapping(FIRE,keySpace);
//          inputManager.addListener(new MyListener(),FIRE,SAY);
        mouseAxisTrigger = new MouseAxisTrigger(MouseInput.AXIS_X, false);
        inputManager.addMapping(WALK, mouseAxisTrigger);
        inputManager.addListener(new MyAxisListener(), WALK);

        inputManager.addMapping(LOAD, new KeyTrigger(KeyInput.KEY_L));
        inputManager.addListener(new MyLoadListener(), LOAD);
        createLight();
    }

    public static void main(String[] args) {
        HelloInputTest helloInputTest = new HelloInputTest();
        helloInputTest.start();
    }

/*    class   MyListener implements ActionListener{

        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name!=null&&FIRE.equals(name)&&isPressed){
                System.out.println(FIRE);
            }
            if (name!=null&&SAY.equals(name)&&isPressed){
                System.out.println(SAY);
            }
        }
    }*/

    class MyLoadListener implements ActionListener {

        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            loadModel();
//            if (name != null && LOAD.equals(name) && isPressed) {
//                // loadModel();
//                final Spatial m = assetManager.loadModel("/Models/people/scene.gltf");
//                m.scale(0.9f);
//                m.center();
//                rootNode.attachChild(m);
//            }
        }

        private void loadModel() {
            new Thread() {
                public void run() {
                    final Spatial m = assetManager.loadModel("/Models/people/scene.gltf");
                    m.scale(0.3f);
                    m.center();
                    enqueue(new Runnable() {
                        @Override
                        public void run() {
                            rootNode.attachChild(m);
                        }
                    });
                }
            }.start();


        }
    }
    class MyAxisListener implements AnalogListener {

        @Override
        public void onAnalog(String name, float value, float tpf) {
            if (WALK.equals(name)) {
                System.out.println(mouseAxisTrigger.getName());
            }
        }
    }
    private void createLight() {
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setDirection(new Vector3f(-1, -2, -3));
        ColorRGBA GAMA = new ColorRGBA();
        directionalLight.setColor(GAMA.mult(0.8f));
        rootNode.addLight(directionalLight);
    }
}
