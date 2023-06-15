/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cblnotification;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class CBLNotification {

   public static String POSTRequest(String args) throws IOException {
       String resp = "";
	 
       Properties prop = new Properties();
       String propFileName = "./urlProperties.properties";
       Properties props = System.getProperties();
       props.setProperty("webhook.server", "https://webhook-server.creditbank.co.ke");
       
       
       
       InputStream inputStream;
       
       inputStream = CBLNotification.class.getClassLoader().getResourceAsStream(propFileName);
             if (inputStream != null) {
                 prop.load(inputStream);
            } else {
                throw new FileNotFoundException("Property file " + propFileName + " not found");
            }
        
       String url = prop.getProperty("notificationURL");
            
            String[] argParams = args.split("#");
            
            String amountParam= argParams[0];
            String debitCreditParam = argParams[1];
            String txnRefParam = argParams[2];
            String txnCBSRefParam = argParams[3];
            String txnMobileParam = argParams[4];
            String mpesaRefParam = argParams[5];
            String acctParam = argParams[6];           
            String txnDateParam = argParams[7];
        
            final String POST_PARAMS = "{\n    \"notificationType\": \"FT\",\n    \"notificationSubType\": \"CBS_FT\",\n    \"data\": {\n        \"amount\": \""+amountParam+"\",\n        \"transactionType\": \""+debitCreditParam+"\",\n        \"transactionReference\": \""+txnRefParam+"\",\n        \"CBSReference\": \""+txnCBSRefParam+"\",\n        \"mobileNumber\": \""+txnMobileParam+"\",\n        \"momoReference\": \""+mpesaRefParam+"\",\n        \"custAccount\": \""+acctParam+"\",\n        \"timestamp\": \""+txnDateParam+"\"\n    }\n}";        
            
            try {
            	           	
                URL obj = new URL(url); 
        
    		    HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
    		    postConnection.setRequestMethod("POST");
    		    postConnection.setRequestProperty("userId", "a1bcdefgh");
    		    postConnection.setRequestProperty("Content-Type", "application/json");
    		  		
    		    postConnection.setDoOutput(true);
    		    OutputStream os = postConnection.getOutputStream();
    		    os.write(POST_PARAMS.getBytes());
    		    os.flush();  // garbage collector for performance
    		    os.close();   		
    		
    		    int responseCode = postConnection.getResponseCode();
    //		    System.out.println(responseCode);
                    System.out.println(obj);
    		    
    		    if (responseCode == HttpURLConnection.HTTP_CREATED) { //success
    		        BufferedReader in = new BufferedReader(new InputStreamReader(
    		            postConnection.getInputStream()));
    		        String inputLine;
    		        StringBuilder response = new StringBuilder();
    		
    		        while ((inputLine = in .readLine()) != null) {
    		            response.append(inputLine);
    		        } in .close();
    		
    		        resp=response.toString();
    		        
    		    } else {
    		        resp="Error Processing Request. Check webhook server."; 		        
    		    }

              } catch (SocketTimeoutException ex) {
            	  resp = "Conn Time Out Error:" +ex;
              } catch (IOException ioe) {
            	  resp = "config File Error:" +ioe;
              } 
            
return resp;
}
   
   
public static void execute(){
           TrustManager[] trustAllCerts = new TrustManager[] {
                 
                       new X509TrustManager() {
                       public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                       return null;
                   
                       }
          @Override
          public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                       throws CertificateException {}
 
          @Override
          public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                       throws CertificateException {}

          }
     };

                  SSLContext sc=null;
             
            try {
                  
                    sc = SSLContext.getInstance("SSL");
                    
               } catch (NoSuchAlgorithmException e) {
                   
                       e.printStackTrace();
              
               }
            
        try {
            
                 sc.init(null, trustAllCerts, new java.security.SecureRandom());
                  
            } catch (KeyManagementException e) {
                 
                         e.printStackTrace();
                         
             }
        
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            
            

      // Create all-trusting host name verifier
          HostnameVerifier validHosts = new HostnameVerifier() {
              
           @Override
           public boolean verify(String arg0, SSLSession arg1) { 
               
           return true;
           
           
           }
         };
         // All hosts will be valid
          HttpsURLConnection.setDefaultHostnameVerifier(validHosts); 
          
   }


//execution done here
      public static void main(String[] args) {
      
             try {
        	 //For testing only
             CBLNotification GetAndPost = new CBLNotification(); //object creation   
             GetAndPost.execute();
           
            
             String testResp = GetAndPost.POSTRequest("500.00#credit#MPESAB2C#FT23089G41GG#254705524792##0141007000038#2023-05-22"); 
             
             System.out.println(testResp);
             
                         
           } 
             
             catch (IOException e) {
                  
                   e.printStackTrace();
             
             } catch (Exception e) {
                 
                   e.printStackTrace();
                 
            } 
             
             
         
         
    }
  }
    

