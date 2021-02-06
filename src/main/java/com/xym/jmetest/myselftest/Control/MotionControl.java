package com.xym.jmetest.myselftest.Control;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

import java.util.Observer;


public class MotionControl extends AbstractControl {

    private Spatial spatial;


    //设定运动速度
    private float walkSpeed = 1f;
    private float speedFactor = 1f;

    //设定运动方向向量
    private Vector3f walkDir;
    //物体运动的中间结果
    private Vector3f step;

    //当前位置
    private Vector3f loc;
    //目标位置
    private Vector3f target;

    //观察者
    private Observer observer;

    public MotionControl() {
        this(1f);
    }

    public MotionControl(float walkSpeed) {
        this.walkSpeed = walkSpeed;
        this.walkDir = null;
        this.loc=new Vector3f();
        this.step = new Vector3f();
        this.target = null;
    }

    /**
     * 设定运动速度
     * @param walkSpeed
     */
    public void setWalkSpeed(float walkSpeed) {
        this.walkSpeed = walkSpeed;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
        loc =new Vector3f(spatial.getLocalTranslation());
    }

    /**
     * 设定观察者
     * @param observer
     */
    public void setObserver(Observer observer) {
        this.observer = observer;
    }



    /**
     * 设定目标位置
     * @param target
     */
    public void setTarget(Vector3f target) {
        this.target = target;

        if (target==null){
            walkDir=null;
            return;
        }

        //设置spatial面朝目标点
        spatial.lookAt(target, Vector3f.UNIT_Y);

        //计算运动方向
        walkDir =target.subtract(loc);
        walkDir.normalizeLocal();
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (walkDir!=null){
            float stepDost =walkSpeed*tpf*speedFactor;
            if (stepDost==0){
                return;
            }

            //计算离目标点的距离
            float dist=loc.distance(target);
            if (stepDost <dist){
                //计算位移
                walkDir.mult(stepDost,step);
                loc.addLocal(step);
                spatial.setLocalTranslation(loc);
            }else {
                //到达终点
                walkDir=null;
                spatial.setLocalTranslation(target);
                target=null;
//                if (observer!=null){
//                    observer.onReachTarget();
//                }
            }
        }


    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
