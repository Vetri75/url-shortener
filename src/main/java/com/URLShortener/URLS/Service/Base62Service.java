package com.URLShortener.URLS.Service;


import org.springframework.stereotype.Service;

@Service
public class Base62Service {

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String encode(long id){
        if(id == 0) return "a";
        StringBuilder sb = new StringBuilder();
        while(id > 0){
            sb.append(BASE62.charAt((int) (id % 62)));
            id /= 62;
        }
        return sb.reverse().toString();
    }

    public long decode(String encode){
        long id = 0;
        for(char ch : encode.toCharArray()){
            id = id * 62 + BASE62.indexOf(ch);
        }
        return id;
    }

}
