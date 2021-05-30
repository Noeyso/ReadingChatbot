package com.example.mobilesw.info;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NaverAPI {
    //결과 값을 받기 위해서 return
    /*
     option
     0 : 문자열 검색
     1 : 책 제목 검색 d_titl
     2 : 저자명 검색 d_auth
     3 : 출판사 검색 d_publ
     */
    public static String main(String apiURL,String query,int start,int option) {
        //static: 클래스 생성없이 메서드 사용가능
        String clientId = "WZc2bICVOzzUatq7dor3";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "28lFn3Ew9x";//애플리케이션 클라이언트 시크릿값";

        try {
            String text = URLEncoder.encode(query, "UTF-8");
            switch(option){
                case 0:
                    apiURL += "query="+ text; // json 결과
                    break;
                case 1:
                    apiURL+="d_titl="+text;
                    break;
                case 2:
                    apiURL+="d_auth="+text;
                    break;
                case 3:
                    apiURL+="d_publ="+text;
                    break;
                default:
                    break;
            }

            apiURL += "&start=" + start; //검색 시작위치로 최대 1000까지 가능
            apiURL += "&display=10"; //검색결과 출력건수 지정
            System.out.println("url 출력 : "+apiURL);

            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println("응답 : "+response.toString());
            return response.toString();
        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }
}
