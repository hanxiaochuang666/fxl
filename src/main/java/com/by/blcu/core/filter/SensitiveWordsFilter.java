package com.by.blcu.core.filter;

import com.by.blcu.core.baseservice.redis.SensitiveRedisScriptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * @Description: check and filter Sensitive Words.
 * @author Daniel
 * @date 2019/11/8 9:57
 */
@Service
@Slf4j
public class SensitiveWordsFilter {

    @Resource
    SensitiveRedisScriptService  redisScriptService;
    /**
     * set of Sensitive Words, add for different types?
     */
    public HashMap sensitiveWords;
    /*
    public synchronized void init(Set<String> sensitiveWords) {

    }*/

    /**
     * check if has SensitiveWord
     *
     * @param s: sentence
     * @return true/false
     */
    public boolean hasSensitiveWord(String s) throws Exception {
        boolean flag = false;
        List<String> wordList = sentense_segment(s);

        Map<String,Object> argvMap = new HashMap<String,Object>();
        Long ret = redisScriptService.checkSensitve(wordList,argvMap);
        return (ret==1);
    }

    /**
     * return all SensitiveWord
     *
     * @param s:sentense
     * @return
     */
    public List<String> getSensitiveWord(String s) throws IOException {
        List<String> sensitiveWordList = new ArrayList<String>();

        List<String> wordList = sentense_segment(s);
        /*Set<String> sensitiveWordList = new HashSet<>();
        for (String word : wordList) {
            if (sensitiveWords.getSensitive(wordList,argvMap) != null) {
                sensitiveWordList.add(word);
            }
        }*/
        Map<String,Object> argvMap = new HashMap<String,Object>();
        sensitiveWordList = redisScriptService.getSensitive(wordList,argvMap);
        return sensitiveWordList;
    }


    /**
     * replace with some string if has SensitiveWord
     *
     * @param s: sentence
     * @param replaceStr: string to replace with.
     * @return
     */
    public String replaceSensitiveWord(String s, String replaceStr) throws IOException {
        String resultTxt = s;
        List<String> sensitiveWordList = getSensitiveWord(s);
        for (String sensitiveWord : sensitiveWordList) {
            resultTxt = resultTxt.replaceAll(sensitiveWord, replaceStr);
        }
        return resultTxt;
    }


    /**
     * function for sentense segmentation
     *
     * @param s: sentence
     * @return list of words
     * @throws IOException
     */
    private List sentense_segment(String s) throws IOException {

        List<String> list = new ArrayList<>();
        StringReader re = new StringReader(s);
        IKSegmenter ik = new IKSegmenter(re, false);
        Lexeme lex;
        while ((lex = ik.next()) != null) {
            list.add(lex.getLexemeText());
        }

        return list;
    }


}
