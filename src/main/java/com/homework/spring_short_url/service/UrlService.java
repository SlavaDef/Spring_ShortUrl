package com.homework.spring_short_url.service;

import com.homework.spring_short_url.controller.UrlController;
import com.homework.spring_short_url.dto.UrlStatDTO;
import com.homework.spring_short_url.models.UrlRecord;
import com.homework.spring_short_url.repo.UrlRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


// DB -> Enteti(20) -> Repo -> Service -> DTO <- Controller -> View / JSON (5)
// ідея наступна - не завжди треба всі поля ентеті, тому поля конвертують у дто а з них вже у контроллер
//  дто це звичайні класи в які ми копіюємо данні з ентеті які нам потрібні
//@AllArgsConstructor
@Slf4j
@RequiredArgsConstructor
@EnableAsync
@Service // анотація похідна від компоненту в рантаймі створюється автоматично реалізує логіку по верх репозитирія
public class UrlService {

    private static final Logger LOGGER = LogManager.getLogger(UrlController.class);

    private final UrlRepository urlRepository;


    // ініціалізується транзакція - виконується код якщо ексепшена нема то транзакція комит якщо є відкатується
    @Transactional
    public UrlRecord createUrl(String url) {
        UrlRecord urlRecord = urlRepository.findByUrl(url);
        if (urlRecord == null || urlRecord.getShortUrl() == null) {
            urlRecord = new UrlRecord(url);
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

    @Transactional
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

    @Transactional
    public UrlRecord deleteUrlByShortLink(String link) {
        UrlRecord urlRecord = urlRepository.findByShortUrl(link);
        log.info(urlRecord.getShortUrl());
        urlRepository.delete(urlRecord);
        return urlRecord;
    }

    @Transactional
    public void deleteUrlRecord(UrlRecord urlRecord) {
        urlRepository.delete(urlRecord);

    }

    public void deleteShortUrl(Long id) {
      UrlRecord urlRecord =  urlRepository.findById(id).isPresent() ? urlRepository.findById(id).get() : null;
        assert urlRecord != null;
        urlRecord.setShortUrl("");
      urlRepository.save(urlRecord);
    }

    @Transactional
    public UrlRecord getById(Long id) {
        return urlRepository.findById(id)
                .orElseThrow(IllegalStateException::new);
    }

    @Transactional
    @Scheduled(cron = "0 0 18 07 * ?", zone = "Europe/Kiev")
    @Async
    void deleteOldUrlRecords() {

        List<UrlRecord> list = urlRepository.findAll();
        for (UrlRecord urlRecord : list) {
            if (urlRecord.getLastAccess().compareTo(LocalDateTime.now()) < 0) {
                urlRecord.setShortUrl("");
            }
        }
        log.info("The old shortLink has been deleted.");

    }

}
