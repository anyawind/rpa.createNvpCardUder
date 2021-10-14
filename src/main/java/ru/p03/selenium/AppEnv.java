package ru.p03.selenium;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import ru.p03.selenium.utils.ApplicationProperties;

public class AppEnv {

    public static ApplicationProperties properties = ApplicationProperties.getInstance();

    public static String FILE_STORAGE_PATH = "";
    public static String IEDRIVER = "IEDRIVER";

    public static String URL_NVP_APP = "URL_NVP_APP";
    public static String USER_NVP_APP = "USER_NVP_APP";
    public static String PASSWORD_NVP_APP = "PASSWORD_NVP_APP";

    public static String sleeptime = "sleeptime";
    public static String sleeptimelong = "sleeptimelong";
    public static String wait = "wait";
    public static String implicitly_wait = "implicitly_wait";
    public static String page_load_timeout = "page_load_timeout";
    public static String set_script_timeout = "set_script_timeout";
    public static String typesleep = "typesleep";

    private final Map environments = new HashMap();

    private static AppEnv CONTEXT;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AppEnv.class);

    private AppEnv(){
        
    }

    public void init(){
        //настройки для робота
        String iedriver = properties.getProperty("app.iedriver");
        String url_nvp_app = properties.getProperty("app.url.nvp");
        String user_nvp_app = properties.getProperty("app.user.nvp");
        String password_nvp_app = properties.getProperty("app.password.nvp");
        String sleeptime_app = properties.getProperty("sleeptime");
        String sleeptimelong_app = properties.getProperty("sleeptimelong");
        String wait_app = properties.getProperty("wait");
        String implicitly_wait_app = properties.getProperty("implicitly_wait");
        String page_load_timeout_app = properties.getProperty("page_load_timeout");
        String set_script_timeout_app = properties.getProperty("set_script_timeout");
        String typesleep_app = properties.getProperty("typesleep");

        //настройки для бд
        String filestoragepath = properties.getProperty("app.filestoragepath");

        environments.put(FILE_STORAGE_PATH, filestoragepath);
        environments.put(IEDRIVER, iedriver);

        environments.put(URL_NVP_APP, url_nvp_app);
        environments.put(USER_NVP_APP, user_nvp_app);
        environments.put(PASSWORD_NVP_APP, password_nvp_app);

        environments.put(sleeptime, sleeptime_app);
        environments.put(sleeptimelong, sleeptimelong_app);
        environments.put(wait, wait_app);
        environments.put(implicitly_wait, implicitly_wait_app);
        environments.put(page_load_timeout, page_load_timeout_app);
        environments.put(set_script_timeout, set_script_timeout_app);
        environments.put(typesleep, typesleep_app);

        environments.put(URL_NVP_APP, url_nvp_app);

   }

    public static AppEnv getContext(){
        if (CONTEXT == null){
            CONTEXT = new AppEnv();
            CONTEXT.init();
        }
        return CONTEXT;
    }


    public String getIEDriver(){
        return (String)environments.get(IEDRIVER);
    }

    //робот
    public String getUserNvpApp(){ return (String)environments.get(USER_NVP_APP);}
    public String getPasswordNvpApp(){ return (String)environments.get(PASSWORD_NVP_APP);}

    public String getSleepTime(){
        return (String)environments.get(sleeptime);
    }
    public String getSleepTimeLong(){
        return (String)environments.get(sleeptimelong);
    }
    public String getWait(){
        return (String)environments.get(wait);
    }
    public String getImplicitly_wait(){ return (String)environments.get(implicitly_wait); }
    public String getPage_load_timeout(){
        return (String)environments.get(page_load_timeout);
    }
    public String getSet_script_timeout(){
        return (String)environments.get(set_script_timeout);
    }
    public String getTypesleep(){
        return (String)environments.get(typesleep);
    }

    public String getUrlNvpApp(){ return (String)environments.get(URL_NVP_APP);}

}
