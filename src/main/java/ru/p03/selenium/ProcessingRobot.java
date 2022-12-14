package ru.p03.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import ru.p03.selenium.utils.ApplicationProperties;
import ru.p03.selenium.utils.People;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static java.util.Objects.isNull;
import static jdk.nashorn.internal.objects.NativeString.trim;

public class ProcessingRobot {
    private WebDriver driver;
    private WebDriverWait wait;
    private static ApplicationProperties properties = ApplicationProperties.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(ProcessingRobot.class);
    private static Integer sleepTime = Integer.parseInt(AppEnv.getContext().getSleepTime());
    private static String file = properties.getProperty("file");
    private static String filecsv = properties.getProperty("filecsv");
    private static String autoit = properties.getProperty("autoit");
    private static String way = properties.getProperty("way");
    private static String wayforfile = properties.getProperty("wayforfile");
    private int count = 1;

    public ProcessingRobot(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(this.driver, Integer.parseInt(AppEnv.getContext().getWait()));
        this.driver.manage().timeouts().implicitlyWait(Integer.parseInt(AppEnv.getContext().getImplicitly_wait()), TimeUnit.MILLISECONDS);
        this.driver.manage().timeouts().pageLoadTimeout(Integer.parseInt(AppEnv.getContext().getPage_load_timeout()), TimeUnit.MILLISECONDS);
        this.driver.manage().timeouts().setScriptTimeout(Integer.parseInt(AppEnv.getContext().getSet_script_timeout()), TimeUnit.MILLISECONDS);
    }

