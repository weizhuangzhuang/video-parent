package com.wzz.util;


import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于操作zookeeper的工具类
 */
public class ZKCurator {

    //zk客户端
    private CuratorFramework client = null;
    final static Logger log = LoggerFactory.getLogger(ZKCurator.class);

    public ZKCurator(CuratorFramework client){
        this.client = client;
    }

    //初始化
    public void init(){
        client = client.usingNamespace("admin");

        //判断在admin命名空间下是否有bgm节点 /admin/bgm
        try {
            if(client.checkExists().forPath("/bgm") == null){
                /**
                 * 对于zk来讲，有两种类型的节点
                 * 持久节点：当你创建一个节点的时候，这个节点就永远存在，除非你手动删除
                 * 临时节点：你创建一个节点之后，会话断开，会自动删除，当然也可以手动删除
                 */
                client.create().creatingParentsIfNeeded()
                        //节点类型：持久节点
                        .withMode(CreateMode.PERSISTENT)
                        //acl:匿名权限
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/bgm");
                log.info("zookeeper初始化成功....");
            }
        } catch (Exception e) {
            log.error("zookeeper客户端连接初始化错误....");
            e.printStackTrace();
        }
    }

    /**
     * 增加或者删除bgm, 向zk-server创建子节点，供小程序后端监听
     * @param bgmId
     * @param operObject
     */
    public void  sendBgmOperator(String bgmId , String operObject){
        try {
            client.create().creatingParentsIfNeeded()
                    //节点类型：持久节点
                    .withMode(CreateMode.PERSISTENT)
                    //acl:匿名权限
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath("/bgm/" + bgmId , operObject.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
