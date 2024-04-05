package com.homework.spring_short_url.service;

import com.homework.spring_short_url.controller.UrlController;
import com.homework.spring_short_url.dto.UrlDTO;
import com.homework.spring_short_url.dto.UrlStatDTO;
import com.homework.spring_short_url.models.UrlRecord;
import com.homework.spring_short_url.repo.UrlRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


// DB -> Enteti(20) -> Repo -> Service -> DTO <- Controller -> View / JSON (5)
// ідея наступна - не завжди треба всі поля ентеті, тому поля конвертують у дто а з них вже у контроллер
//  дто це звичайні класи в які ми копіюємо данні з ентеті які нам потрібні
@AllArgsConstructor
@Service // анотація похідна від компоненту в рантаймі створюється автоматично реалізує логіку по верх репозитирія
public class UrlService {

    private static final Logger LOGGER = LogManager.getLogger(UrlController.class);

    private final UrlRepository urlRepository;


    // ініціалізується транзакція - виконується код якщо ексепшена нема то транзакція комит якщо є відкатується
    @Transactional
    public UrlRecord createUrl(String url) {
        UrlRecord urlRecord = urlRepository.findByUrl(url);
        if(urlRecord==null) {
            urlRecord=new UrlRecord(url);
            urlRecord.setShortUrl(getRandomString(7));
        }
        urlRepository.save(urlRecord);
        return urlRecord;
    }

    @Transactional
    public UrlRecord getUrl(String link) {

        UrlRecord urlRecord = urlRepository.findByShortUrl(link); // get entity by short link

        LOGGER.info("findByLink = " + urlRecord);

        urlRecord.setCount(urlRecord.getCount() + 1L); // count++
        urlRecord.setLastAccess(LocalDateTime.now()); // set Last Access
        urlRepository.save(urlRecord); // save in base ??? need or not?
        return urlRecord; // return object
    }

    @Transactional(readOnly = true) // тут методи тільки читають з бази
    public List<UrlStatDTO> getStatistics() {
        List<UrlRecord> records = urlRepository.findAll(); // витягаємо всі записи з репо
        ArrayList<UrlStatDTO> result = new ArrayList<UrlStatDTO>(); // з листа ентеті створюємо лист дто

        records.forEach(x -> result.add(x.toStatDTO())); // конвертуе в дто

        return result;
    }

    public List<UrlRecord> listAll() {
        return urlRepository.findAll();
    }

    public String getRandomString(int l) {
        String AlphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz";
        StringBuilder s = new StringBuilder(l);
        for (int i = 0; i < l; i++) {
            s.append(AlphaNumericStr.charAt(ThreadLocalRandom.current().nextInt(AlphaNumericStr.length())));
        }
        return s.toString();
    }

}
