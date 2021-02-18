package uft.developer.academy;


import com.hp.lft.report.ReportException;
import com.hp.lft.report.Reporter;
import com.hp.lft.sdk.GeneralLeanFtException;
import com.hp.lft.sdk.ModifiableSDKConfiguration;
import com.hp.lft.sdk.SDK;
import com.hp.lft.sdk.web.Browser;
import com.hp.lft.sdk.web.BrowserFactory;
import com.hp.lft.sdk.web.BrowserType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadCSV {

    @BeforeClass
    public void TearUp() throws Exception {
        ModifiableSDKConfiguration config = new ModifiableSDKConfiguration();
        config.setServerAddress(new URI("ws://localhost:5095"));
        SDK.init(config);
        Reporter.init();
    }

    @AfterClass
    public void TearDown() throws ReportException {
        Reporter.generateReport();
        SDK.cleanup();

    }


    // CSV
    @DataProvider (name="LoginDatacsv")
    public Object[][] logindatacsv() throws IOException {
        Object[][] dataTable = readData();
        return dataTable;
    }


    // Data provider used in Test
    @Test (dataProvider = "LoginDatacsv")
    private void mainTest(String pBrowser, String username, String password) throws GeneralLeanFtException, ReportException, InterruptedException {
        BrowserType pBrowserType = null;

        if (pBrowser.equals("Chrome")) {
            pBrowserType = BrowserType.CHROME;
        }
        if (pBrowser.equals("Firefox")) {
            pBrowserType = BrowserType.FIREFOX;
        }
        if (pBrowser.equals("EdgeChromium")) {
            pBrowserType = BrowserType.EDGE_CHROMIUM;
        }


        Reporter.reportEvent("Browser - " + pBrowser.toString(), "Browser used in this iteration: " + pBrowser.toString());

        Browser browser = BrowserFactory.launch(pBrowserType);
        browser.navigate("http://advantageonlineshopping.com");

        AOSModels AUTaos = new AOSModels(browser);
        AUTaos.AdvantageShoppingPage().UserMenu().click();
        AUTaos.AdvantageShoppingPage().Username().setValue(username);
        AUTaos.AdvantageShoppingPage().Password().setValue(password);
        AUTaos.AdvantageShoppingPage().SignIn().click();

        Thread.sleep(4000);

        browser.close();


    }


    // CSV Function of providing the right format for data provider

    private static final String DELIMITER = ";";

    private Object [][] readData() throws IOException {
        int rowcount = 0;
        int colcount = 0;
        String file = "C:\\Users\\khanamir\\IdeaProjects\\uftdeveloper\\src\\uft\\developer\\academy\\logindata.csv";
        int totalrows = (int) Files.lines(Paths.get(file)).count();
        BufferedReader colCount = new BufferedReader(new FileReader(file));
        int totalcols = 0;
        try {
            String rline = "";
            rline = colCount.readLine();
            totalcols = rline.split(DELIMITER).length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        String [][] content = new String[totalrows][totalcols];
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = "";
            line = br.readLine(); //skipping first line
            while ((line = br.readLine()) != null) {
                String[] myData = line.split(DELIMITER);
                colcount = 0;
                for (String s: myData) {
                    System.out.println(s);
                    content[rowcount][colcount]= s;
                    colcount=colcount + 1;
                }
                rowcount=rowcount + 1;

            }
        } catch (FileNotFoundException e) {
            //Some error logging
        }
        return content;
    }


}
