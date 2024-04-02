package com.homework.spring_short_url.service;

import com.homework.spring_short_url.dto.UrlDTO;
import com.homework.spring_short_url.dto.UrlStatDTO;
import com.homework.spring_short_url.models.UrlRecord;
import com.homework.spring_short_url.repo.UrlRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


// DB -> Enteti(20) -> Repo -> Service -> DTO <- Controller -> View / JSON (5)
// ідея наступна - не завжди треба всі поля ентеті, тому поля конвертують у дто а з них вже у контроллер
//  дто це звичайні класи в які ми копіюємо данні з ентеті які нам потрібні
@AllArgsConstructor
@Service // анотація похідна від компоненту в рантаймі створюється автоматично реалізує логіку по верх репозитирія
public class UrlService {

    private final UrlRepository urlRepository;
// через конструктор інжект репозіторій з усіма методами


    @Transactional
    // ініціалізується транзакція - виконується код якщо ексепшена нема то транзакція комит якщо є відкатується
    public long saveUrl(UrlDTO urlDTO) {
        UrlRecord urlRecord = urlRepository.findByUrl(urlDTO.getUrl()); // за довгим урл знайди запис в таблиці чи скорочували ми вже його
        if (urlRecord == null) { // якщо такого нема то це новий
            urlRecord = UrlRecord.of(urlDTO); // копіюємо в дто обьект і зберігаємо в базу
            urlRepository.save(urlRecord);
        }
// присвоєний id використовуємо як унік індефікатор для короткого посилання
        return urlRecord.getId(); // зберігаємо айди
    }

    @Transactional // завдяки цьому автокоміту всі зміни будеть показуватися в базі
   // public String getUrl(long id) { // тут навпаки по id хочу отримати довгий урл повертає опшинал(обькт) за id
    public String getUrl(String link) {
        Optional<UrlRecord> urlOpt = urlRepository.findByLink(link);
        if (urlOpt.isEmpty())
            return null;

        UrlRecord urlRecord = urlOpt.get(); // отримали цей обьект
        urlRecord.setCount(urlRecord.getCount() + 1); // збільшиои його каунт на 1
        urlRecord.setLastAccess(LocalDateTime.now()); // встановили останню дату перегляду

        return urlRecord.getUrl(); // повертаємо це довге посилання
    }

    @Transactional(readOnly = true) // тут методи тільки читають з бази
    public List<UrlStatDTO> getStatistics() {
        List<UrlRecord> records = urlRepository.findAll(); // витягаємо всі записи з репо
        ArrayList<UrlStatDTO> result = new ArrayList<UrlStatDTO>(); // з листа ентеті створюємо лист дто

        records.forEach(x -> result.add(x.toStatDTO())); // конвертуе в дто

        return result;
    }


    // ініціалізується транзакція - виконується код якщо ексепшена нема то транзакція комит якщо є відкатується
    @Transactional
    public String saveUrl2(UrlDTO urlDTO) {
        UrlRecord urlRecord = urlRepository.findByUrl(urlDTO.getUrl()); // за довгим урл знайди запис в таблиці чи скорочували ми вже його
        if (urlRecord == null) { // якщо такого нема то це новий
            urlRecord = UrlRecord.of(urlDTO); // копіюємо в дто обьект і зберігаємо в базу
            urlRepository.save(urlRecord);
        }
        if (urlRecord.getUrl().length() > 15) {
            return getRandomString(7);
        }
        return urlRecord.getUrl();
    }

    public List<UrlRecord> listAll(){
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