    private String getErrorMessage(Exception e, String methodName) {
        String errorMessage = "";
        try {
            e.printStackTrace();
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                String line = stackTraceElement.toString();
                if (line.contains("ru.p03.selenium")) {
                    errorMessage = "???????????? ?? ???????????? " + methodName + ": " + line + "; ???????????? " + stackTraceElement.getLineNumber() + "; " + e.getLocalizedMessage();
                    logger.error(errorMessage);
                    return errorMessage;
                }
            }
            errorMessage = "???????????? ?? ???????????? " + methodName + ": " + e.getLocalizedMessage();
            logger.error(errorMessage);
        } catch (Exception e2) {
            System.out.println("???????????? ?? getErrorMessage: "+e.getLocalizedMessage());
        }
        return errorMessage;
    }

    private void smartSleep(int time) {
        try {
            try {
                ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            } catch (Exception e) {
                time += 1000;
                Thread.sleep(time);
                try {
                    ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                } catch (Exception e2) {
                    time += 1000;
                    Thread.sleep(time);
                    ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                }
            }
            Thread.sleep(time);
            try {
                ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                Thread.sleep(100);
            } catch (Exception e) {
                time += 1000;
                Thread.sleep(time);
                try {
                    ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                } catch (Exception e2) {
                    time += 1000;
                    Thread.sleep(time);
                    ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                }
                Thread.sleep(time);
            }
        } catch (Exception e) {
            getErrorMessage(e,"smartSleep");
        }
    }

    public Boolean run() {
        String errorPerProcess = "";
        try {
            openNVP();
            getCSV();
            Thread.sleep(4000);

        } catch (Exception e) {
            getErrorMessage(e,"run");
        }
        return true;
    }

    public void openNVP() {
        try {
            try {
                driver.get(AppEnv.getContext().getUrlNvpApp() + "/ViplataWEB/login.jsp");
            } catch (NoSuchWindowException nswe) {
                Thread.sleep(sleepTime);
                Set<String> windowHandles = driver.getWindowHandles();
                Iterator<String> iterator = windowHandles.iterator();
                driver.switchTo().window(iterator.next());
                Thread.sleep(sleepTime);
                driver.get(AppEnv.getContext().getUrlNvpApp() + "/ViplataWEB/login.jsp");
            }

            smartSleep(sleepTime);
            WebElement webElement = driver.findElement(By.name("j_username"));

            webElement.sendKeys(AppEnv.getContext().getUserNvpApp());
            String text = webElement.getText();
            if (!text.equals(AppEnv.getContext().getUserNvpApp())) {
                webElement.clear();
                webElement.sendKeys(AppEnv.getContext().getUserNvpApp());
            }

            smartSleep(sleepTime);
            webElement = driver.findElement(By.name("j_password"));

            webElement.sendKeys(AppEnv.getContext().getPasswordNvpApp());
            text = webElement.getText();
            if (!text.equals(AppEnv.getContext().getPasswordNvpApp())) {
                webElement.clear();
                webElement.sendKeys(AppEnv.getContext().getPasswordNvpApp());
            }

            smartSleep(sleepTime);
            webElement = driver.findElement(By.name("action"));
            webElement.sendKeys(Keys.ENTER);
            smartSleep(sleepTime);

        } catch (Exception e) {
            getErrorMessage(e,"openNVP");
        }
    }

    public void close() {
        try {
            driver.quit();
        } catch (Exception e) {
            getErrorMessage(e,"close");
        }
    }

    public People getExcel() {
        People peop = new People();
        try {
            FileInputStream vyp = new FileInputStream(file);
            Workbook wb = new HSSFWorkbook(vyp);
            Sheet sheet1 = wb.getSheetAt(0);
            int n = sheet1.getLastRowNum();
            System.out.println(n);
            for (int i=0;i<n;i++){
                if(!isNull(wb.getSheetAt(0).getRow(i+1).getCell(0))) {
                    String a = wb.getSheetAt(0).getRow(i + 1).getCell(0).getStringCellValue().replace("-","");
                    a = a.replace(" ","");
                    System.out.println("?????????? " + a);
                    peop.setsnils(a);
                }
                if(!isNull(wb.getSheetAt(0).getRow(i+1).getCell(1))) {
                    System.out.println("?????????? ?????????????????? "+wb.getSheetAt(0).getRow(i + 1).getCell(1).getStringCellValue());
                    peop.setndocument(wb.getSheetAt(0).getRow(i + 1).getCell(1).getStringCellValue());
                }
                if(!isNull(wb.getSheetAt(0).getRow(i+1).getCell(2))) {
                    SimpleDateFormat parser2 = new SimpleDateFormat("dd.MM.yyyy");
                    String a = parser2.format(wb.getSheetAt(0).getRow(i + 1).getCell(2).getDateCellValue());
                    a = a.replace(".", "");
                    System.out.println("???????? ???????????? ?????????????????? " + a);
                    peop.setdvdocument(a);
                }
                if(!isNull(wb.getSheetAt(0).getRow(i+1).getCell(3))) {
                    SimpleDateFormat parser2 = new SimpleDateFormat("dd.MM.yyyy");
                    String a = parser2.format(wb.getSheetAt(0).getRow(i + 1).getCell(3).getDateCellValue());
                    a = a.replace(".", "");
                    System.out.println("???????? ???????????? ?????????????????? " + a);
                    peop.setdatestartuder(a);
                }
                if(!isNull(wb.getSheetAt(0).getRow(i+1).getCell(4))) {
                    SimpleDateFormat parser2 = new SimpleDateFormat("dd.MM.yyyy");
                    String a = parser2.format(wb.getSheetAt(0).getRow(i + 1).getCell(4).getDateCellValue());
                    a = a.replace(".", "");
                    System.out.println("???????? ?????????????????? ?????????????????? " + a);
                    peop.setdateenduder(a);
                }
                if(!isNull(wb.getSheetAt(0).getRow(i+1).getCell(5))) {
                    System.out.println("?????????? ?????????? ?????????????????? "+wb.getSheetAt(0).getRow(i + 1).getCell(5).getNumericCellValue());
                    String a = Double.toString(wb.getSheetAt(0).getRow(i + 1).getCell(5).getNumericCellValue());
                    a = a.replace(".",",");
                    System.out.println(a);
                    peop.setsummauder(a);
                }
            }
        }
        catch (Exception e){
            logger.info("file error");
            e.printStackTrace();
            logger.error(e.getLocalizedMessage());
        }
        return peop;
    }

    public void getCSV() {
        HSSFWorkbook xls = new HSSFWorkbook();
        HSSFSheet sheet = xls.createSheet();
        HSSFRow row;
        row = sheet.createRow(0);
        row.createCell(0).setCellValue("?????????????????? ??????????");

        System.out.println(filecsv);
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        int lines=0;
        try {
            br = new BufferedReader(new FileReader(filecsv));
            while ((line = br.readLine()) != null) {
                lines++;
            }
        }
        catch (Exception e){
        logger.info("file error");
        e.printStackTrace();
        logger.error(e.getLocalizedMessage());
        }
        System.out.println(lines);

        People peop = new People();
        try {
            int first=0;
            br = null;
            br = new BufferedReader(new FileReader(filecsv));
            int i=0;
            while ((line = br.readLine()) != null) {
                if (first != 0) {
                    String[] people = line.split(cvsSplitBy);
                    System.out.println("[??????????= " + people[1].trim() +", ?????????? ??????????????????= " + people[2].trim() + " , ???????? ????????????=" + people[3].trim() + " , ???????? ??=" + people[4].trim() + " , ???????? ????=" + people[5].trim() + " , ????????????????=" + people[6].trim() + " , ??????????=" + people[7].trim() + " , ??????????????=" + people[8].trim() +" , ????????=" + people[9].trim()+ "] ");

                    peop.setsnils(people[0].trim().replace("-","").replace(" ",""));
                    peop.setndocument(people[2].trim());
                    peop.setdvdocument(people[3].trim().substring(0,10).replace(".",""));
                    peop.setdatestartuder(people[4].trim().replace(".",""));
                    peop.setdateenduder(people[5].trim().replace(".",""));
                    peop.setistochnic(people[6].trim());
                    peop.setsummauder(people[7].replace("??",""));
                    peop.setprichina(people[8].trim());
                    peop.setschet(people[9].trim());

                    inserUderP(peop, sheet, row);
                    i++;
                }
                first=1;
            }
                br.close();

            File d = new File(wayforfile+"???????????????????????? ????????.xls");
            FileOutputStream file = new FileOutputStream(d);
            xls.write(file);
            xls.close();
        }
        catch (Exception e){
            logger.info("file error");
            e.printStackTrace();
            logger.error(e.getLocalizedMessage());
        }
    }

    public void inserUderP(People p, HSSFSheet sheet, HSSFRow row) {
        try {
            //?????????? ????????????????????
            String frameLocator1 = "//frame[@title='Task Navigation Frame']";
            String elementLocator1 = "//a[@title='?????????? ????????????????????']";
            driver.get(AppEnv.getContext().getUrlNvpApp() + "/ViplataWEB/");
            WebElement frame1 = driver.findElement(By.xpath(frameLocator1));
            driver.switchTo().frame(frame1);
            WebElement element2 = driver.findElement(By.xpath(elementLocator1));
            element2.sendKeys("\n");

            smartSleep(sleepTime);
            driver.switchTo().defaultContent();
            smartSleep(sleepTime);

            //?????????? ???? ????????????
            String frameLocator2 = "//frame[@title='Work Area Frame']";
            WebElement frame2 = driver.findElement(By.xpath(frameLocator2));
            driver.switchTo().frame(frame2);

            smartSleep(sleepTime);

            while(!(driver.findElement(By.xpath("//input[@id='form2:textFilterNpers']"))).isDisplayed()){
                smartSleep(sleepTime);
            }

            WebElement webElement = driver.findElement(By.xpath("//input[@id='form2:textFilterNpers']")); //(By.id("form2:textFilterNpers"));
            webElement.sendKeys(Keys.HOME);
            webElement.sendKeys(p.getsnils());

            webElement = driver.findElement(By.xpath("//input[@id='form2:checkbox9']")); //(By.id("form2:checkbox9"));
            if(webElement.isSelected()) {
                webElement.sendKeys(Keys.SPACE);
                smartSleep(sleepTime);
            }

            webElement = driver.findElement(By.xpath("//input[@id='form2:filterButton']")); //By.name("form2:filterButton"));
            webElement.sendKeys(Keys.ENTER);
            smartSleep(sleepTime);

            while(!(driver.findElement(By.xpath("//td[@class='table-td'][@style='']/a[@class='commandLink']"))).isDisplayed()){
                smartSleep(sleepTime);
            }

            //?????????? ???????????????????????? ???????????????????? ????????
            webElement = driver.findElement(By.xpath("//td[@class='table-td'][@style='']/a[@class='commandLink']"));
            webElement.sendKeys(Keys.ENTER);
            smartSleep(sleepTime);

            //?????????????? ???? ?????????????? ????????
            String frameLocator = "//frame[@title='Header Frame']";
            driver.get(AppEnv.getContext().getUrlNvpApp() + "/ViplataWEB/");
            WebElement frame = driver.findElement(By.xpath(frameLocator));
            driver.switchTo().frame(frame);

            webElement = driver.findElement(By.xpath("//*[@id='form1:linkEx6']"));
            System.out.println(webElement);
            webElement.click();
            webElement.sendKeys("\n");
            smartSleep(sleepTime);

            driver.switchTo().defaultContent();
            smartSleep(sleepTime);

            //???????????????????? ??????????????????
            String frameLocator3 = "//frame[@title='Task Navigation Frame']";
            WebElement frame3 = driver.findElement(By.xpath(frameLocator3));
            driver.switchTo().frame(frame3);

            while(!(driver.findElement(By.xpath("//a[@title='???????????????????? ?????????????????? / ???????????????? ??????????????']"))).isDisplayed()){
                smartSleep(sleepTime);
            }

            webElement = driver.findElement(By.xpath("//a[@title='???????????????????? ?????????????????? / ???????????????? ??????????????']"));
            webElement.sendKeys("\n");
            smartSleep(sleepTime);

            driver.switchTo().defaultContent();
            smartSleep(sleepTime);

            String frameLocator4 = "//frame[@title='Work Area Frame']";
            WebElement frame4 = driver.findElement(By.xpath(frameLocator4));
            driver.switchTo().frame(frame4);

            while(!(driver.findElement(By.xpath("//input[@id='form1:table1:addButton']"))).isDisplayed()){
                smartSleep(sleepTime);
            }

            //????????????????
            webElement = driver.findElement(By.xpath("//input[@id='form1:table1:addButton']"));
            webElement.sendKeys(Keys.ENTER);

            while(!(driver.findElement(By.xpath("//input[@id='form1:textDoc1']"))).isDisplayed()){
                smartSleep(sleepTime);
            }

            //?????????? ??????????????????
            webElement = driver.findElement(By.xpath("//input[@id='form1:textDoc1']"));
            webElement.sendKeys(p.getndocument());

            //???????????????? ??????????????????
            webElement = driver.findElement(By.xpath("//input[@id='form1:textDocnd1']"));
            webElement.sendKeys("???????????????? ???????? ?? ???????????????????? ??????????????????");

            //?????? ??????????????????
            webElement = driver.findElement(By.xpath("//input[@id='form1:textVv']"));
            webElement.sendKeys("?????????? ?????????????? ?????????????????????? ???????? ??????");

            /*
            //???????????????? ??????????????????
            Select select1 = null;
            WebElement webElement11 = driver.findElement(By.id("form1:menu6"));
            select1 = new Select(webElement11);
            select1.selectByValue("12");
             */

            //???????? ???????????? ??????????????????
            webElement = driver.findElement(By.xpath("//input[@id='form1:textDocdv1']")); //(By.id("form1:textDocdv1"));
            webElement.sendKeys(Keys.HOME);
            webElement.sendKeys(p.getdvdocument());

            /*
            //?????? ??????????????????
            Select select = null;
            WebElement webElement10 = driver.findElement(By.id("form1:menu3"));
            select = new Select(webElement10);
            select.selectByValue("1");
            smartSleep(sleepTime);
             */

            //?????? ??????????????????
            Select select2 = null;
            webElement = driver.findElement(By.xpath("//select[@id='form1:menu1']"));
            select2 = new Select(webElement);
            select2.selectByValue("6");
            smartSleep(sleepTime);
            smartSleep(sleepTime);

            //???????? ???????????? ??????????????????
            webElement = driver.findElement(By.xpath("//input[@id='form1:text2']"));
            webElement.click();
            webElement.sendKeys(Keys.HOME);
            webElement.sendKeys(p.getdatestartuder());
            smartSleep(sleepTime);
            smartSleep(sleepTime);

            //???????? ?????????????????? ??????????????????
            webElement = driver.findElement(By.xpath("//input[@name='form1:text3']"));
            webElement.sendKeys(Keys.HOME);
            smartSleep(sleepTime);
            webElement = driver.findElement(By.xpath("//input[@name='form1:text3']"));
            webElement.sendKeys(Keys.HOME);
            webElement.sendKeys(p.getdateenduder());
            smartSleep(sleepTime);

            //???????????? ?????????????????? ??????????????????
            Select select3 = null;
            webElement = driver.findElement(By.xpath("//select[@name='form1:menu4']"));
            select3 = new Select(webElement);
            select3.selectByValue("3");
            smartSleep(sleepTime);

            /*
            //?????????????? ????????????????
            Select select4 = null;
            WebElement webElement16 = driver.findElement(By.id("form1:menu2"));
            select4 = new Select(webElement16);
            select4.selectByValue("4");
            smartSleep(sleepTime);
             */

            //????????????????
            webElement = driver.findElement(By.xpath("//input[@id='form1:refreshButton']"));
            webElement.sendKeys(Keys.ENTER);
            smartSleep(sleepTime);

            //??????????
            webElement = driver.findElement(By.xpath("//input[@id='form1:selectWprLink']"));
            webElement.sendKeys(Keys.ENTER);
            smartSleep(sleepTime);

            while(!(driver.findElement(By.xpath("//input[@name='form2:j_id_jsp_1823085157_4']"))).isDisplayed()){
                smartSleep(sleepTime);
            }

            //?????????? ??????
            webElement = driver.findElement(By.xpath("//input[@name='form2:j_id_jsp_1823085157_4']"));
            webElement.clear();
            webElement.sendKeys("0323048828");
            smartSleep(sleepTime);

            //?????????? ??????
            webElement = driver.findElement(By.xpath("//input[@name='form2:j_id_jsp_1823085157_5']"));
            webElement.clear();
            webElement.sendKeys("032601001");
            smartSleep(sleepTime);

            //?????????? ??????
            webElement = driver.findElement(By.xpath("//input[@name='form2:j_id_jsp_1823085157_6']"));
            webElement.clear();
            webElement.sendKeys("018142016");
            smartSleep(sleepTime);

    /*        //??????????
            WebElement webElement23 = driver.findElement(By.id("form2:filterButton"));
            webElement23.sendKeys(Keys.ENTER);
            smartSleep(sleepTime);

            //??????????????
            WebElement webElement25 = driver.findElement(By.id("form3:table1:0:selectLink"));
            webElement25.sendKeys(Keys.ENTER);
            smartSleep(sleepTime);
     */
            //??????????
            webElement = driver.findElement(By.xpath("//input[@id='form2:filterButton']"));
            webElement.sendKeys(Keys.ENTER);
            smartSleep(sleepTime);
            smartSleep(sleepTime);
            smartSleep(sleepTime);

            //??????????????
            webElement = driver.findElement(By.xpath("//input[@id='form3:table1:0:rowSelect1__input_sel']"));  //By.id("form3:table1:0:selectLink"));
            webElement.sendKeys(Keys.SPACE);
            smartSleep(sleepTime);

            webElement = driver.findElement(By.xpath("//input[@id='form3:table1:buttonSelect']"));
            webElement.sendKeys(Keys.ENTER);
            smartSleep(sleepTime);

            while(!(driver.findElement(By.xpath("//input[@id='form1:textSpe2']"))).isDisplayed()){
                smartSleep(sleepTime);
            }

            //?????????? ?????????? ??????????????????
            webElement = driver.findElement(By.xpath("//input[@id='form1:textSpe2']"));
            webElement.click();
            webElement.sendKeys(Keys.ARROW_LEFT);
            webElement.sendKeys(Keys.ARROW_LEFT);
            webElement.sendKeys(Keys.ARROW_LEFT);
            smartSleep(sleepTime);
            webElement.sendKeys(p.getsummauder());
            smartSleep(sleepTime);


            //?????????????????? ????????????????????
            webElement = driver.findElement(By.xpath("//input[@id='form1:selectRazdelLink']"));
            webElement.sendKeys(Keys.ENTER);
            smartSleep(sleepTime);

            webElement = driver.findElement(By.xpath("//span[@id='form1:text_filteredRows22']"));
            int q1 = Integer.parseInt(webElement.getText().replace(".",""));
            q1=q1-1;
            System.out.println("?????????????????? "+(q1+1));

            int flag=1111;
            for(int i=0; i<q1+1; i++) {
                if(i!=0) {
                    webElement = driver.findElement(By.xpath("//input[@id='form1:selectRazdelLink']"));
                    webElement.sendKeys(Keys.ENTER);
                    smartSleep(sleepTime);
                }
                while (!(driver.findElement(By.xpath("//a[@id='form1:dataTable2:0:command_link_razdel2']"))).isDisplayed()) {
                    smartSleep(sleepTime);
                }
                webElement = driver.findElement(By.xpath("//input[@id='form1:dataTable2:"+i+":rowSelect_rows2__input_sel']"));
                webElement.sendKeys(Keys.SPACE);
                webElement = driver.findElement(By.xpath("//input[@id='form1:dataTable2:button1_2']"));
                webElement.sendKeys(Keys.ENTER);
                smartSleep(sleepTime);

                while (!(driver.findElement(By.xpath("//input[@id='form1:selectIstLink']"))).isDisplayed()) {
                    smartSleep(sleepTime);
                }

                //????????????????
                webElement = driver.findElement(By.xpath("//input[@id='form1:selectIstLink']"));
                webElement.sendKeys(Keys.ENTER);
                smartSleep(sleepTime);

                //?????????? ??????????????????
                webElement = driver.findElement(By.xpath("//span[@id='form1:text_filteredRows22']"));
                int q2 = Integer.parseInt(webElement.getText().replace(".",""));
                q2=q2-1;
                System.out.println("???????????????????? "+(q2+1));
                for (int j=0; j<(q2+1); j++){
                    System.out.println("//span[@id='form1:dataTable2:"+j+":text_ist22']");
                    webElement = driver.findElement(By.xpath("//span[@id='form1:dataTable2:"+j+":text_ist22']"));
                    System.out.println(webElement.getText());
                    if(webElement.getText().equals(p.getistochnic())){
                        flag=j;
                    }
                }

                if(flag!=1111) {
                    webElement = driver.findElement(By.xpath("//input[@id='form1:dataTable2:"+flag+":rowSelect_rows2__input_sel']"));
                    webElement.sendKeys(Keys.SPACE);
                    webElement = driver.findElement(By.xpath("//input[@id='form1:dataTable2:button1_2']"));
                    webElement.sendKeys(Keys.ENTER);
                    smartSleep(sleepTime);
                    break;
                }
                else{
                    webElement = driver.findElement(By.xpath("//input[@id='form1:dataTable2:button2_2']"));
                    webElement.sendKeys(Keys.ENTER);
                }
            }
            
            //?????? ?????????????????????????? ??????????????
            select2 = null;
            webElement = driver.findElement(By.id("form1:menu41"));
            select2 = new Select(webElement);
            select2.selectByValue("0");
            smartSleep(sleepTime);


            //???????? ?????????????????????????? ??????????????????????????
            webElement = driver.findElement(By.xpath("//input[@name='form1:arrearsDate']"));
            webElement.sendKeys(Keys.HOME);
            smartSleep(sleepTime);
            webElement = driver.findElement(By.xpath("//input[@name='form1:arrearsDate']"));
            webElement.sendKeys(Keys.HOME);
            webElement.sendKeys(p.getdvdocument());
            smartSleep(sleepTime);

            //?????????????????? ???? ???????? ????????????????????
            Select select4 = null;
            webElement = driver.findElement(By.xpath("//select[@name='form1:menu5']"));
            if(p.getschet().equals("209")) {
                select4 = new Select(webElement);
                select4.selectByValue("1");
            }
            if((p.getschet().equals("16"))||(p.getschet().equals("??31"))){
                select4 = new Select(webElement);
                select4.selectByValue("0");
            }
            smartSleep(sleepTime);

            //?????????????? ?????????????????????? ?????????????? ?????????????????????? ???????? ????????????
            webElement = driver.findElement(By.xpath("//input[@id='form1:reasonCommentText']"));
            webElement.sendKeys(p.getprichina());
            smartSleep(sleepTime);

            //?????? ??????????????
            webElement = driver.findElement(By.xpath("//input[@id='form1:text9']"));
            webElement.sendKeys("39211302996066000130");
            smartSleep(sleepTime);

            /*
            select2 = null;
            webElement25 = driver.findElement(By.id("form1:menu11"));
            select2 = new Select(webElement25);
            select2.selectByValue("7");
            smartSleep(sleepTime);
             */

             //??????????????????/????????????????????
            webElement = driver.findElement(By.xpath("//input[@id='form1:saveButton']"));
            webElement.sendKeys(Keys.ENTER);
            smartSleep(sleepTime);

            smartSleep(sleepTime);
            while(!(driver.findElement(By.xpath("//span[@id='form1:viewFragmentPaging:filtredText']"))).isDisplayed()){//(By.id("form1:viewFragmentPaging:filtredText")).isDisplayed())){
            smartSleep(sleepTime);}

            driver.switchTo().defaultContent();
            smartSleep(sleepTime);

            driver.switchTo().frame(frame3);

            webElement = driver.findElement(By.xpath("//a[@title='???????????????????? ?????????????????? / ???????????????? ??????????????']"));
            webElement.sendKeys("\n");
            smartSleep(sleepTime);

            driver.switchTo().defaultContent();
            smartSleep(sleepTime);

            driver.switchTo().frame(frame4);

            webElement = driver.findElement(By.xpath("//span[@id='form1:viewFragmentPaging:filtredText']"));
            smartSleep(sleepTime);
            smartSleep(sleepTime);

            int q = Integer.parseInt(webElement.getText().replace(".",""));
            q=q-1;
            String a = "form1:table1:"+q+":rowSelect1__input_sel";
            System.out.println(a);

            webElement = driver.findElement(By.xpath("//input[@name='"+a+"']"));
            webElement.sendKeys(Keys.SPACE);
            smartSleep(sleepTime);

            webElement = driver.findElement(By.xpath("//input[@name='form1:table1:calcUder']"));
            webElement.sendKeys(Keys.ENTER);

            while(!(driver.findElement(By.xpath("//ul[@id='form1:messages1']"))).getText().equals("???????????????????? ???????????? ??????????????!")){
                smartSleep(sleepTime);
            }

            webElement = driver.findElement(By.xpath("//span[@id='form1:viewFragmentPaging:filtredText']"));
            smartSleep(sleepTime);
            smartSleep(sleepTime);

            q = Integer.parseInt(webElement.getText().replace(".",""));
            q=q-1;
            a = "form1:table1:"+q+":rowSelect1__input_sel";
            System.out.println(a);

            webElement = driver.findElement(By.xpath("//input[@name='"+a+"']"));
            webElement.sendKeys(Keys.SPACE);
            smartSleep(sleepTime);

            String start = autoit+" "+"protokol_"+p.getsnils()+" "+way;
            Process pr = Runtime.getRuntime().exec(start);

            //???????????????? ?? ??????????????????
            webElement = driver.findElement(By.xpath("//input[@name='form1:table1:renderDetectionProtocol']"));
            webElement.sendKeys(Keys.ENTER);

            String snils = p.getsnils().substring(0,3)+"-"+p.getsnils().substring(3,6)+"-"+p.getsnils().substring(6,9)+" "+p.getsnils().substring(9,11);

            row = sheet.createRow(count);
            row.createCell(0).setCellValue(snils);
            count++;

            pr.waitFor();
       //     Thread.sleep(2000);

        } catch (Exception e) {
            getErrorMessage(e,"insertUder");
        }
    }
}