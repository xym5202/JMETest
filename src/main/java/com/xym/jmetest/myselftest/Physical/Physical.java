package com.xym.jmetest.myselftest.Physical;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 * 数学方法制作物理引擎
 * 创建一个Physical类，继承并实现 AbstractControl。
 * 这个Physical 类用来为物体增加物理特性，比如速度、加速度等。
 * 在主循环中，Physical对象将会实时计算物体的运动状态，并更新物体的坐标。
 */
public class Physical extends AbstractControl {

    private Vector3f position=new Vector3f(0,0,0);//位置
    private Vector3f velocity=new Vector3f(1,0,0);//运动速度
    private Vector3f gravity =new Vector3f(0,0,0);//重力加速度

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    public Vector3f getGravity() {
        return gravity;
    }

    public void setGravity(Vector3f gravity) {
        this.gravity = gravity;
    }

    @Override
    public void setSpatial(Spatial spatial){
        super.setSpatial(spatial);
        position.set(spatial.getLocalTranslation());
    }

    @Override
    protected void controlUpdate(float v) {
        velocity.addLocal(gravity.mult(v));
        position.addLocal(velocity.mult(v));
        spatial.setLocalTranslation(position);
    }

    @Override
    protected void controlRender(RenderManager renderManager, ViewPort viewPort) {

    }
}
