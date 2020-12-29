package com.example.mail_server.Model.Search;

import com.example.mail_server.Model.Mail.Priority;
import com.example.mail_server.Model.Mail.indexMail;

import java.util.Arrays;
import java.util.LinkedList;

public class searchMailsFacade {
    public LinkedList<mailSearchResults> search(LinkedList<indexMail> mails, String x){
        LinkedList<mailSearchResults> mailSearchResults = new LinkedList<>();
        BinarySearch binarySearch = new BinarySearch();
        LinearSearch linearSearch = new LinearSearch();
        for(indexMail mail : mails){
            Interval[] subjectOccurrences =  binarySearch.search(mail.getSortedSubject(),0,mail.getSortedSubject().length-1,x);
            Interval[] bodyOccurrences =  binarySearch.search(mail.getSortedBody(),0,mail.getSortedBody().length-1,x);
            Interval[] priorityOccurrences = linearSearch.search(Priority.mapPriority(mail.getPriority()),x);
            Interval[] senderOccurrences = linearSearch.search(mail.getSender(),x);
            Interval[] dateOccurrences = linearSearch.search(mail.getDate(),x);
            if((subjectOccurrences!=null&& subjectOccurrences.length>0 )|| (bodyOccurrences!=null&& bodyOccurrences.length>0 )|| (priorityOccurrences!=null && priorityOccurrences.length>0)|| (senderOccurrences!=null&&senderOccurrences.length>0) || (dateOccurrences!=null&&dateOccurrences.length>0))
                mailSearchResults.add(new mailSearchResults(mail, subjectOccurrences, bodyOccurrences, senderOccurrences, priorityOccurrences, dateOccurrences));
        }
        return mailSearchResults;
    }

}
