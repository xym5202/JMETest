package com.xym.jmetest.myselftest.Collidable;

import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import org.lwjgl.Sys;

public class printCollisionResults  {

    /**
     * 打印碰撞检测结果
     */
    public  void printCollisionResults(Collidable a,Collidable b){

        CollisionResults results=new CollisionResults();
        a.collideWith(b,results);
        System.out.println("碰撞检测结果:"+results.size());
        if (results.size()>0){
            for (int i=0;i<results.size();i++){
                CollisionResult result= results.getCollision(i);
                float dist=result.getDistance();
                //返回交点坐标
                Vector3f point=result.getContactPoint();
                //返回交点处法线向量
                Vector3f normal=result.getContactNormal();
                Geometry geometry=result.getGeometry();

                System.out.printf("序号：%a, 距离：%.2f, 物体名称：%b, 交点：%b, 交点法线：%b\\n",i,dist,geometry.getName(),point,normal);
            }
            //离b最近的交点
            Vector3f closest=results.getClosestCollision().getContactPoint();
            //离b最远的交点
            Vector3f farthest=results.getFarthestCollision().getContactPoint();
            System.out.printf("\"最近点：%s, 最远点：%s\\n\"", closest, farthest);
        }

    }
}
