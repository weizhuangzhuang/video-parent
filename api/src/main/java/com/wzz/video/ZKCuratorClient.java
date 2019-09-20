package com.wzz.video;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import com.wzz.video.config.ResourceConfig;
import com.wzz.video.pojo.Bgm;
import com.wzz.video.service.BgmService;
import com.wzz.video.utils.BGMOperatorTypeEnum;
import com.wzz.video.utils.JsonUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class ZKCuratorClient {

	// zk客户端
	private CuratorFramework client = null;	
	final static Logger log = LoggerFactory.getLogger(ZKCuratorClient.class);

	@Autowired
	private BgmService bgmService;

	
	@Autowired
	private ResourceConfig resourceConfig;
	
	public void init() {
		
		if (client != null) {
			return;
		}
		
		// 重试策略
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
		// 创建zk客户端
		client = CuratorFrameworkFactory.builder().connectString(resourceConfig.getZookeeperServer())
				.sessionTimeoutMs(10000).retryPolicy(retryPolicy).namespace("admin").build();
		// 启动客户端
		client.start();
		
		try {
//			String testNodeData = new String(client.getData().forPath("/bgm/18052674D26HH3X4"));
//			log.info("测试的节点数据为： {}", testNodeData);
			addChildWatch("/bgm");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	//监听事件
	public void addChildWatch(String nodePath) throws Exception {
		
		final PathChildrenCache cache = new PathChildrenCache(client, nodePath, true);
		cache.start();
		//添加监听器
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) 
					throws Exception {
				
				if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
					log.info("监听到事件 CHILD_ADDED");
					
					//
					/**
					 * [190918HGA799WARP]
					 * [zk: localhost:2181(CONNECTED) 1] get /admin/bgm/190918HGA799WARP
					 * {"path":"\\bgm\\carryon5.mp3","operType":"1"}
					 * 从zookeeper节点取值，path下的data
					 */
					String path = event.getData().getPath();
					log.info("path: {}"  + path);
					String operatorObjStr = new String(event.getData().getData() , "UTF-8");
					log.info("operatorObjStr: {}"  + operatorObjStr);
					Map<String, String> map = JsonUtils.jsonToPojo(operatorObjStr, Map.class);
					String operatorType = map.get("operType");
					//数据库中保存的地址
					String songPath = map.get("path");


					//String arr[] = path.split("/");
					//String bgmId = arr[arr.length - 1];

/*					Bgm bgm = bgmService.queryBgmById(bgmId);
					if (bgm == null) {
						return;
					}*/

					// 1.1 bgm所在的相对路径
					//String songPath = bgm.getPath();

					// 2. 定义保存到本地的bgm路径
					String filePath = resourceConfig.getFileSpace() + songPath;
					//String filePath = resourceConfig.getFileSpace() + songPath;

					// 3. 定义下载的路径（播放url）
					//String arrPath[] = songPath.split("\\\\");           windows下
					String arrPath[] = songPath.split("/");      //linux下
					String finalPath = "";
					// 3.1 处理url的斜杠以及编码
					for(int i = 0; i < arrPath.length ; i ++) {
						if (StringUtils.isNotBlank(arrPath[i])) {
							finalPath += "/";
							finalPath += URLEncoder.encode(arrPath[i], "UTF-8") ;
						}
					}
					String bgmUrl = resourceConfig.getBgmServer() + finalPath;
					//String bgmUrl = resourceConfig.getBgmServer() + finalPath;

					if (operatorType.equals(BGMOperatorTypeEnum.ADD.type)) {
						 //下载bgm到spingboot服务器
					    URL url = new URL(bgmUrl);
						File file = new File(filePath);
						FileUtils.copyURLToFile(url, file);
						client.delete().forPath(path);
					} else if (operatorType.equals(BGMOperatorTypeEnum.DELETE.type)) {
						File file = new File(filePath);
						FileUtils.forceDelete(file);
						client.delete().forPath(path);
					}
				}
			}
		});
	}
	
}
