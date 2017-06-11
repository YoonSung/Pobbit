package config.init;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServerLauncher {

	private static final Logger log = LoggerFactory.getLogger(WebServerLauncher.class);
	private static final String PORT_NUMBER = "8080";
	private static final String WEBAPP_DIR_LOCATION = "webapp/";

	public static void main(String[] args) throws Exception {
		startServer(new Tomcat(), null);
	}

	public interface DoSomeThingAfterTomcatStart {
		void doSomeThing();
	}

	public static void startServer(Tomcat tomcat, DoSomeThingAfterTomcatStart doSomeThingClass) throws Exception {

		if (tomcat == null)
			throw new IllegalArgumentException("Tomcat cannot be null");

		String webappDirLocation = WEBAPP_DIR_LOCATION;

		String webPort = System.getenv("PORT_NUMBER");
		if(webPort == null || webPort.isEmpty()) {
			webPort = PORT_NUMBER;
		}

		tomcat.setPort(Integer.valueOf(webPort));
		Connector connector = tomcat.getConnector();
		connector.setURIEncoding("UTF-8");

		Context ctx = tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());

		//reconcile WebServerlauncher ClassLoader and Tomcat ClassLoader
		tomcat.getEngine().setParentClassLoader(WebServerLauncher.class.getClassLoader());
		//ctx.setLoader(new WebappLoader(WebServerLauncher.class.getClassLoader()));
		
		File additionWebInfClasses = new File("target/classes");

		WebResourceRoot resources = new StandardRoot(ctx);
		resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
		ctx.setResources(resources);
		log.info("configuring app with basedir: {}", new File("./" + webappDirLocation).getAbsolutePath());

		tomcat.enableNaming();
		tomcat.start();

		if (doSomeThingClass != null) {
			Thread thread = new Thread(() -> {
				doSomeThingClass.doSomeThing();
			});

			thread.start();
			thread.join();
		}

		tomcat.getServer().await();
	}

	public static void stopServer(Tomcat tomcat) {
		System.out.println("stopServer start");
		if (tomcat == null || tomcat.getServer() == null) {
			return;
		}

		System.out.println(tomcat.getServer().getState());
		if (tomcat.getServer().getState() != LifecycleState.DESTROYED) {
			if (tomcat.getServer().getState() != LifecycleState.STOPPED) {
				try {
					tomcat.stop();
				} catch (LifecycleException e) {
					log.error("Stop tomcat error.", e);
				}
			}
			try {
				tomcat.destroy();
			} catch (LifecycleException e) {
				log.error("Destroy tomcat error.", e);
			}
		}
	}
}